package com.project.therollamissionapp.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.FragmentRegistrationBinding
import com.project.therollamissionapp.databinding.FragmentRegistrationSuccessBinding
import com.project.therollamissionapp.ui.main.WelcomeFragmentDirections

class RegistrationSuccessFragment : Fragment() {

    lateinit var binding: FragmentRegistrationSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registration_success, container, false)
        binding = FragmentRegistrationSuccessBinding.bind(root)

        binding.buttonCheckinComplete.setOnClickListener {
            val navController = it.findNavController()
            navController.popBackStack(R.id.welcomeFragment, false)
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSearchFragment()
            it.findNavController().navigate(action)
        }

        return binding.root
    }
}
