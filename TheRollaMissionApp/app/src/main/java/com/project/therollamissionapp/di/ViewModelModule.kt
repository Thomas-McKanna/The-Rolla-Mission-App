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
    @ViewModelKey(IdentificationViewModel::class)
    abstract fun bindIdentificationViewModel(viewModel: IdentificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomelessViewModel::class)
    abstract fun bindHomelessViewModel(viewModel: HomelessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpecialCaseViewModel::class)
    abstract fun bindSpecialCaseViewModel(viewModel: SpecialCaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PictureViewModel::class)
    abstract fun bindPictureViewModel(viewModel: PictureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConsentViewModel::class)
    abstract fun bindConsentViewModel(viewModel: ConsentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckInViewModel::class)
    abstract fun bindCheckInViewModel(viewModel: CheckInViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MissionViewModelFactory): ViewModelProvider.Factory
}