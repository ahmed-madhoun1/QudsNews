package com.ahmedmadhoun.qudsnews.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceUtils : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("TAG", "From: " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.e("TAG", "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e("TAG", "Message Notification Body: " + remoteMessage.notification!!.body)
        }

    }

}