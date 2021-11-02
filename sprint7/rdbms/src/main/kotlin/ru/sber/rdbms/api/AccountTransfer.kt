package ru.sber.rdbms.api

interface AccountTransfer {

    fun transfer(decrementAccountId: Long, incrementAccountId: Long, amount: Long)

}