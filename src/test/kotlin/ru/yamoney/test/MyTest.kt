package ru.yamoney.test

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

val SUM=123.toBigDecimal()
val NAME="Olga"
val SHOP_ID="85351"

class MyTest {
    private val file = File("operations")

    @BeforeEach
    fun deleteFileBefore()
    {
        try{file.delete()}catch(e:Exception){e.printStackTrace()}
    }

    @Test
    fun checkStringInFile(){
        Payment(user = NAME, sum=SUM, shopid = SHOP_ID)
    }
}