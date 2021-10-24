package ru.sber.services

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
class SingletonService {
    override fun toString(): String {
        return "I am singletonService"
    }
}

@Service
@Scope("prototype")
class PrototypeService {
    override fun toString(): String {
        return "I am prototypeService"
    }
}
