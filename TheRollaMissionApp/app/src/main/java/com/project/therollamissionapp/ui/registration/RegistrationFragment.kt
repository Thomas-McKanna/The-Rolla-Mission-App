package com.project.therollamissionapp.ui.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.project.therollamissionapp.EventObserver

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.*
import com.project.therollamissionapp.util.setupSnackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentRegistrationBinding

    private val viewModel: RegistrationViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registration, container, false)
        binding = FragmentRegistrationBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupSnackbar()
    }

    private fun setupViewModel() {
        viewModel.contentChangedEvent.observe(viewLifecycleOwner, EventObserver {
            setContent(it)
        })
        viewModel.patronCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            val fragment = CompleteFragment()
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.apply {
                val transaction = beginTransaction()
                transaction.replace(R.id.content_frame, fragment)
                transaction.commit()
            }
        })
    }

    private fun setContent(layoutId: Int) {
        val frameRoot = layoutInflater.inflate(layoutId, binding.content, false)
        when (layoutId) {
            R.layout.reg_part1 -> RegPart1Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part2 -> RegPart2Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part3 -> RegPart3Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part4 -> RegPart4Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part5 -> RegPart5Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
        }
        binding.content.removeAllViews()
        binding.content.addView(frameRoot)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}
