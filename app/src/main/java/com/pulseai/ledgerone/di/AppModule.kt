package com.pulseai.ledgerone.di

import android.content.Context
import com.pulseai.ledgerone.LedgerOneApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): LedgerOneApp {
        return app as LedgerOneApp
    }
}
