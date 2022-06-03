package com.example.dakt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@LargeTest
class CategoriesViewTest {
    @get:Rule
    val intentRule = ActivityScenarioRule(CategoriesView::class.java)

    @Test
    fun generateCategoriesGivesProperAmount() {
        val categories: MutableMap<Int, Category> = mutableMapOf()
        var id: Int = 0
        val resources: Array<String> = arrayOf("Nazywam", "sie", "Cezary", "Baryka", "i", "od", "dwudziestu", "minut", "jestem", "wlascicielem", "tego", "oto", "szklanego", "domu..", "Powoli", "Zaczynam", "zalowac", "zakupu", "W", "nocy", "pizga", "w", "dzien", "parowa", "Zero", "wentylacji", "oraz", "brak", "kanalizacji", "robia", "swoje")
        for (str in resources) {
            categories[id] = Category(id, str, "Dzien dobry poprosze kosmoslimy mentolowe, te najciensze. Z polki czy spod lady? Spod laady! 2.50. CO TAK DROGO?! To z polki... 2.50. ehhh, prosze. Zaraz, gdzie ja to mam. Europaleta to nie", (Random.nextInt(0, 10) < 5))
            id++
        }
        intentRule.scenario.onActivity{ activity -> activity.generateCategories(categories)}
        onView(withId(R.id.catScrollView)).check(matches(hasChildCount(id)))
    }
}