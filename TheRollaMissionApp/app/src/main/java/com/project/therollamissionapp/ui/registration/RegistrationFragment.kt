package com.project.therollamissionapp.ui.registration

import DateFormattingTextWatcher
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
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.project.therollamissionapp.EventObserver

import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.*
import com.project.therollamissionapp.di.Injectable
import com.project.therollamissionapp.ui.common.Helpers.hideKeyboard
import com.project.therollamissionapp.ui.main.WelcomeFragmentDirections
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
        setupDropdownMenus()
        setupImageEventListener()
        setupHideKeyboardEventListener()
        setupDialogEvents()
        binding.editPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.editBirthdate.addTextChangedListener(DateFormattingTextWatcher(binding.editBirthdate))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.setImageUri(currentPhotoPath)
        }
    }

    private fun setupViewModel() {
        viewModel.patronCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            val navController = findNavController()
            navController.popBackStack(R.id.welcomeFragment, false)
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSearchFragment()
            navController.navigate(action)
        })
        viewModel.registrationCanceledEvent.observe(viewLifecycleOwner, EventObserver {
            requireView().findNavController().navigateUp()
        })
        viewModel.verifyPatronEvent.observe(viewLifecycleOwner, EventObserver {
            if (FormValidator(requireContext(), binding, viewModel).validateForm()) {
                viewModel.savePatron()
            }
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
            hideKeyboard(requireActivity())
        })
    }

    private fun setupDialogEvents() {
        viewModel.errorDialogEvent.observe(viewLifecycleOwner, EventObserver{
            viewModel.getErrorDialog(context)?.show()
        })
        viewModel.warningDialogEvent.observe(viewLifecycleOwner, EventObserver{
            viewModel.getWarningDialog(context)?.show()
        })
        viewModel.successDialogEvent.observe(viewLifecycleOwner, EventObserver{
            val dialog = viewModel.getSuccessDialog(context)
            dialog?.setCancelable(false)
            dialog?.show()
        })
    }

    private fun setupDropdownMenus() {
        val GENDERS = arrayOf(
            getString(R.string.male), getString(R.string.female),
            getString(R.string.other), getString(R.string.pref_not_answer)
        )
        val DURATIONS = arrayOf(
            getString(R.string.less_1_month), getString(R.string.months_1to3),
            getString(R.string.months_4to6), getString(R.string.months_6to12),
            getString(R.string.years_1to2), getString(R.string.more_2_years)
        )
        val REASONS_ROLLA = arrayOf(
            getString(R.string.family), getString(R.string.work), getString(R.string.school),
            getString(R.string.relationship), getString(R.string.hospital),
            getString(R.string.substance), getString(R.string.mission), getString(R.string.other)
        )
        setupDropDownMenu(binding.dropdownGender, GENDERS)
        setupDropDownMenu(binding.dropDownDurationHomeless, DURATIONS)
        setupDropDownMenu(binding.dropdownReasonRolla, REASONS_ROLLA)
    }

    private fun setupDropDownMenu(dropdown: AutoCompleteTextView, options: Array<String>) {
        val adapter = ArrayAdapter<String>(
            requireContext(), R.layout.dropdown_menu_popup_item, options)
        dropdown.setAdapter(adapter)
    }
}
