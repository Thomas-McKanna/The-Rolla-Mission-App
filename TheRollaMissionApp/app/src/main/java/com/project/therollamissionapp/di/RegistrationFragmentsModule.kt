package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.registration.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegistrationFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment
}
