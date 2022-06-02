package com.example.dakt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val intentRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun switchChangesActivityToCategories() {
        Intents.init()
        onView(withId(R.id.switchCatButton)).perform(click())
        intended(hasComponent(CategoriesView::class.java.name))
        Intents.release()
    }

    @Test
    fun switchChangesActivityToStatistics() {

    }
}