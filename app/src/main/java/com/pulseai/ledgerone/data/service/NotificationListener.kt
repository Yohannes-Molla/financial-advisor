package com.pulseai.ledgerone.data.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.pulseai.ledgerone.data.parser.ParserEngine
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.usecase.SaveTransactionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var parserEngine: ParserEngine

    @Inject
    lateinit var saveTransactionUseCase: SaveTransactionUseCase

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = sbn?.packageName ?: return
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getString("android.text") ?: ""

        Log.d("NotificationListener", "Notification from $packageName: $title - $text")
        
        // TODO: maintain a list of supported bank package names
        // validation logic here
        
        val body = "$title $text"
        val transaction = parserEngine.parse(packageName, body) // Parser might need adjustment to handle pkg name as sender
        
        if (transaction != null) {
             val scope = CoroutineScope(Dispatchers.IO)
             scope.launch {
                 saveTransactionUseCase(transaction)
                 Log.d("NotificationListener", "Transaction saved: $transaction")
             }
        }
    }
}
