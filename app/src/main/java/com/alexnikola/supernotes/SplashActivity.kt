package com.alexnikola.supernotes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alexnikola.fingerprint.FingerPrint
import com.alexnikola.supernotes.utils.AndroidUtilities


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidUtilities.lockOrientation(this)

        //startActivity(Intent(this, MainActivity::class.java))

        FingerPrint.with(this)
                .withPassword("dfddfg")
                .withFinger(true)
                .withTitle("Title")
                .withFingerMessage("Confirm fingerprint")
                .withPasswordMessage("Enter your password")
                .show()

    }
}
