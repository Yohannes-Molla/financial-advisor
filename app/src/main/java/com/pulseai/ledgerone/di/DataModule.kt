package com.pulseai.ledgerone.di

import android.content.Context
import androidx.room.Room
import com.pulseai.ledgerone.data.local.AppDatabase
import com.pulseai.ledgerone.data.local.dao.TransactionDao
import com.pulseai.ledgerone.data.repository.TransactionRepositoryImpl
import com.pulseai.ledgerone.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import net.sqlcipher.database.SQLiteDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val passphrase = SQLiteDatabase.getBytes("ledgerone-secret-key".toCharArray()) // TODO: Use Keystore
        val factory = SupportFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ledgerone_db"
        )
        .openHelperFactory(factory)
        .build()
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(dao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(dao)
    }
}
