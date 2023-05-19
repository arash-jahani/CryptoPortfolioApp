package com.arashjahani.cryptoportfolioapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.arashjahani.cryptoportfolioapp.data.local.db.AppDatabase
import com.arashjahani.cryptoportfolioapp.data.local.db.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
open class DatabaseModule {

    @Provides
    @Singleton
    open fun provideApplicationDatabase(context: Context):AppDatabase{

        var applicationDatabase:AppDatabase=
            Room.databaseBuilder(context,AppDatabase::class.java,"portfolio-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        return applicationDatabase

    }

    @Provides
    @Singleton
    open fun provideTransactionDao(applicationDatabase: AppDatabase):TransactionDao{
        return applicationDatabase.transactionDao()
    }
}