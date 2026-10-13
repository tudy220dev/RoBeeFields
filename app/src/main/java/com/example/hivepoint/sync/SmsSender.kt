package com.example.hivepoint.sync

import android.telephony.SmsManager
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.util.HiveSerializer

object SmsSender {
    fun sendHiveUpdate(phoneNumber: String, hive: Beehive) {
        val payload = HiveSerializer.serialize(hive)
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, payload, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
