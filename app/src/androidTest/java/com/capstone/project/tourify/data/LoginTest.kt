package com.capstone.project.tourify.data

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.capstone.project.tourify.R
import com.capstone.project.tourify.ui.view.login.LoginActivity
import com.capstone.project.tourify.util.IdlingResourceSingleton
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResourceSingleton.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResourceSingleton.countingIdlingResource)
    }

    @Test
    fun testLogin() {
        // Fill in login details
        Espresso.onView(withId(R.id.edLoginEmail))
            .perform(ViewActions.typeText("rifqila245@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.edLoginPassword))
            .perform(ViewActions.typeText("12345678"), ViewActions.closeSoftKeyboard())

        // Click the login button
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(2000)
        Intents.init()
    }
}