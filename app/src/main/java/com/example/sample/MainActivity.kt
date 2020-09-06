package com.example.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sample.util.CronetInstaller
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability

    @Inject
    lateinit var installer: CronetInstaller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onResume() {
        super.onResume()

        // Check availability here because this app requires Play Service and Cronet
        // but user can uninstall / force stop while it life cycle is in created state.
        googleApiAvailability.makeGooglePlayServicesAvailable(this)
            .addOnFailureListener { finish() }
            .addOnSuccessListener {
                installer.install(this)
                    .addOnSuccessListener { TODO() }
                    .addOnFailureListener { finish() }
            }
    }
}
