package com.project.therollamissionapp.ui.checkin

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.therollamissionapp.AppExecutors

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.FragmentSearchBinding
import com.project.therollamissionapp.di.Injectable
import javax.inject.Inject

class CheckInSearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentSearchBinding

    lateinit var adapter: PatronListAdapter

    val viewModel: CheckInViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        setupViewModel()
        initRecyclerView()
        val pAdapter = PatronListAdapter(
            appExecutors = appExecutors
        ) { patron ->
            CheckInConfirmationDialogue(
                patron = patron,
                positiveListener = DialogInterface.OnClickListener() { dialog, id ->
                    viewModel.checkIn(patron)
                }
            ).show(requireActivity().supportFragmentManager, "confirmation")
        }
        binding.patronList.adapter = pAdapter
        adapter = pAdapter
    }

    fun setupViewModel() {
        viewModel.cancelEvent.observe(viewLifecycleOwner, Observer {
            findNavController()?.navigateUp()
        })
        viewModel.patronCheckInEvent.observe(viewLifecycleOwner, Observer {
            val action = CheckInSearchFragmentDirections.actionSearchFragmentToCheckInSuccessFragment()
            findNavController().navigate(action)
        })
        viewModel.checkInErrorEvent.observe(viewLifecycleOwner, Observer {
            // TODO
        })
    }

    private fun initRecyclerView() {
        viewModel.patrons.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.patronList.layoutManager = LinearLayoutManager(context)
    }
}
