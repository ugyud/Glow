package com.dozkan.glow

import android.content.Context

class StringResourceProvider(private val context: Context) {
    operator fun invoke(resourceId: Int): String {
        return context.getString(resourceId)
    }
}