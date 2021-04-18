package com.examp.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.examp.artbooktesting.api.RetrofitAPI
import com.examp.artbooktesting.roomdb.ArtDatabase
import com.examp.artbooktesting.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponentManager::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRommDatabase(
            @ApplicationContext context: Context) =Room.databaseBuilder(
            context,ArtDatabase::class.java,"ArtBookDB"
            ).build()

    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI() : RetrofitAPI {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(RetrofitAPI::class.java)
    }
}