package com.examp.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.examp.artbooktesting.model.ImageResponse
import com.examp.artbooktesting.roomdb.Art
import com.examp.artbooktesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertAll(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt() :LiveData<List<Art>>

    suspend fun searchImage(imageString : String) : Resource<ImageResponse>
}