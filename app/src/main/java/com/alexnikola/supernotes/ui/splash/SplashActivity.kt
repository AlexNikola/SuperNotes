package com.alexnikola.supernotes.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alexnikola.supernotes.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
        finish()

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerPrint.with(this)
                    .withPassword("dfddfg")
                    .withFinger(true)
                    .withTitle("Title")
                    .withFingerMessage("Confirm fingerprint")
                    .withPasswordMessage("Enter your password")
                    .show()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }*/

    }
}
