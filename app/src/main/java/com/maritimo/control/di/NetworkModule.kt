package com.maritimo.control.di

import com.maritimo.control.BuildConfig
import com.maritimo.control.data.remote.api.AuthApi
import com.maritimo.control.data.remote.api.PuertoApi
import com.maritimo.control.data.remote.api.MuelleApi
import com.maritimo.control.data.remote.api.BuqueApi
import com.maritimo.control.data.remote.api.CapitanApi
import com.maritimo.control.data.remote.api.AtraqueApi
import com.maritimo.control.data.remote.api.InspeccionApi
import com.maritimo.control.data.remote.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        if (BuildConfig.DEBUG) {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, trustAllCerts, SecureRandom())
            }

            builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providePuertoApi(retrofit: Retrofit): PuertoApi {
        return retrofit.create(PuertoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMuelleApi(retrofit: Retrofit): MuelleApi {
        return retrofit.create(MuelleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBuqueApi(retrofit: Retrofit): BuqueApi {
        return retrofit.create(BuqueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCapitanApi(retrofit: Retrofit): CapitanApi {
        return retrofit.create(CapitanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAtraqueApi(retrofit: Retrofit): AtraqueApi {
        return retrofit.create(AtraqueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInspeccionApi(retrofit: Retrofit): InspeccionApi {
        return retrofit.create(InspeccionApi::class.java)
    }
}
