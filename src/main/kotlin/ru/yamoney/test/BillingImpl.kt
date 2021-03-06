package ru.yamoney.test

import com.google.gson.Gson
import java.io.File
import java.math.BigDecimal

class BillingImpl : Billing {
    private val file = File("operations")
    private val gson = Gson()

    override fun addOperation(operation: Operation) {
        file.appendText("${operation::class.java.name}||${gson.toJson(operation)}\n")
    }

    override fun getUserBalance(user: String) {
        var userBalance = BigDecimal.ZERO
        file.bufferedReader().lineSequence().map { it.toOperation() }.forEach {
            when {
                it.user == user ->
                    userBalance = it.calculate(userBalance)
                it is P2P && it.userTo == user ->
                    userBalance = it.sum + userBalance
            }
        }
        println(userBalance)
    }

    override fun getShopIdOperations(shopId: String) {
        file.readLines()
                .map { it.toOperation() }
                .filter { it is Payment && it.shopid == shopId }
                .forEach {
                    println(it)
                }
    }

    private fun String.toOperation(): Operation {
        val type = this.substringBefore("||")
        val json = this.substringAfter("||")
        return gson.fromJson<Operation>(json, Class.forName(type))
    }
}