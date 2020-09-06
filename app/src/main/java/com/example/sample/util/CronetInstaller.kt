package com.example.sample.util

import android.content.Context
import com.google.android.gms.net.CronetProviderInstaller
import com.google.android.gms.tasks.Task

class CronetInstaller {
    fun install(context: Context): Task<Void> {
        return CronetProviderInstaller.installProvider(context)
    }
}
