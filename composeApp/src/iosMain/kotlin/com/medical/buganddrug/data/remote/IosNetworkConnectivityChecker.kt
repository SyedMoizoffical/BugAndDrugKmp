package com.medical.buganddrug.data.remote

class IosNetworkConnectivityChecker : NetworkConnectivityChecker {
    override fun isNetworkAvailable(): Boolean {
        return true
    }
}
