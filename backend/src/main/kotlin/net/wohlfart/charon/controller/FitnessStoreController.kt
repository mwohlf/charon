package net.wohlfart.charon.controller

import io.swagger.v3.oas.annotations.Parameter
import net.wohlfart.charon.api.FitnessStoreApi
import net.wohlfart.charon.model.FitnessDataItem
import net.wohlfart.charon.model.FitnessDataListElement
import net.wohlfart.charon.model.FitnessDataTimeseries
import net.wohlfart.charon.service.FitnessStoreService
import net.wohlfart.charon.service.OAuthTokenService
import net.wohlfart.charon.service.Predicate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// https://developers.google.com/fit/rest/v1/get-started

/*


Available operations
Fitness API v1

⊟ DataSources UsersQueries for user's data point changes for a particular data source.
Request: GET https://fitness.googleapis.com/fitness/v1/users/{userId}/dataSources/{dataSourceId}/dataPointChanges
Mandatory HTTP Headers:

⊟ DataSources UsersReturns a dataset containing all data points whose start and end times overlap with the specified range of the dataset minimum start time and maximum end time. Specifically, any data point whose start time is less than or equal to the dataset end time and whose end time is greater than or equal to the dataset start time.
Request: GET https://fitness.googleapis.com/fitness/v1/users/{userId}/dataSources/{dataSourceId}/datasets/{datasetId}
Mandatory HTTP Headers:

⊟ DataSources UsersReturns the specified data source.
Request: GET https://fitness.googleapis.com/fitness/v1/users/{userId}/dataSources/{dataSourceId}
Mandatory HTTP Headers:

⊟ DataSources UsersLists all data sources that are visible to the developer, using the OAuth scopes provided. The list is not exhaustive; the user may have private data sources that are only visible to other developers, or calls using other scopes.
Request: GET https://fitness.googleapis.com/fitness/v1/users/{userId}/dataSources
Mandatory HTTP Headers:

⊟ Dataset UsersAggregates data of a certain type or stream into buckets divided by a given type of boundary.
Multiple data sets of multiple types and from multiple sources can be aggregated into exactly one bucket type per request.
Request: POST https://fitness.googleapis.com/fitness/v1/users/{userId}/dataset:aggregate
Mandatory HTTP Headers:

⊟ Sessions UsersLists sessions previously created.
Request: GET https://fitness.googleapis.com/fitness/v1/users/{userId}/sessions
Mandatory HTTP Headers:
Close



--------------

relevant google endpoints
see:
https://developers.google.com/oauthplayground/?code=4/0Adeu5BU8Kpbk2EhWHU1fRVUBys3PTR_jfVH2M8fbJmjSssY_4C_OI1QWTCRQ3SJpQ1TxUA&scope=https://www.googleapis.com/auth/fitness.activity.read%20https://www.googleapis.com/auth/fitness.blood_glucose.read%20https://www.googleapis.com/auth/fitness.blood_pressure.read%20https://www.googleapis.com/auth/fitness.body.read%20https://www.googleapis.com/auth/fitness.body_temperature.read%20https://www.googleapis.com/auth/fitness.heart_rate.read%20https://www.googleapis.com/auth/fitness.location.read%20https://www.googleapis.com/auth/fitness.nutrition.read%20https://www.googleapis.com/auth/fitness.oxygen_saturation.read%20https://www.googleapis.com/auth/fitness.reproductive_health.read%20https://www.googleapis.com/auth/fitness.sleep.read



test requests:
https://developers.google.com/oauthplayground/


#  get a list of data sources with id and names
GET https://www.googleapis.com/fitness/v1/users/me/dataSources


#  get info about a data source
GET https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.distance.delta:com.google.android.gms:asus:Nexus 7:f0e3ca13:


#  get the data
GET https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.distance.delta:com.google.android.gms:asus:Nexus 7:f0e3ca13:/datasets/000000-1420845034000000000
    https://fitness.googleapis.com/fitness/v1/users/{userId}/dataSources/{dataSourceId}/datasets/{datasetId}

userId = me
dataSourceId = raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform
datasetId = 1688330144000000-1397515179728708316
->
https://    www.googleapis.com/fitness/v1/users/me/dataSources/raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform/datasets/1692136800000000-1693432800000000
https://fitness.googleapis.com/fitness/v1/users/me/dataSources/raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform/datasets/1688330144000000000-1693687024000000000
https://    www.googleapis.com/fitness/v1/users/me/dataSources/raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform/datasets/1693605600000000000-1693778400000000000
1688330144000000000
1693687024000000000

# create and retrieve an aggregated dataset:
POST https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate
{
  "aggregateBy": [{
    "dataTypeName": "com.google.step_count.delta",
    "dataSourceId": "derived:com.google.heart_rate.bpm:com.google.android.gms:merge_heart_rate_bpm"
  }],
  "bucketByTime": { "durationMillis": 86400000 },
  "startTimeMillis": 1693000000000,
  "endTimeMillis": 1693500000000
}



TODO: convert the timerange into a datasetId : the final request:


 https://fitness.googleapis.com/fitness/v1/users/me/dataSources/raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform/datasets/1688330144000000000-1693687024000000000 https://www.googleapis.com/fitness/v1/users/me/dataSources/raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform/datasets/1695506400000000000-1695592800000000000 1688330144000000000 begin is nanosec 1693687024000000000 end



*/

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class FitnessStoreController(
    val oAuthTokenService: OAuthTokenService,
    val fitnessStoreService: FitnessStoreService,
) : FitnessStoreApi {

    @Secured(value = ["SCOPE_profile"])
    override fun readFitnessDataList(
        @Parameter(description = "id of the user", required = true) @PathVariable("userId") userId: String
    ): ResponseEntity<List<FitnessDataListElement>> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication).block()
        // val predicate = Predicate.True()
        // "type": "raw"
        // "dataStreamId": "raw:com.google.step_count.delta:fitapp.fittofit:FitToFit - step count"
        val predicate = Predicate.And(
            // Predicate.Equals("type", "raw"),
            // Predicate.Matches("dataStreamId", Regex(".*heart.*"))
        )
        accessToken?.let { token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataList(token, userId, predicate))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Secured(value = ["SCOPE_profile"])
    override fun readFitnessDataItem(
        @Parameter(description = "id of the user", required = true) @PathVariable("userId") userId: String,
        @Parameter(description = "id of the data source", required = true) @PathVariable("dataSourceId") dataSourceId: String
    ): ResponseEntity<FitnessDataItem> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication).block()
        accessToken?.let { token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataItem(token, userId, dataSourceId))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Secured(value = ["SCOPE_profile"])
    override fun readFitnessDataTimeseries(
        @Parameter(description = "id of the user", required = true) @PathVariable("userId") userId: String,
        @Parameter(description = "id of the data source", required = true) @PathVariable("dataSourceId") dataSourceId: String,
        @Parameter(description = "id of the data set", required = true) @PathVariable("dataSetId") dataSetId: String
    ): ResponseEntity<FitnessDataTimeseries> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication).block()
        accessToken?.let { token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataTimeseries(token, userId, dataSourceId, dataSetId))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
