package ru.yamoney.test

import com.winterbe.expekt.should
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import java.io.PrintStream
import java.io.ByteArrayOutputStream
import java.math.BigDecimal


private val SUM: BigDecimal = 123.toBigDecimal()
private val SUM_P2P = 10.toBigDecimal()
private const val USERNAME = "Olga"
private const val USERNAME_TO = "Ivan"
private const val SHOP_ID = "85351"
private const val depositLine = "ru.yamoney.test.Deposit||{\"sum\":%s,\"user\":\"%s\"}"
private const val paymentLine = "ru.yamoney.test.Payment||{\"sum\":%s,\"user\":\"%s\",\"shopid\":\"%s\"}"
private const val p2pLine = "ru.yamoney.test.P2P||{\"sum\":%s,\"user\":\"%s\",\"userTo\":\"%s\"}"

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
        val p2p = P2P(sum = SUM_P2P, user = USERNAME, userTo = USERNAME_TO)
        billing.addOperation(p2p)
        val strFromFile = file.readLines()
        assertAll("Проверяем, что в файл записалась нужная строка", {
            strFromFile.count().should.be.equal(1)
            strFromFile.first().should.contain(p2pLine.format(SUM_P2P, USERNAME, USERNAME_TO))
        })
    }

    @DisplayName("Проверяем, что баланс уменьшается, если выполнена операция Payment")
    @Test
    fun checkUserBalanceAfterPayment() {
        file.appendText(paymentLine.format(SUM, USERNAME, SHOP_ID))
        val balance = getBalanceFromConsole(USERNAME)

        balance.should.equal("-$SUM")
    }

    @DisplayName("Проверяем изменение баланса пользователей при P2P")
    @Test
    fun checkUserBalanceAfterPaymentDepositP2P() {
        file.appendText(p2pLine.format(SUM_P2P, USERNAME, USERNAME_TO) + "\n")
        val balanceUser1 = getBalanceFromConsole(USERNAME)
        val balanceUser2 = getBalanceFromConsole(USERNAME_TO)

        assertAll("Проверяем, что у первого пользователя сумма уменьшилась , а у второго увеличилась", {
            balanceUser1.should.be.equal("-$SUM_P2P")
            balanceUser2.should.be.equal(SUM_P2P.toString())
        })
    }

    //метод для считывания с консоли
    @Synchronized()
    private fun readconsole(action: () -> Unit): String {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        val old = System.out
        System.setOut(ps)
        action()
        System.out.flush()
        System.setOut(old)
        return baos.toString().trim()
    }

    private fun getBalanceFromConsole(username: String): String =
            readconsole({ billing.getUserBalance(username) })

}