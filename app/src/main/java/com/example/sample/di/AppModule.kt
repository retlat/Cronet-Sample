package com.example.sample.di

import com.example.sample.util.CronetInstaller
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

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
}
