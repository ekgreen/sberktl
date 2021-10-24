package ru.sber.anotherservices

import org.springframework.stereotype.Service

@Service("anotherFourthService")
class FourthService {
    override fun toString(): String {
        return "I am anotherFourthService"
    }
}