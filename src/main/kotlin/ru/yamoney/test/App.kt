package ru.yamoney.test

import java.math.BigDecimal

const val USAGE_MESSAGE = """
java -jar app.jar [COMMAND] [ARGUMENTS]
            Commands:
            payment %USER% %SUM% %SHOP_ID%
            deposit %USER% %SUM%
            balance %USER%
            shop_info %SHOP_ID%
            p2p %USER% %SUM% %USER%
"""

val billing: Billing = BillingImpl()

fun main(args: Array<String>) {
    try {
        when (args.command()) {
            "PAYMENT" -> billing.addOperation(Payment(args[2].toBigDecimal(), args[1], args[3]))
            "DEPOSIT" -> billing.addOperation(Deposit(BigDecimal(args[2]), args[1]))
            "BALANCE" -> billing.getUserBalance(args[1])
            "SHOP_INFO" -> billing.getShopIdOperations(args[1])
            "P2P" -> billing.addOperation(P2P(args[2].toBigDecimal(),args[1],  args[3]))
            else -> throw IllegalArgumentException("Unknown Command")
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        print(USAGE_MESSAGE)
    }
}

private fun Array<String>.command(): String = this[0].toUpperCase()