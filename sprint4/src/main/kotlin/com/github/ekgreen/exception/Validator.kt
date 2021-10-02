package com.github.ekgreen.exception

import java.util.regex.Pattern

abstract class Validator<T> {
    abstract fun validate(value: T?): List<ErrorCode>
}

object ClientValidator : Validator<Client>() {

    override fun validate(value: Client?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null){
            codes.add(ErrorCode.CLIENT_IS_REQUIRED)
            return codes
        }

        codes.addAll(LastNameValidator.validate(value.lastName))
        codes.addAll(FirstNameValidator.validate(value.firstName))
        codes.addAll(PhoneValidator.validate(value.phone))
        codes.addAll(EmailValidator.validate(value.email))
        codes.addAll(SNILSValidator.validate(value.snils))

        return codes
    }

}

object PhoneValidator : Validator<String>() {

    private const val PHONE_NUMBER_LENGTH: Int = 11

    override fun validate(value: String?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null || value.isEmpty()){
            codes.add(ErrorCode.PHONE_NUMBER_IS_REQUIRED)
            return codes
        }

        if(value.length != PHONE_NUMBER_LENGTH)
            codes.add(ErrorCode.MOBILE_NUMBER_OUT_OF_RANGE)

        if(!(value.startsWith('7') || value.startsWith('8')))
            codes.add(ErrorCode.NOT_RU_MOBILE_NUMBER)

        if(!value.all { it in '0'..'9' })
            codes.add(ErrorCode.NOT_DIGITAL_MOBILE_NUMBER)

        return codes
    }
}

object EmailValidator : Validator<String>() {

    private const val EMAIL_MAX_LENGTH: Int = 32

    /**
     * Регулярное выражение для e-mail [RFC 5322, немного урезанный - для латиницы]
     * PS не очень понял, что имеется ввиду под валидацией домена (если на какие-то конкретные, то можно переделать)
     *
     * @see <a href="https://i.stack.imgur.com/YI6KR.png">RFC 5322</a>
     */
    private val EMAIL_REGEXP: Pattern =
        Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")


    override fun validate(value: String?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null || value.isEmpty()){
            codes.add(ErrorCode.EMAIL_IS_REQUIRED)
            return codes
        }

        if(value.length > EMAIL_MAX_LENGTH)
            codes.add(ErrorCode.EMAIL_OUT_OF_RANGE)

        if(!EMAIL_REGEXP.matcher(value).matches())
            codes.add(ErrorCode.EMAIL_NOT_VALID_REGEXP)

        return codes
    }

}

object SNILSValidator: Validator<String>() {

    private const val SNILS_LENGTH: Int = 11

    override fun validate(value: String?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null || value.isEmpty()){
            codes.add(ErrorCode.SNILS_IS_REQUIRED)
            return codes
        }

        if(value.length != SNILS_LENGTH)
            codes.add(ErrorCode.SNILS_OUT_OF_RANGE)

        if(!value.all { it in '0'..'9' }) {
            codes.add(ErrorCode.NOT_DIGITAL_SNILS)
            return codes // если будем делать проверку контрольных сум, а значения не цифры, то все взорвется
        }

        if(!checkControlSum(value.chars().map{v -> v - 48}.toArray()))
            codes.add(ErrorCode.SNILS_CONTROL_SUM_NOT_MATCHED)

        return codes
    }

    private fun checkControlSum(numbers: IntArray): Boolean{
        var sum: Int = 0
        for (i in 9 downTo 1)
            sum += numbers[9 - i] * i

        val controlSum: Int = calculateControlSum(sum)

        return controlSum == (numbers[9] * 10 + numbers[10])
    }

    private fun calculateControlSum(sum: Int): Int {
        if(sum < 100)
            return sum

        if(sum == 100)
            return 0

        return calculateControlSum(sum % 101)
    }
}

object LastNameValidator: Validator<String>() {

    private const val LAST_NAME_MAX_LENGTH: Int = 16

    override fun validate(value: String?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null || value.isEmpty()){
            codes.add(ErrorCode.LAST_NAME_IS_REQUIRED)
            return codes
        }

        if(value.length > LAST_NAME_MAX_LENGTH)
            codes.add(ErrorCode.LAST_NAME_OUT_OF_RANGE)

        if(!value.chars().allMatch { it in 1040..1103 })
            codes.add(ErrorCode.NOT_CYRILLIC_LAST_NAME)

        return codes
    }
}

object FirstNameValidator: Validator<String>() {

    private const val FIRST_NAME_MAX_LENGTH: Int = 16

    override fun validate(value: String?): List<ErrorCode> {
        val codes: MutableList<ErrorCode> = ArrayList();

        if(value == null || value.isEmpty()){
            codes.add(ErrorCode.FIRST_NAME_IS_REQUIRED)
            return codes
        }

        if(value.length > FIRST_NAME_MAX_LENGTH)
            codes.add(ErrorCode.FIRST_NAME_OUT_OF_RANGE)

        if(!value.chars().allMatch { it in 1040..1103 })
            codes.add(ErrorCode.NOT_CYRILLIC_FIRST_NAME)

        return codes
    }
}