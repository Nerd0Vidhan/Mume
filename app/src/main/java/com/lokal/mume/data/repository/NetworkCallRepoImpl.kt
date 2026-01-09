package com.lokal.mume.data.repository

import android.net.http.HttpException
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.remote.NetworkHelper
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.domain.repository.ResultWrapper
import java.io.IOException
import javax.inject.Inject

class NetworkCallRepoImpl @Inject constructor(
    private val api: NetworkHelper
) : NetworkCallRepo {

    override suspend fun getHome()//: /*HomeData*/
    {
    }

    override suspend fun searchArtist(
        query: String,
        limit: Int
    ): ResultWrapper<ArtistResponse> {
        return try {
            val response = api.searchArtist(query, limit)
            ResultWrapper.Success(response)
        } catch (e: HttpException) {
            ResultWrapper.Failure(
                "Unable to Fetch Artists"
            )
        } catch (e: IOException) {
            ResultWrapper.Failure("Network error")
        } catch (e: Exception) {
            ResultWrapper.Failure(e.message ?: "Unknown error")
        }
    }

}