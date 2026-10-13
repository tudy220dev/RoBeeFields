package com.example.hivepoint.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast
import com.example.hivepoint.util.HiveSerializer

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val messageBody = sms.messageBody ?: continue
                if (messageBody.startsWith("HP1|")) {
                    val beehive = HiveSerializer.deserialize(messageBody)
                    if (beehive != null) {
                        Toast.makeText(
                            context,
                            "HivePoint Sync: Primit stup ${beehive.hiveNumber} (${beehive.status})",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
