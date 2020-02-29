package com.project.therollamissionapp.ui.checkin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
            // TODO: show confirmation dialogue
            Log.d("TEST", "CLICKED")
        }
        binding.patronList.adapter = pAdapter
        adapter = pAdapter
    }

    fun setupViewModel() {
        viewModel.cancelEvent.observe(viewLifecycleOwner, Observer {
            view?.findNavController()?.navigateUp()
        })
    }

    private fun initRecyclerView() {
        viewModel.patrons.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.patronList.layoutManager = LinearLayoutManager(context)
    }
}
