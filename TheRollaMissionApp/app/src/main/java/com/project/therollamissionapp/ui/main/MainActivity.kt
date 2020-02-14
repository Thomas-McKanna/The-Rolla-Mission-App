package com.project.therollamissionapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.therollamissionapp.R
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
