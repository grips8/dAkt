package com.example.dakt

import java.text.SimpleDateFormat

object DataValidator {
    fun checkIfValidDate (inp: String) : Boolean {
        val patt: Regex = Regex("(0?[1-9]|1[0-9]|2[0-9]|3[01])/(0?[1-9]|1[012])/(19[7-9][0-9]|2[0-9][0-9][0-9])")
        return inp.matches(patt)
    }

    fun checkIfValidDateAndTime (inp: String) : Boolean {
        val patt: Regex = Regex("(0?[1-9]|1[0-9]|2[0-9]|3[01])/(0?[1-9]|1[012])/(19[7-9][0-9]|2[0-9][0-9][0-9]) (0?[0-9]|1[0-9]|2[0123]):([0-5][0-9])")
        return inp.matches(patt)
    }

    fun checkIfValidStartFinishDateTimes (st: String, fin: String) : Boolean {
        return SimpleDateFormat("dd/MM/yyyy HH:mm").parse(st).time < SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fin).time
    }

    fun checkIfValidStartFinishDates (st: String, fin: String) : Boolean {
        return SimpleDateFormat("dd/MM/yyyy").parse(st).time <= SimpleDateFormat("dd/MM/yyyy").parse(fin).time
    }
}