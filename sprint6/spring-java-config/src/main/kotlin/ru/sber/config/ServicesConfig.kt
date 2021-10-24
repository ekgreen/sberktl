package ru.sber.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.sber.services.FirstService
import ru.sber.services.SecondService
import ru.sber.services.ThirdService

@Configuration
@ComponentScan("ru.sber.services")
class ServicesConfig {
    @Bean
    fun firstService(): FirstService {
        return FirstService()
    }

    @Bean
    fun secondService(): SecondService {
        return SecondService()
    }

    @Bean("thirdService")
    fun someService(): ThirdService {
        return ThirdService()
    }
}

@Configuration
@ComponentScan("ru.sber")
class AnotherServicesConfig