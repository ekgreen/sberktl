package ru.sber.rdbms.api


class ReUsableTransferRelease(private val decrementAccountId: Long, private val incrementAccountId: Long, private val comparator: Comparator<Long> = AvoidDeadLockComparator()
): TransferRelease {

    override fun iterator(): Iterator<Long> {
        return listOf(decrementAccountId, incrementAccountId).sortedWith(comparator).iterator()
    }

    override fun <D,I> release(decrementCallback: (accountId: Long) -> D, incrementCallback: (accountId: Long) -> I): ReleaseResult<D,I> {
        val decrementValue: D;
        val incrementValue: I;
        // поддерживаем порядок исполнения операций согласно конвенции
        if (comparator.compare(decrementAccountId,incrementAccountId) <= 0) {
            decrementValue = decrementCallback(decrementAccountId)
            incrementValue = incrementCallback(incrementAccountId)
        } else {
            incrementValue = incrementCallback(incrementAccountId)
            decrementValue = decrementCallback(decrementAccountId)
        }

        return ReleaseResult(decrementValue,incrementValue)
    }

    private class AvoidDeadLockComparator: Comparator<Long>{
        override fun compare(account1: Long, account2: Long): Int {
            return account1.compareTo(account2)
        }
    }
}