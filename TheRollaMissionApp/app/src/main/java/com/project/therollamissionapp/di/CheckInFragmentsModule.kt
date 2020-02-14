package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.checkin.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CheckInFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}