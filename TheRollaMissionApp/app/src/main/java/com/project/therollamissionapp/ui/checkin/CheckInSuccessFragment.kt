package com.project.therollamissionapp.ui.checkin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.FragmentCheckInSuccessBinding

class CheckInSuccessFragment : Fragment() {
    lateinit var binding: FragmentCheckInSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_check_in_success, container, false)
        binding = FragmentCheckInSuccessBinding.bind(root)

        binding.buttonReturnStart.setOnClickListener {
            val action = CheckInSuccessFragmentDirections.actionGlobalWelcomeFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
