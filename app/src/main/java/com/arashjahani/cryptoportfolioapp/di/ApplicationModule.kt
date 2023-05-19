package com.arashjahani.cryptoportfolioapp.di

import android.app.Application
import android.content.Context
import com.arashjahani.cryptoportfolioapp.PortfolioApplication
import com.arashjahani.cryptoportfolioapp.data.DataRepository
import com.arashjahani.cryptoportfolioapp.data.DataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun providesApplication(portfolioApplication: PortfolioApplication):PortfolioApplication{
        return portfolioApplication
    }

    @Provides
    @Singleton
    fun provideContext(application:Application):Context{
        return application
    }

    @Provides
    @Singleton
    fun provideDataRepository(dataRepositoryImpl: DataRepositoryImpl):DataRepository=dataRepositoryImpl


}