package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.checkin.CheckInSuccessFragment
import com.project.therollamissionapp.ui.checkin.SearchFragment
import com.project.therollamissionapp.ui.main.WelcomeFragment
import com.project.therollamissionapp.ui.registration.RegistrationFragment
import com.project.therollamissionapp.ui.registration.RegistrationSuccessFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
