package ru.yamoney.test

import java.math.BigDecimal

interface Operation {
    val sum: BigDecimal
    val user: String
    fun calculate(oldBalance: BigDecimal): BigDecimal
}

data class Deposit(
        override val sum: BigDecimal,
        override val user: String
) : Operation {
    override fun calculate(oldBalance: BigDecimal): BigDecimal {
        return oldBalance + sum
    }
}

data class Payment(
        override val sum: BigDecimal,
        override val user: String,
        val shopid: String
) : Operation {
    override fun calculate(oldBalance: BigDecimal): BigDecimal {
        return oldBalance - sum
    }
}

data class WithDraw(
        override val sum: BigDecimal,
        override val user: String
) : Operation {
    override fun calculate(oldBalance: BigDecimal): BigDecimal {
        return oldBalance - sum
    }
}

data class P2P(override val sum: BigDecimal,
               override val user: String,
               val userTo:String

) : Operation {

    override fun calculate(oldBalance: BigDecimal): BigDecimal {
        return oldBalance - sum
    }
}

