package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import net.wohlfart.charon.model.FitnessDataItem
import net.wohlfart.charon.model.FitnessDataListElement
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigInteger


// https://developers.google.com/fit/rest/v1/get-started

// https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.step_count.delta:1234567890:Example%20Manufacturer:ExampleTablet:1000001:MyDataSource
// https://stackoverflow.com/questions/64956764/what-data-source-id-to-use-for-google-fit-rest-heart-rate-query
// https://developers.google.com/fit/datatypes/aggregate
// https://developers.google.com/oauthplayground

// raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform

/*
https://developers.google.com/oauthplayground/
POST https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate

{
  "aggregateBy": [{
    "dataTypeName": "health_platform",
    "dataSourceId": "raw:com.google.distance.delta:com.fitbit.FitbitMobile:health_platform"
  }],
  "bucketByTime": { "durationMillis": 86400000 },
  "startTimeMillis": 1691000000000,
  "endTimeMillis": 1693500000000
}

{
  "aggregateBy": [{
    "dataTypeName": "health_platform",
    "dataSourceId": "raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform"
  }],
  "bucketByTime": { "durationMillis": 86400000 },
  "startTimeMillis": 1691000000000,
  "endTimeMillis": 1693500000000
}
 */

private val logger = KotlinLogging.logger {}
private var mapper = ObjectMapper()


@Service
class FitnessStoreService(
    private val fitStoreClientBuilder: WebClient.Builder
) {


    fun readFitnessDataList(
        accessToken: AccessToken,
        userId: String,
        predicate: Predicate,
        ): List<FitnessDataListElement> {

        // https://www.googleapis.com/fitness/v1/resourcePath?parameters

        val bodyString = fitStoreClientBuilder.build()
            .method(HttpMethod.GET)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
        val requestResult = mapper.readTree(bodyString) as ObjectNode
        val dataSource = requestResult["dataSource"] as ArrayNode

        return dataSource
            .filter(predicate)
            .map {
            FitnessDataListElement(
                id = it["dataStreamId"].asText(),
                name = it["dataStreamName"].asText(),
                type = it["type"].asText(),
                dataTypeName = it["dataType"]["name"].asText(),
            )
        }
    }

    fun readFitnessDataItem(accessToken: AccessToken, userId: String, dataSourceId: String): FitnessDataItem? {
        val bodyString = fitStoreClientBuilder.build()
            .method(HttpMethod.GET)
            .uri("/$dataSourceId")
            // .uri("/" + URLEncoder.encode(id, "UTF-8"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
        val requestResult = mapper.readTree(bodyString) as ObjectNode
        logger.info { "requestResult: $requestResult" }
        // {
        // "dataStreamId":"derived:com.google.active_minutes:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
        // "dataStreamName":"top_level",
        // "type":"derived",
        // "dataType":{"name":"com.google.active_minutes",
        // "field":[{"name":"duration","format":"integer"}]},
        // "device":{"uid":"53f7246b","type":"phone","version":"",
        // "model":"Pixel 6a",
        // "manufacturer":"Google"},
        // "application":{"packageName":"com.google.android.fit"},
        // "dataQualityStandard":[]}
        return FitnessDataItem(
            id = requestResult["dataStreamId"].asText(),
            name = requestResult["dataStreamName"].asText(),
        )
    }

    fun readFitnessDataSet(accessToken: AccessToken, userId: String, dataSourceId: String, dataSetId: String): FitnessDataItem? {
        val bodyString = fitStoreClientBuilder.build()
            .method(HttpMethod.GET)
            .uri("/$dataSourceId/datasets/$dataSetId")
            // .uri("/" + URLEncoder.encode(id, "UTF-8"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val requestResult = mapper.readTree(bodyString) as ObjectNode
        val minStartTimeNs = requestResult["minStartTimeNs"]?.asText()
        val maxEndTimeNs = requestResult["maxEndTimeNs"]?.asText()
        val dataSourceIdIncoming = requestResult["dataSourceId"]?.asText()
        val point = requestResult["point"]?.asText()

        if (minStartTimeNs == null || maxEndTimeNs == null || dataSourceIdIncoming == null || point == null) {
            throw ServiceException("Can't find data for $dataSourceId")
        }

        val minStartTimeSec = BigInteger(minStartTimeNs).divide(BigInteger.valueOf(1000 * 1000)).longValueExact()
        val maxEndTimeSec = BigInteger(minStartTimeNs).divide(BigInteger.valueOf(1000 * 1000)).longValueExact()

        return FitnessDataItem(
            id = dataSourceIdIncoming,
            name = dataSourceIdIncoming,
        )
    }

}

