@file:Suppress("unused", "DEPRECATION")

package com.apboutos.moneytrack.model.repository.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


object NetworkTester {
    /**
     * Returns true if the device is connected to the internet either via a wifi connection or via mobile data.
     */
    fun internetConnectionIsAvailable(context: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: Array<NetworkInfo> = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true)) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true)) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}