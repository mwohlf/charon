package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import net.wohlfart.charon.model.FitDataSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


// https://developers.google.com/fit/rest/v1/get-started

private val logger = KotlinLogging.logger {}
private var mapper = ObjectMapper()


@Service
class FitnessStoreService(
    private val fitStoreClientBuilder: WebClient.Builder
) {

    fun fetchDataSources(accessToken: AccessToken): List<FitDataSource> {

        // https://www.googleapis.com/fitness/v1/resourcePath?parameters

        val bodyString = fitStoreClientBuilder.build()
            .method(HttpMethod.GET)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
        val requestResult = mapper.readTree(bodyString) as ObjectNode
        val dataSource = requestResult["dataSource"] as ArrayNode

        return dataSource.map {
            FitDataSource(
                id = it["dataStreamId"].asText(),
                name = it["dataStreamName"].asText(),
                type = it["type"].asText(),
                dataTypeName = it["dataType"]["name"].asText(),
            )
        }
    }

}


/*

{
  "dataSource": [
    {
      "dataStreamId": "derived:com.google.active_minutes:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.active_minutes",
        "field": [
          {
            "name": "duration",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.active_minutes:com.google.android.gms:merge_active_minutes",
      "dataStreamName": "merge_active_minutes",
      "type": "derived",
      "dataType": {
        "name": "com.google.active_minutes",
        "field": [
          {
            "name": "duration",
            "format": "integer",
            "optional": true
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.fitbit.FitbitMobile:session_activity_segment",
      "dataStreamName": "session_activity_segment",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.apps.fitness:session_activity_segment",
      "dataStreamName": "session_activity_segment",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.apps.fitness"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:Google:Pixel 3a:68b036a5:activity_from_steps",
      "dataStreamName": "activity_from_steps",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:Google:Pixel 3a:68b036a5:from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:Google:Pixel 3a:68b036a5:detailed",
      "dataStreamName": "from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:Google:Pixel 3a:68b036a5:detailed",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:HTC:m8:8f2f48be:activity_from_steps",
      "dataStreamName": "activity_from_steps",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:HTC:m8:8f2f48be:from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:HTC:m8:8f2f48be:detailed",
      "dataStreamName": "from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:HTC:m8:8f2f48be:detailed",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:htc:m8:278b2ce7:activity_from_steps",
      "dataStreamName": "activity_from_steps",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:htc:m8:278b2ce7:from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:htc:m8:278b2ce7:detailed",
      "dataStreamName": "from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:htc:m8:278b2ce7:detailed",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:htc:m8:74e1de4d:activity_from_steps",
      "dataStreamName": "activity_from_steps",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:htc:m8:74e1de4d:from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:htc:m8:74e1de4d:detailed",
      "dataStreamName": "from_activity_samples\u003c-derived:com.google.activity.samples:com.google.android.gms:htc:m8:74e1de4d:detailed",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:merge_activity_segments",
      "dataStreamName": "merge_activity_segments",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:merge_activity_segments_local",
      "dataStreamName": "merge_activity_segments_local",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.google.android.gms:session_activity_segment",
      "dataStreamName": "session_activity_segment",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:com.strava:session_activity_segment",
      "dataStreamName": "session_activity_segment",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.strava"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.activity.segment:fitapp.fittofit:session_activity_segment",
      "dataStreamName": "session_activity_segment",
      "type": "derived",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.calories.bmr:com.google.android.gms:merged",
      "dataStreamName": "merged",
      "type": "derived",
      "dataType": {
        "name": "com.google.calories.bmr",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.calories.expended:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.calories.expended:com.google.android.gms:merge_calories_expended",
      "dataStreamName": "merge_calories_expended",
      "type": "derived",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.calories.expended:com.strava:",
      "dataStreamName": "",
      "type": "derived",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.strava"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:Google:Pixel 3a:68b036a5:from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:Google:Pixel 3a:68b036a5:live_location",
      "dataStreamName": "from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:Google:Pixel 3a:68b036a5:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:Google:Pixel 3a:68b036a5:from_location\u003c-raw:com.google.location.sample:com.google.android.gms:Google:Pixel 3a:68b036a5:live_location",
      "dataStreamName": "from_location\u003c-raw:com.google.location.sample:com.google.android.gms:Google:Pixel 3a:68b036a5:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:HTC:m8:8f2f48be:from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:HTC:m8:8f2f48be:live_location",
      "dataStreamName": "from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:HTC:m8:8f2f48be:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:HTC:m8:8f2f48be:from_location\u003c-raw:com.google.location.sample:com.google.android.gms:HTC:m8:8f2f48be:live_location",
      "dataStreamName": "from_location\u003c-raw:com.google.location.sample:com.google.android.gms:HTC:m8:8f2f48be:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:from_high_accuracy_location\u003c-derived:com.google.location.sample:com.google.android.gms:merge_high_fidelity",
      "dataStreamName": "from_high_accuracy_location\u003c-derived:com.google.location.sample:com.google.android.gms:merge_high_fidelity",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:htc:m8:278b2ce7:from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:278b2ce7:live_location",
      "dataStreamName": "from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:278b2ce7:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:htc:m8:278b2ce7:from_location\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:278b2ce7:live_location",
      "dataStreamName": "from_location\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:278b2ce7:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:htc:m8:74e1de4d:from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:74e1de4d:live_location",
      "dataStreamName": "from_activity\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:74e1de4d:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:htc:m8:74e1de4d:from_location\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:74e1de4d:live_location",
      "dataStreamName": "from_location\u003c-raw:com.google.location.sample:com.google.android.gms:htc:m8:74e1de4d:live_location",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.google.android.gms:merge_distance_delta",
      "dataStreamName": "merge_distance_delta",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.distance.delta:com.strava:",
      "dataStreamName": "",
      "type": "derived",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.strava"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_minutes:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_minutes",
        "field": [
          {
            "name": "intensity",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_minutes:com.google.android.gms:bout_filtered_5min\u003c-merge_heart_minutes",
      "dataStreamName": "bout_filtered_5min\u003c-merge_heart_minutes",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_minutes",
        "field": [
          {
            "name": "intensity",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_minutes:com.google.android.gms:from_heart_rate\u003c-merge_heart_rate_bpm",
      "dataStreamName": "from_heart_rate\u003c-merge_heart_rate_bpm",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_minutes",
        "field": [
          {
            "name": "intensity",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_minutes:com.google.android.gms:merge_heart_minutes",
      "dataStreamName": "merge_heart_minutes",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_minutes",
        "field": [
          {
            "name": "intensity",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_minutes:com.google.android.gms:normalised\u003c-bout_filtered_5min",
      "dataStreamName": "normalised\u003c-bout_filtered_5min",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_minutes",
        "field": [
          {
            "name": "intensity",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_rate.bpm:com.google.android.gms:merge_heart_rate_bpm",
      "dataStreamName": "merge_heart_rate_bpm",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_rate.bpm",
        "field": [
          {
            "name": "bpm",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.heart_rate.bpm:com.google.android.gms:resting_heart_rate\u003c-merge_heart_rate_bpm",
      "dataStreamName": "resting_heart_rate\u003c-merge_heart_rate_bpm",
      "type": "derived",
      "dataType": {
        "name": "com.google.heart_rate.bpm",
        "field": [
          {
            "name": "bpm",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.sleep.segment:com.google.android.gms:merged",
      "dataStreamName": "merged",
      "type": "derived",
      "dataType": {
        "name": "com.google.sleep.segment",
        "field": [
          {
            "name": "sleep_segment_type",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.speed:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.speed",
        "field": [
          {
            "name": "speed",
            "format": "floatPoint"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.speed:com.google.android.gms:merge_speed",
      "dataStreamName": "merge_speed",
      "type": "derived",
      "dataType": {
        "name": "com.google.speed",
        "field": [
          {
            "name": "speed",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.cumulative:com.google.android.gms:HTC:m8:8f2f48be:soft_step_counter",
      "dataStreamName": "soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.cumulative",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:278b2ce7:soft_step_counter",
      "dataStreamName": "soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.cumulative",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:74e1de4d:soft_step_counter",
      "dataStreamName": "soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.cumulative",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.fit:Google:Pixel 6a:53f7246b:top_level",
      "dataStreamName": "top_level",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.fit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:Google:Pixel 3a:68b036a5:derive_step_deltas\u003c-raw:com.google.step_count.cumulative:Google:Pixel 3a:68b036a5:Step Counter",
      "dataStreamName": "derive_step_deltas\u003c-raw:com.google.step_count.cumulative:Google:Pixel 3a:68b036a5:Step Counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:HTC:m8:8f2f48be:derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:HTC:m8:8f2f48be:soft_step_counter",
      "dataStreamName": "derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:HTC:m8:8f2f48be:soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "8f2f48be",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "HTC"
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:estimated_steps",
      "dataStreamName": "estimated_steps",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:htc:m8:278b2ce7:derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:278b2ce7:soft_step_counter",
      "dataStreamName": "derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:278b2ce7:soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "278b2ce7",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:htc:m8:74e1de4d:derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:74e1de4d:soft_step_counter",
      "dataStreamName": "derive_step_deltas\u003c-derived:com.google.step_count.cumulative:com.google.android.gms:htc:m8:74e1de4d:soft_step_counter",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "74e1de4d",
        "type": "phone",
        "version": "",
        "model": "m8",
        "manufacturer": "htc"
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "derived:com.google.step_count.delta:com.google.android.gms:merge_step_deltas",
      "dataStreamName": "merge_step_deltas",
      "type": "derived",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.gms",
        "version": "1"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:com.google.android.apps.fitness:user_input",
      "dataStreamName": "user_input",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.apps.fitness"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:",
      "dataStreamName": "",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Aerobic Workout - activity segments",
      "dataStreamName": "Aerobic Workout - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Bike - activity segments",
      "dataStreamName": "Bike - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Elliptical - activity segments",
      "dataStreamName": "Elliptical - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Run - activity segments",
      "dataStreamName": "Run - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Sleep - activity segments",
      "dataStreamName": "Sleep - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Sport - activity segments",
      "dataStreamName": "Sport - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Swim - activity segments",
      "dataStreamName": "Swim - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Treadmill - activity segments",
      "dataStreamName": "Treadmill - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.activity.segment:fitapp.fittofit:Walk - activity segments",
      "dataStreamName": "Walk - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.activity.segment",
        "field": [
          {
            "name": "activity",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:com.fitbit.FitbitMobile:health_platform",
      "dataStreamName": "health_platform",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:",
      "dataStreamName": "",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Aerobic Workout - calories",
      "dataStreamName": "Aerobic Workout - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Bike - calories",
      "dataStreamName": "Bike - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Elliptical - calories",
      "dataStreamName": "Elliptical - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Run - calories",
      "dataStreamName": "Run - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Sport - calories",
      "dataStreamName": "Sport - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Swim - calories",
      "dataStreamName": "Swim - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Treadmill - calories",
      "dataStreamName": "Treadmill - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.calories.expended:fitapp.fittofit:Walk - calories",
      "dataStreamName": "Walk - calories",
      "type": "raw",
      "dataType": {
        "name": "com.google.calories.expended",
        "field": [
          {
            "name": "calories",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.distance.delta:com.fitbit.FitbitMobile:health_platform",
      "dataStreamName": "health_platform",
      "type": "raw",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.distance.delta:fitapp.fittofit:Bike - distance",
      "dataStreamName": "Bike - distance",
      "type": "raw",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.distance.delta:fitapp.fittofit:FitToFit - distance",
      "dataStreamName": "FitToFit - distance",
      "type": "raw",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.distance.delta:fitapp.fittofit:Swim - distance",
      "dataStreamName": "Swim - distance",
      "type": "raw",
      "dataType": {
        "name": "com.google.distance.delta",
        "field": [
          {
            "name": "distance",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.heart_rate.bpm:com.fitbit.FitbitMobile:health_platform",
      "dataStreamName": "health_platform",
      "type": "raw",
      "dataType": {
        "name": "com.google.heart_rate.bpm",
        "field": [
          {
            "name": "bpm",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.heart_rate.bpm:com.google.android.apps.fitness:user_input",
      "dataStreamName": "user_input",
      "type": "raw",
      "dataType": {
        "name": "com.google.heart_rate.bpm",
        "field": [
          {
            "name": "bpm",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "com.google.android.apps.fitness"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.heart_rate.bpm:fitapp.fittofit:FitToFit - heart rate",
      "dataStreamName": "FitToFit - heart rate",
      "type": "raw",
      "dataType": {
        "name": "com.google.heart_rate.bpm",
        "field": [
          {
            "name": "bpm",
            "format": "floatPoint"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.sleep.segment:com.fitbit.FitbitMobile:health_platform",
      "dataStreamName": "health_platform",
      "type": "raw",
      "dataType": {
        "name": "com.google.sleep.segment",
        "field": [
          {
            "name": "sleep_segment_type",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.sleep.segment:fitapp.fittofit:Sleep - activity segments",
      "dataStreamName": "Sleep - activity segments",
      "type": "raw",
      "dataType": {
        "name": "com.google.sleep.segment",
        "field": [
          {
            "name": "sleep_segment_type",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.step_count.cumulative:Google:Pixel 3a:68b036a5:Step Counter",
      "dataStreamName": "Step Counter",
      "type": "raw",
      "dataType": {
        "name": "com.google.step_count.cumulative",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "68b036a5",
        "type": "phone",
        "version": "",
        "model": "Pixel 3a",
        "manufacturer": "Google"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.step_count.cumulative:Google:Pixel 6a:53f7246b:Step Counter",
      "dataStreamName": "Step Counter",
      "type": "raw",
      "dataType": {
        "name": "com.google.step_count.cumulative",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "device": {
        "uid": "53f7246b",
        "type": "phone",
        "version": "",
        "model": "Pixel 6a",
        "manufacturer": "Google"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.step_count.delta:com.fitbit.FitbitMobile:health_platform",
      "dataStreamName": "health_platform",
      "type": "raw",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "com.fitbit.FitbitMobile"
      },
      "dataQualityStandard": []
    },
    {
      "dataStreamId": "raw:com.google.step_count.delta:fitapp.fittofit:FitToFit - step count",
      "dataStreamName": "FitToFit - step count",
      "type": "raw",
      "dataType": {
        "name": "com.google.step_count.delta",
        "field": [
          {
            "name": "steps",
            "format": "integer"
          }
        ]
      },
      "application": {
        "packageName": "fitapp.fittofit"
      },
      "dataQualityStandard": []
    }
  ]
}

 */
