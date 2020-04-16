package com.project.therollamissionapp

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var adminComponentName: ComponentName
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var decorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupKioskMode()
        decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { hideSystemUI() }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    fun disableKioskMode() {
        if (devicePolicyManager.isDeviceOwnerApp(packageName)) {
            stopLockTask()
            devicePolicyManager.clearPackagePersistentPreferredActivities(adminComponentName, packageName)
            finish()
        }
    }

    private fun setupKioskMode() {
        adminComponentName = AdminReceiver.getComponentName(this)
        devicePolicyManager = ContextCompat.getSystemService(
            this,
            DevicePolicyManager::class.java
        ) as DevicePolicyManager
        if (devicePolicyManager.isDeviceOwnerApp(packageName)) {
            devicePolicyManager.setLockTaskPackages(adminComponentName, arrayOf(packageName))
            configureKioskBehavior()
            startLockTask()
        } else {
            Toast.makeText(this, getString(R.string.no_admin_permission), Toast.LENGTH_LONG).show()
        }
    }

    private fun configureKioskBehavior() {
        // App will start upon device boot
        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
        intentFilter.addCategory(Intent.CATEGORY_HOME)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        devicePolicyManager.addPersistentPreferredActivity(adminComponentName,
            intentFilter, ComponentName(packageName, MainActivity::class.java.name))

        // Disable lock screen
        devicePolicyManager.setKeyguardDisabled(adminComponentName, true)

        // Screen will never sleep while charging (the screen will still dim after some time)
        devicePolicyManager.setGlobalSetting(adminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            Integer.toString(BatteryManager.BATTERY_PLUGGED_AC or
                    BatteryManager.BATTERY_PLUGGED_USB or
                    BatteryManager.BATTERY_PLUGGED_WIRELESS)
        )
    }

    private fun hideSystemUI() {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
    }
}
