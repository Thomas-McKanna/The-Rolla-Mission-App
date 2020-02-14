package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.registration.RegistationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegistrationActivityModule {
    @ContributesAndroidInjector(modules = [RegistrationFragmentsModule::class])
    abstract fun contributeRegistrationActivity(): RegistationActivity
}