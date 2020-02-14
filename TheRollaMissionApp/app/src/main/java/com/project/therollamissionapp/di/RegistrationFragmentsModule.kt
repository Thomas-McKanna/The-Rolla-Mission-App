package com.project.therollamissionapp.di

import com.project.therollamissionapp.ui.registration.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegistrationFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeIdentificationFragment(): IdentificationFragment
    @ContributesAndroidInjector
    abstract fun contributeHomelessFragment(): HomelessFragment
    @ContributesAndroidInjector
    abstract fun contributeSpecialCaseFragment(): SpecialCaseFragment
    @ContributesAndroidInjector
    abstract fun contributePictureFragment(): PictureFragment
    @ContributesAndroidInjector
    abstract fun contributeConsentFragment(): ConsentFragment
    @ContributesAndroidInjector
    abstract fun contributeFinishedFragment(): FinishedFragment
}
