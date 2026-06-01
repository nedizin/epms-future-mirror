package com.epms.epmssmartmirror.data

data class Lead(
    val timestamp: String,
    val profile: String,
    val photoStyle: String = "landscape",
    val name: String,
    val school: String,
    val yearLevel: String,
    val contact: String,
    val deliveryEmail: String,
    val interestArea: String,
    val savedLandscape: Boolean = false,
    val savedSquare: Boolean = false,
    val savedStory: Boolean = false,
    val savedPrintLandscape: Boolean = false,
    val savedPrintPortrait: Boolean = false
)
