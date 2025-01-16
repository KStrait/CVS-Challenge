package com.kls.cvschallenge.data

import android.os.Parcelable
import kotlinx.serialization.Serializable

data class FlickrResponse(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<FlickrImage>
)

@Serializable
@kotlinx.parcelize.Parcelize
data class FlickrImage(
    val title: String,
    val link: String,
    val media: Media,
    val date_taken: String,
    val description: String,
    val published: String,
    val author: String,
    val author_id: String,
    val tags: String
) : Parcelable
@Serializable
@kotlinx.parcelize.Parcelize
data class Media(
    val m: String
) : Parcelable

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}