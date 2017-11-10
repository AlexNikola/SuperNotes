package com.alexnikola.fingerprint

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerPrint {

    private var activity: AppCompatActivity? = null

    private var fragment: Fragment? = null

    private var password: String? = null

    private var withFinger: Boolean = false

    private var title: String? = null

    private var fingerMessage: String? = null

    private var passwordMessage: String? = null

    private constructor(activity: AppCompatActivity) {
        this.activity = activity
    }

    private constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    fun withPassword(password: String): FingerPrint {
        this.password = password
        return this
    }

    fun withFinger(withFinger: Boolean): FingerPrint {
        this.withFinger = withFinger
        return this
    }

    fun withTitle(title: String): FingerPrint {
        this.title = title
        return this
    }

    fun withFingerMessage(fingerMessage: String): FingerPrint {
        this.fingerMessage = fingerMessage
        return this
    }

    fun withPasswordMessage(passwordMessage: String): FingerPrint {
        this.passwordMessage = passwordMessage
        return this
    }

    fun show(): FingerPrintDialog {
        val dialogFragment = FingerPrintDialog.newInstance(password, withFinger, title, fingerMessage, passwordMessage)

        if (password == null && !withFinger) {
            throw IllegalArgumentException("Need to setup password of fingerprint")
        }

        when {
            activity != null -> dialogFragment.show(activity!!.supportFragmentManager, DIALOG_FRAGMENT_TAG)
            fragment != null -> dialogFragment.show(fragment!!.fragmentManager, DIALOG_FRAGMENT_TAG)
            else -> throw IllegalArgumentException("Need to provide context")
        }

        return dialogFragment
    }

    companion object {

        private val DIALOG_FRAGMENT_TAG = "FingerPrintDialog"

        fun with(activity: AppCompatActivity?): FingerPrint {
            if (activity == null) {
                throw IllegalArgumentException("Activity must not be null")
            }
            return FingerPrint(activity)
        }

        fun with(fragment: Fragment?): FingerPrint {
            if (fragment == null) {
                throw IllegalArgumentException("Fragment must not be null")
            }
            return FingerPrint(fragment)
        }
    }
}
