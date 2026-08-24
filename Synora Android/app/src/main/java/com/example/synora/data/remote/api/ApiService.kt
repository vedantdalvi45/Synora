package com.example.synora.data.remote.api

import retrofit2.http.GET

// Placeholder — endpoints will be added per feature in later phases.
interface ApiService {

    @GET("health")
    suspend fun healthCheck(): Map<String, String>
}
