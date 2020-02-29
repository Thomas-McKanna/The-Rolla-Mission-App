package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.checkin.CheckInSearchFragment
import com.project.therollamissionapp.ui.registration.RegistrationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): CheckInSearchFragment
}
