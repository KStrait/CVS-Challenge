package com.kls.cvschallenge.network

import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.data.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    companion object {
        const val BASE_URL = "https://api.flickr.com/services/feeds/"
    }

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getFlickrImages(@Query("tags") search: String): FlickrResponse
}