package ru.sber.services

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

class FirstService {
    override fun toString(): String {
        return "I am firstService"
    }
}

class SecondService {
    override fun toString(): String {
        return "I am secondService"
    }
}

@Component
class ThirdService {
    override fun toString(): String {
        return "I am thirdService"
    }
}

@Service("fourthService")
class FourthService {
    override fun toString(): String {
        return "I am fourthService"
    }
}