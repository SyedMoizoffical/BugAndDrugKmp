package com.medical.buganddrug.util

object NetworkErrorHandler {

    const val NO_INTERNET_MESSAGE = "No internet connection. Please check your network and try again."
    const val TIMEOUT_MESSAGE = "Connection timed out. Please check your internet connection and try again."
    const val SERVER_ERROR_MESSAGE = "Unable to connect to the server. Please try again later."
    const val SESSION_EXPIRED_MESSAGE = "Your session has expired. Please relogin."
    const val GENERIC_ERROR_MESSAGE = "Something went wrong. Please try again."

    fun isSessionExpired(message: String?): Boolean {
        if (message.isNullOrBlank()) return false
        val lower = message.lowercase()
        return lower.contains("401") ||
                lower.contains("unauthorized") ||
                lower.contains("session has expired") ||
                lower.contains("session in expire") ||
                lower.contains("session is expired") ||
                lower.contains("session expired") ||
                lower.contains("token expired") ||
                lower.contains("invalid token") ||
                lower.contains("authentication failed") ||
                lower.contains("please relogin") ||
                lower.contains("please log in again")
    }

    fun sanitizeMessage(message: String?): String {
        if (message.isNullOrBlank()) return GENERIC_ERROR_MESSAGE

        val lower = message.lowercase()

        // 0. Session expired / 401 Unauthorized
        if (isSessionExpired(message)) {
            return SESSION_EXPIRED_MESSAGE
        }

        // 1. Connectivity / DNS / Socket errors
        if (lower.contains("no internet") ||
            lower.contains("unable to resolve host") ||
            lower.contains("unknownhostexception") ||
            lower.contains("unresolvedaddressexception") ||
            lower.contains("network is unreachable") ||
            lower.contains("enetunreach") ||
            lower.contains("no address associated with hostname") ||
            lower.contains("failed to connect to") ||
            lower.contains("connectexception") ||
            lower.contains("connection refused") ||
            lower.contains("network error") ||
            lower.contains("route to host")
        ) {
            return NO_INTERNET_MESSAGE
        }

        // 2. Timeout errors
        if (lower.contains("timeout") ||
            lower.contains("timed out") ||
            lower.contains("sockettimeoutexception") ||
            lower.contains("connecttimeoutexception") ||
            lower.contains("httprequesttimeoutexception")
        ) {
            return TIMEOUT_MESSAGE
        }

        // 3. Server 5xx / gateway errors
        if (lower.contains("500 internal server error") ||
            lower.contains("502 bad gateway") ||
            lower.contains("503 service unavailable") ||
            lower.contains("504 gateway timeout") ||
            lower.contains("server response") ||
            lower.contains("serverresponseexception")
        ) {
            return SERVER_ERROR_MESSAGE
        }

        // 4. Serialization / payload errors
        if (lower.contains("serializationexception") ||
            lower.contains("jsondecodingexception") ||
            lower.contains("unexpected json token")
        ) {
            return "Unable to process server response. Please try again later."
        }

        // 5. Sanitize any URLs, IP addresses, ports, and API endpoints
        var cleaned = message
        // Remove full URLs (http://... or https://...)
        cleaned = cleaned.replace(Regex("https?://[^\\s,)\\]]+"), "")
        // Remove IP addresses and ports (e.g., 154.26.128.29:905 or 192.168.1.1)
        cleaned = cleaned.replace(Regex("\\b(?:\\d{1,3}\\.){3}\\d{1,3}(?::\\d+)?\\b"), "")
        // Remove API path prefixes (e.g., /api/SignIn or /api/GetInitialAppData)
        cleaned = cleaned.replace(Regex("/?api/[A-Za-z0-9_/-]+", RegexOption.IGNORE_CASE), "")
        // Remove Java / Kotlin exception class names
        cleaned = cleaned.replace(Regex("^[a-zA-Z0-9_.]+(?:Exception|Error):\\s*"), "")
        cleaned = cleaned.trim().trim(':', '-', ',', ' ')

        if (cleaned.isBlank() || cleaned.length < 3) {
            return SERVER_ERROR_MESSAGE
        }

        return cleaned
    }

    fun getFriendlyMessage(throwable: Throwable?): String {
        if (throwable == null) return GENERIC_ERROR_MESSAGE
        return sanitizeMessage(throwable.message)
    }
}

fun Throwable.toUserFriendlyMessage(): String = NetworkErrorHandler.getFriendlyMessage(this)
