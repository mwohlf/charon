package net.wohlfart.charon.controller

import io.swagger.v3.oas.annotations.Parameter
import net.wohlfart.charon.api.FitnessStoreApi
import net.wohlfart.charon.model.FitnessDataItem
import net.wohlfart.charon.model.FitnessDataListElement
import net.wohlfart.charon.service.FitnessStoreService
import net.wohlfart.charon.service.OAuthTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// https://developers.google.com/fit/rest/v1/get-started

/*

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
        accessToken?.let { token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataList(token, userId))
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
    override fun readFitnessDataSet(
        @Parameter(description = "id of the user", required = true) @PathVariable("userId") userId: String,
        @Parameter(description = "id of the data source", required = true) @PathVariable("dataSourceId") dataSourceId: String,
        @Parameter(description = "id of the data set", required = true) @PathVariable("dataSetId") dataSetId: String
    ): ResponseEntity<FitnessDataItem> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication).block()
        accessToken?.let { token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataSet(token, userId, dataSourceId, dataSetId))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
