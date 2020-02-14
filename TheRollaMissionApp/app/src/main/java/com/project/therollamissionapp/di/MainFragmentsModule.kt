package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.main.WelcomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeWelcomeFragment(): WelcomeFragment
}
