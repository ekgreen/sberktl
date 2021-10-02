package com.github.ekgreen.exception

class ValidationException(val errorCode: Array<ErrorCode>) : RuntimeException(errorCode.joinToString(",") { it.msg })

enum class ErrorType(val description: String){
    REQUIRED("Объект не может быть null"),
    REGEXP("Объект не удовлетворяет шаблону/регулярному выражению"),
    CONTROL_SUM("Контрольная сумма не совпадает")
}

enum class ErrorCode(val type: ErrorType, val code: Int, val msg: String) {
    CLIENT_IS_REQUIRED      (ErrorType.REQUIRED, 100, "Клиент не может быть null"),
    LAST_NAME_IS_REQUIRED   (ErrorType.REQUIRED, 200, "Фамилия клиента не может быть null"),
    FIRST_NAME_IS_REQUIRED  (ErrorType.REQUIRED, 300, "Имя клиента не может быть null"),
    PHONE_NUMBER_IS_REQUIRED(ErrorType.REQUIRED, 400, "Номер телефона не может быть null"),
    EMAIL_IS_REQUIRED       (ErrorType.REQUIRED, 500, "E-mail не может быть null"),
    SNILS_IS_REQUIRED       (ErrorType.REQUIRED, 600, "СНИЛС не может быть null"),

    LAST_NAME_OUT_OF_RANGE(ErrorType.REGEXP, 201, "Фамилия содержит не более 16 символов в длину"),
    NOT_CYRILLIC_LAST_NAME(ErrorType.REGEXP, 202, "Фамилия может состоять только из кириллицы"),

    FIRST_NAME_OUT_OF_RANGE(ErrorType.REGEXP, 301, "Фамилия содержит не более 16 символов в длину"),
    NOT_CYRILLIC_FIRST_NAME(ErrorType.REGEXP, 302, "Фамилия может состоять только из кириллицы"),

    MOBILE_NUMBER_OUT_OF_RANGE(ErrorType.REGEXP,401, "Телефон должен быть не более 11 символов в длину"),
    NOT_DIGITAL_MOBILE_NUMBER(ErrorType.REGEXP,402, "Телефон должен состоять только из цифр"),
    NOT_RU_MOBILE_NUMBER(ErrorType.REGEXP,403, "Телефон должен начинаться с 7 или 8"),

    EMAIL_OUT_OF_RANGE(ErrorType.REGEXP, 501, "E-mail должен быть не более 32 символов в длину"),
    EMAIL_NOT_VALID_REGEXP(ErrorType.REGEXP,502, "E-mail должен состоять исключительно из латинских букв, цифр и символов [!#\\$%&'*+/=?^_`{|}~-]"),

    SNILS_OUT_OF_RANGE(ErrorType.REGEXP, 601,"СНИЛС должен состоять из 11 цифр"),
    NOT_DIGITAL_SNILS(ErrorType.REGEXP,602, "СНИЛС должен состоять только из цифр"),
    SNILS_CONTROL_SUM_NOT_MATCHED(ErrorType.CONTROL_SUM, 603, "Контрольные суммы СНИЛС не совпадают")
}