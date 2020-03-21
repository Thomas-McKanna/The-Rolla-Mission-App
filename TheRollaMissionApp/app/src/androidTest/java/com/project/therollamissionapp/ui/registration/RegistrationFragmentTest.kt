package com.project.therollamissionapp.ui.registration

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.project.therollamissionapp.R
import com.project.therollamissionapp.util.FakePatronRepository
import com.project.therollamissionapp.testing.SingleFragmentActivity
import com.project.therollemissionapp.util.*
import com.project.therollamissionapp.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        onView(withId(R.id.edit_name))
            .perform(ViewActions.typeText("Steve Brule"))
        onView(withId(R.id.edit_birthdate))
            .perform(ViewActions.typeText("01022020"))
        onView(withId(R.id.edit_phone_number))
            .perform(ViewActions.typeText("5555555555"))

        onView(withId(R.id.dropdown_gender)).perform(click())
        onView(withText("Male"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());

        onView(withId(R.id.drop_down_duration_homeless)).perform(click())
        onView(withText("Less than 1 month"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());

        onView(withId(R.id.button_submit)).perform(click())
        onView(withId(R.id.text_rolla_when_became_homeless))
            .check(matches(hasTextColor(R.color.error)))

        onView(withId(R.id.radio_from_rolla_n)).perform(click())
        onView(withId(R.id.edit_city))
            .perform(ViewActions.typeText("Los Angeles"))
        onView(withId(R.id.dropdown_reason_rolla)).perform(click())
        onView(withText("School"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());

        onView(withId(R.id.button_hide_keyboard)).perform(click())
        onView(withId(R.id.radio_violence_n)).perform(click())
        onView(withId(R.id.radio_veteran_n)).perform(click())
        onView(withId(R.id.radio_offender_n)).perform(click())

        val expectedIntent = allOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
        intending(expectedIntent)
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
        onView(withId(R.id.button_take_picture))
            .perform(click())

        onView(withId(R.id.checkbox_consent_1)).perform(click())
        onView(withId(R.id.checkbox_consent_2)).perform(click())
        onView(withId(R.id.checkbox_consent_3)).perform(click())

        onView(withId(R.id.button_submit)).perform(click())
        onView(withId(R.id.text_signature)).check(matches(hasTextColor(R.color.error)))

        onView(withId(R.id.sp)).perform(ViewActions.swipeRight())
    }
}