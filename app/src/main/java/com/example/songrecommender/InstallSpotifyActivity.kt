package com.example.songrecommender

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

fun isSpotifyInstalled(packageManager: PackageManager): Boolean {
    try {
        packageManager.getPackageInfo("com.spotify.music", 0)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
}

class InstallSpotifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install_spotify)
        val installButton: Button = findViewById(R.id.installButton)
        if (isSpotifyInstalled(packageManager)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        installButton.setOnClickListener {
            openInstallOptions()
        }
    }

    private fun openInstallOptions() {
        val appPackageName = "com.spotify.music"
        val referrer =
            "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall"
        try {
            val uri: Uri = Uri.parse("market://details")
                .buildUpon()
                .appendQueryParameter("id", appPackageName)
                .appendQueryParameter("referrer", referrer)
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (ignored: ActivityNotFoundException) {
            val uri: Uri = Uri.parse("https://play.google.com/store/apps/details")
                .buildUpon()
                .appendQueryParameter("id", appPackageName)
                .appendQueryParameter("referrer", referrer)
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
