package com.project.therollamissionapp.ui.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.therollamissionapp.EventObserver

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.*
import com.project.therollamissionapp.di.Injectable
import com.project.therollamissionapp.ui.common.Helpers.hideKeyboard
import com.project.therollamissionapp.ui.main.WelcomeFragmentDirections
import com.project.therollamissionapp.util.setupSnackbar
import java.io.File
import java.io.IOException
import javax.inject.Inject

class RegistrationFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentRegistrationBinding

    private val viewModel: RegistrationViewModel by viewModels {
        viewModelFactory
    }

    val REQUEST_TAKE_PHOTO = 1

    lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registration, container, false)
        binding = FragmentRegistrationBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        binding.setLifecycleOwner { lifecycle }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupSnackbar()
        setupPickBirthDateEventListener()
        setupImageEventListener()
        setupHideKeyboardEventListener()
        setupErrorDialogueEvent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.setImageUri(currentPhotoPath)
        }
    }

    private fun setupViewModel() {
        viewModel.contentChangedEvent.observe(viewLifecycleOwner, EventObserver {
            setContent(it)
        })
        viewModel.patronCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            val navController = findNavController()
            navController.popBackStack(R.id.welcomeFragment, false)
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationSuccessFragment()
            navController.navigate(action)
        })
        viewModel.registrationCanceledEvent.observe(viewLifecycleOwner, EventObserver {
            view!!.findNavController().navigateUp()
        })
    }

    private fun setContent(layoutId: Int) {
        val frameRoot = layoutInflater.inflate(layoutId, binding.content, false)
        val regBinding = when (layoutId) {
            R.layout.reg_part1 -> RegPart1Binding.bind(frameRoot).apply {
                this.viewmodel = viewModel
                editPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            }
            R.layout.reg_part2 -> RegPart2Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part3 -> RegPart3Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            R.layout.reg_part4 -> RegPart4Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
            else -> RegPart5Binding.bind(frameRoot).apply { this.viewmodel = viewModel }
        }
        regBinding.setLifecycleOwner { lifecycle }
        binding.content.removeAllViews()
        binding.content.addView(frameRoot)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupPickBirthDateEventListener() {
        viewModel.birthDateDialogueEvent.observe(viewLifecycleOwner, EventObserver{
            it.show(activity!!.supportFragmentManager, "datePicker")
        })
    }

    private fun setupImageEventListener() {
        viewModel.takeImageEvent.observe(viewLifecycleOwner, EventObserver{id ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile(id)
                    } catch (ex: IOException) {
                        // TODO
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "com.project.therollamissionapp.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        })
    }

    @Throws(IOException::class)
    private fun createImageFile(id: String): File {
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, "${id}.jpg").apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun setupHideKeyboardEventListener() {
        viewModel.hideKeyboardEvent.observe(viewLifecycleOwner, EventObserver{
            hideKeyboard(view)
        })
    }

    private fun setupErrorDialogueEvent() {
        viewModel.errorDialogueEvent.observe(viewLifecycleOwner, EventObserver{
            viewModel.getErrorDialogue(context)?.show()
        })
    }
}
