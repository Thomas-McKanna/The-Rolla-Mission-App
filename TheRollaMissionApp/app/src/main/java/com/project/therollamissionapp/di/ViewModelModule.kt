package com.project.therollamissionapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.therollamissionapp.ui.checkin.CheckInViewModel
import com.project.therollamissionapp.ui.registration.*
import com.project.therollamissionapp.viewmodel.MissionViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckInViewModel::class)
    abstract fun bindCheckInViewModel(viewModel: CheckInViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MissionViewModelFactory): ViewModelProvider.Factory
}