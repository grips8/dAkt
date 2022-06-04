package com.example.dakt

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@LargeTest
class StatisticsViewTest {
    @get:Rule
    val intentRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun selectedActivitiesReturnProperDays() {
//        intentRule.scenario.onActivity{ activity -> activity.generateCategories()}


    }
}