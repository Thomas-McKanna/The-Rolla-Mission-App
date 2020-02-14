package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.checkin.CheckInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CheckInActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeCheckInActivity(): CheckInActivity
}