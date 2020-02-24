package com.project.therollamissionapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_welcome, container, false)
        binding = FragmentWelcomeBinding.bind(root)

        binding.buttonCheckin.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSearchFragment()
            it.findNavController().navigate(action)
        }

        binding.buttonRegister.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment()
            it.findNavController().navigate(action)
        }

        return binding.root
    }
}
