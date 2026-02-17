package com.pulseai.ledgerone.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.pulseai.ledgerone.data.parser.ParserEngine
import com.pulseai.ledgerone.domain.usecase.SaveTransactionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var parserEngine: ParserEngine

    @Inject
    lateinit var saveTransactionUseCase: SaveTransactionUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            messages.forEach { sms ->
                val sender = sms.displayOriginatingAddress
                val body = sms.displayMessageBody
                
                Log.d("SmsReceiver", "Received SMS from: $sender")

                val transaction = parserEngine.parse(sender, body)
                if (transaction != null) {
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        saveTransactionUseCase(transaction)
                        Log.d("SmsReceiver", "Transaction saved: $transaction")
                    }
                }
            }
        }
    }
}
