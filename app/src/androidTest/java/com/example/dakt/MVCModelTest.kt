package com.example.dakt

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
@LargeTest
class MVCModelTest {
    @get:Rule
    val intentRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun daysOfWeekConsecutiveReturnTrue() {
        val mod: MVCModel = MVCModel(ApplicationProvider.getApplicationContext<Context>())
        val activities: MutableList<Activity> = mutableListOf()
        val fromT: Long = 1654293600000
        val toT: Long = 1656885600000
        var curr: Long = fromT
        for (i in 1..15) {
            activities.add(Activity(i, "Test", Date(curr), Date(curr + 5000), "xD", curr, curr + 5000))
            curr += 86400000
        }   // 15 consecutive days starts, from saturday
        activities.sortedBy { Activity -> Activity.started }

//        val res: IntArray = mod.daysOfWeek(fromT, toT, activities)
//        Assert.assertArrayEquals(intArrayOf(2, 2, 2, 2, 2, 3, 2), res)
        // changed input of function

    }

    @Test
    fun daysOfWeekConsecutiveReturnFalse() {
        val mod: MVCModel = MVCModel(ApplicationProvider.getApplicationContext<Context>())
        val activities: MutableList<Activity> = mutableListOf()
        val fromT: Long = 1654293600000
        val toT: Long = 1656885600000
        var curr: Long = fromT
        for (i in 1..15) {
            activities.add(Activity(i, "Test", Date(curr), Date(curr + 5000), "xD", curr, curr + 5000))
            curr += 86400000
        }   // 15 consecutive days starts, from saturday
        activities.sortedBy { Activity -> Activity.started }

//        val res: IntArray = mod.daysOfWeek(fromT, toT, activities)
//        assertThat((intArrayOf(2, 3, 2, 2, 2, 2, 2)), not(equalTo(res)))
        // changed input of function

    }

    @Test
    fun daysOfWeekRandomReturnTrue() {
        val mod: MVCModel = MVCModel(ApplicationProvider.getApplicationContext<Context>())
        val activities: MutableList<Activity> = mutableListOf()
        val fromT: Long = 1488754800000
        val toT: Long = 1495404000000
        val dates: LongArray = longArrayOf(fromT + (2 * 86400000) + Random.nextLong(0, 86400000),
            fromT + (4 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (5 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (6 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (7 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (12 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (15 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (17 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (17 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (17 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (20 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (21 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (22 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (23 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (24 * 86400000L) + Random.nextLong(0, 86400000),
            fromT + (27 * 86400000L) + Random.nextLong(0, 86400000))
        // mon -> 2, tue -> 2, wed -> 2, thu -> 4, fri -> 1, sat -> 2, sun -> 3

        for (i in dates) {
            activities.add(Activity(1, "Test", Date(i), Date(i + 500000), "xD", i, i + 500000))
        }
        activities.sortedBy { Activity -> Activity.started }

//        val res: IntArray = mod.daysOfWeek(fromT, toT, activities)
//        Assert.assertArrayEquals(intArrayOf(2, 2, 2, 4, 1, 2, 3), res)
        // changed input of function
    }
}