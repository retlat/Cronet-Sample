package com.example.sample.di

import android.content.Context
import com.example.sample.data.DefaultJSONCallback
import com.example.sample.data.DefaultSampleDataSource
import com.example.sample.data.JSONCallback
import com.example.sample.data.SampleDataSource
import com.example.sample.util.CronetInstaller
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.chromium.net.CronetEngine

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideGoogleApiAvailability(): GoogleApiAvailability {
        return GoogleApiAvailability.getInstance()
    }

    @Provides
    fun provideCronetInstaller(): CronetInstaller {
        return CronetInstaller()
    }

    @Provides
    fun provideCronetEngine(@ApplicationContext context: Context): CronetEngine {
        return CronetEngine.Builder(context)
            .enableHttp2(true)
            .enableQuic(true)
            .enableBrotli(true)
            .build()
    }

    @IODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideJSONCallback(): JSONCallback {
        return DefaultJSONCallback()
    }

    @Provides
    fun provideSampleDataSource(
        engine: CronetEngine,
        callback: JSONCallback,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): SampleDataSource {
        return DefaultSampleDataSource(engine, callback, dispatcher)
    }
}
