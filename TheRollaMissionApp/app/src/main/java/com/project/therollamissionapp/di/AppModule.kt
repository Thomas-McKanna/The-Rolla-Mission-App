package com.project.therollamissionapp.di

import android.app.Application
import android.util.Base64
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.project.therollamissionapp.BuildConfig
import com.project.therollamissionapp.data.ApiEndpointInterface
import com.project.therollamissionapp.data.source.local.MissionDb
import com.project.therollamissionapp.data.source.local.PatronDao
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): MissionDb {
        return Room
            .databaseBuilder(app, MissionDb::class.java, "mission.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePatronDao(db: MissionDb): PatronDao {
        return db.patronDao()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(): ApiEndpointInterface {
        val API_BASE_URL = "http://192.168.1.14:8000/api/"  // TODO: change
        val client = getOkHttpClientWithAuthorization()
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiEndpointInterface::class.java)
    }

    fun getOkHttpClientWithAuthorization(): OkHttpClient {
        val basicAuth = getBasicAuthorizationString()
        val interceptor = getAuthorizationInterceptor(basicAuth)
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(interceptor)
        return builder.build()
    }

    fun getBasicAuthorizationString(): String {
        val credentials = "${BuildConfig.API_USER}:${BuildConfig.API_PASS}"
        return "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

    fun getAuthorizationInterceptor(basicAuth: String): Interceptor {
        return Interceptor { chain ->
            val newRequest: Request =
                chain.request().newBuilder().addHeader("Authorization", basicAuth).build()
            chain.proceed(newRequest)
        }
    }
}