package ru.yamoney.test


//логика платежной системы
interface Billing {
    fun getShopIdOperations(shopId: String)
    fun getUserBalance(user: String)
    fun addOperation(operation: Operation)
}