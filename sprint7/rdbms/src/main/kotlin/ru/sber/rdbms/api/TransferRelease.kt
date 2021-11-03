package ru.sber.rdbms.api


interface TransferRelease: Iterable<Long> {
    fun <D,I> release(decrementCallback: (accountId: Long) -> D, incrementCallback: (accountId: Long) -> I): ReleaseResult<D,I>
}
