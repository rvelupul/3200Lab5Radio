package com.radio.data

import android.graphics.drawable.Drawable

class RadioStation(
    var radioStreamUrl: String,
    var radioName: String,
    var radioIcon: Drawable?,
    var playing: Boolean = false
)