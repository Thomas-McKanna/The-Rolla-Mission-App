package com.project.therollamissionapp.di

import com.project.therollamissionapp.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [MainFragmentsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}