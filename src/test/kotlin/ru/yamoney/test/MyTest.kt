package ru.yamoney.test

import com.winterbe.expekt.should
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import java.io.PrintStream
import java.io.ByteArrayOutputStream


val SUM = 123.toBigDecimal()
val USERNAME = "Olga"
val USERNAME_TO = "Ivan"
val SHOP_ID = "85351"
val depositLine = "ru.yamoney.test.Deposit||{\"sum\":%s,\"user\":\"%s\"}"
val paymentLine = "ru.yamoney.test.Payment||{\"sum\":%s,\"user\":\"%s\",\"shopid\":\"%s\"}"
val p2pLine = "ru.yamoney.test.P2P||{\"sum\":%s,\"user\":\"%s\",\"userTo\":\"%s\"}"

class MyTest {
    private val file = File("operations")
    val billing = BillingImpl()

    @BeforeEach
    fun deleteFileBefore() {
        try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @DisplayName("Проверка записи операции Deposit в файл")
    @Test
    fun addOperationDepositTest() {
        val deposit = Deposit(sum = SUM, user = USERNAME)
        billing.addOperation(deposit)
        val strFromFile = file.readLines()
        assertAll("Проверяем, что в файл записана нужная строка", {
            strFromFile.count().should.be.equal(1)
            strFromFile.first().should.contain(depositLine.format(SUM, USERNAME))
        })

    }

    @DisplayName("Проверка записи операции Payment в файл")
    @Test
    fun addOperationPaymentTest() {
        val payment = Payment(sum = SUM, user = USERNAME, shopid = SHOP_ID)
        billing.addOperation(payment)
        val strFromFile = file.readLines()
        assertAll("Проверяем, что в файл записана нужная строка", {
            strFromFile.count().should.be.equal(1)
            strFromFile.first().should.contain(paymentLine.format(SUM, USERNAME, SHOP_ID))
        })

    }

    @DisplayName("Проверка записи операции P2P в файл")
    @Test
    fun addOperationP2PTest() {
        val p2p = P2P(sum = SUM, user = USERNAME, userTo = USERNAME_TO)
        billing.addOperation(p2p)
        val strFromFile = file.readLines()
        assertAll("Проверяем, что в файл записалась нужная строка", {
            strFromFile.count().should.be.equal(1)
            strFromFile.first().should.contain(p2pLine.format(SUM, USERNAME, USERNAME_TO))
        })
    }

    @DisplayName("Проверяем, что баланс уменьшается, если выполнена операция Payment")
    @Test
    fun checkUserBalanceAfterPayment() {
        file.appendText(paymentLine.format(SUM, USERNAME, SHOP_ID))

        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        val old = System.out
        System.setOut(ps)
        billing.getUserBalance(USERNAME)
        System.out.flush()
        System.setOut(old)
        val balance = baos.toString().trim()
        
        balance.should.equal("-$SUM")
    }

}