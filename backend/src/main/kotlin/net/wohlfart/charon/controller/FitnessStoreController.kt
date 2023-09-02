package net.wohlfart.charon.controller

import net.wohlfart.charon.api.FitnessStoreApi
import net.wohlfart.charon.model.FitnessDataItem
import net.wohlfart.charon.model.FitnessDataListElement
import net.wohlfart.charon.service.FitnessStoreService
import net.wohlfart.charon.service.OAuthTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// https://developers.google.com/fit/rest/v1/get-started

/*

relevant google endpoints:


#  get a list of data sources with id and names
GET https://www.googleapis.com/fitness/v1/users/me/dataSources


#  get info about a data source
GET https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.distance.delta:com.google.android.gms:asus:Nexus 7:f0e3ca13:


#  get the data
GET https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.distance.delta:com.google.android.gms:asus:Nexus 7:f0e3ca13:/datasets/000000-1420845034000000000


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
    override fun readFitnessDataList(): ResponseEntity<List<FitnessDataListElement>> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication) .block()
        accessToken?.let {token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataList(token))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Secured(value = ["SCOPE_profile"])
    override fun readFitnessDataItem(id: String): ResponseEntity<FitnessDataItem> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication) .block()
        accessToken?.let {token ->
            return ResponseEntity.ok(fitnessStoreService.readFitnessDataItem(token, id))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

}
