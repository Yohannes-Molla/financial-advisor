package com.pulseai.ledgerone.data.local

import androidx.room.TypeConverter
import com.pulseai.ledgerone.domain.model.BankType
import com.pulseai.ledgerone.domain.model.TransactionType
import com.pulseai.ledgerone.domain.model.VerificationStatus

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromBankType(value: BankType): String = value.name

    @TypeConverter
    fun toBankType(value: String): BankType = BankType.valueOf(value)

    @TypeConverter
    fun fromVerificationStatus(value: VerificationStatus): String = value.name

    @TypeConverter
    fun toVerificationStatus(value: String): VerificationStatus = VerificationStatus.valueOf(value)
}
