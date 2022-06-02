package com.example.dakt

import com.example.dakt.DataValidator
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DataValidatorTest {

    @Test
    fun `date wrong day returns false` () {
        val result = DataValidator.checkIfValidDate("0/11/2020")
        assertThat(result).isFalse()
    }
    @Test
    fun `date wrong month returns false` () {
        val result = DataValidator.checkIfValidDate("15/13/2020")
        assertThat(result).isFalse()
    }
    @Test
    fun `date wrong year returns false` () {
        val result = DataValidator.checkIfValidDate("0/11/1940")
        assertThat(result).isFalse()
    }
    @Test
    fun `date wrong delimiters returns false` () {
        val result = DataValidator.checkIfValidDate("15.11.2020")
        assertThat(result).isFalse()
    }
    @Test
    fun `date single digit day returns true` () {
        val result = DataValidator.checkIfValidDate("5/11/2019")
        assertThat(result).isTrue()
    }
    @Test
    fun `date single digit month returns true` () {
        val result = DataValidator.checkIfValidDate("17/4/2020")
        assertThat(result).isTrue()
    }
    @Test
    fun `date random letters returns false` () {
        val result = DataValidator.checkIfValidDate("ad/dz/fqws")
        assertThat(result).isFalse()
    }

    @Test
    fun `date-time wrong day returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("0/11/2020 16:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time wrong month returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("15/13/2020 16:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time wrong year returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("0/11/1940 16:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time wrong delimiters returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("15.11.2020 16:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time single digit day returns true` () {
        val result = DataValidator.checkIfValidDateAndTime("5/11/2019 16:20")
        assertThat(result).isTrue()
    }
    @Test
    fun `date-time single digit month returns true` () {
        val result = DataValidator.checkIfValidDateAndTime("17/4/2020 16:20")
        assertThat(result).isTrue()
    }
    @Test
    fun `date-time random letters returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("ad/dz/fqws 16:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time wrong minutes returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("5/11/2019 15:66")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time wrong hours returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("5/11/2019 25:20")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time single digit minute returns false` () {
        val result = DataValidator.checkIfValidDateAndTime("5/11/2019 16:5")
        assertThat(result).isFalse()
    }
    @Test
    fun `date-time single digit hour returns true` () {
        val result = DataValidator.checkIfValidDateAndTime("5/11/2019 4:20")
        assertThat(result).isTrue()
    }
}