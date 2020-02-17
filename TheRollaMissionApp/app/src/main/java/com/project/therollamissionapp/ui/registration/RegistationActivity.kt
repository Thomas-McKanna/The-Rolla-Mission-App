package com.project.therollamissionapp.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.therollamissionapp.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class RegistationActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject((this))
        super.onCreate(savedInstanceState)
        val fragment = RegistrationFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
        setContentView(R.layout.activity_registration)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}
