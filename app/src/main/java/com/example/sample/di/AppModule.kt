package com.example.sample.di

import android.content.Context
import com.example.sample.util.CronetInstaller
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
}
