package com.project.therollamissionapp.ui.registration

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.project.therollamissionapp.R
import com.project.therollamissionapp.util.FakePatronRepository
import com.project.therollamissionapp.testing.SingleFragmentActivity
import com.project.therollemissionapp.util.*
import com.project.therollamissionapp.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

/*
 * NOTE: in order to run these tests, you may need to enabled developer options
 * [https://developer.android.com/studio/debug/dev-options] and then disable the
 * following three settings:
 *   1) Window animation scale
 *   2) Transition animation scale
 *   3) Animator duration scale
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class RegistrationFragmentTest {
    @Rule
    @JvmField
    val activityRule = IntentsTestRule(SingleFragmentActivity::class.java, true, true)

    private val navController = mock<NavController>()
    private lateinit var viewModel: RegistrationViewModel

    private val registrationFragment = RegistrationFragment()

    @Before
    fun init() {
        viewModel = RegistrationViewModel(FakePatronRepository())
        registrationFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        Navigation.setViewNavController(
            activityRule.activity.findViewById<View>(R.id.container),
            navController)
        activityRule.activity.setFragment(registrationFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun registration_EndToEnd() {
        // Fill out some fields, but not all
        onView(withId(R.id.edit_first_name))
            .perform(ViewActions.typeText("Steve"))
        onView(withId(R.id.edit_last_name))
            .perform(ViewActions.typeText("Brule"))
        onView(withId(R.id.edit_phone))
            .perform(ViewActions.typeText("5555555555"))
        onView(withId(R.id.radio_gender_male)).perform(click())

        // Click Next
        onView(withId(R.id.next))
            .perform(click())

        // Verify snackbar appeared
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.field_incomplete)))

        onView(withId(R.id.button_birthday)).perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(1985, 5, 5))
        onView(withText("OK")).perform(click());

        onView(withId(R.id.next))
            .perform(click())

        onView(withId(R.id.edit_city_before_homeless))
            .perform(ViewActions.typeText("Rolla"))
        onView(withId(R.id.radio_reason_relationship)).perform(click())

        onView(withId(R.id.next))
            .perform(click())

        onView(withId(R.id.radio_homeless_1to3month)).perform(click())
        onView(withId(R.id.radio_violence_n)).perform(click())
        onView(withId(R.id.radio_veteran_n)).perform(click())
        onView(withId(R.id.radio_offender_y)).perform(click())

        onView(withId(R.id.next))
            .perform(click())

       val expectedIntent = allOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))

        intending(expectedIntent)
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))

        onView(withId(R.id.take_picture_button))
            .perform(click())

        onView(withId(R.id.next))
            .perform(click())

        onView(withId(R.id.checkbox_consent_1)).perform(click())
        onView(withId(R.id.checkbox_consent_2)).perform(click())
        onView(withId(R.id.checkbox_consent_3)).perform(click())
        onView(withId(R.id.sp)).perform(ViewActions.swipeRight())
    }
}