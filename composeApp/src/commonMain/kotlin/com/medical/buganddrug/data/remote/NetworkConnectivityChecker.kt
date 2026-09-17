package com.medical.buganddrug.data.remote

interface NetworkConnectivityChecker {
    fun isNetworkAvailable(): Boolean
}
