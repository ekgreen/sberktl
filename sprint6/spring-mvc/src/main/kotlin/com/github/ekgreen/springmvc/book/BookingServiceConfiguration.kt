package com.github.ekgreen.springmvc.book

import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.book.da.BookingRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class BookingServiceConfiguration {

    @Bean
    fun bookingRepository(): BookingRepository {
        return BookingRepository()
    }

    @Bean
    fun bookingService(repository: BookingRepository): BookingService {
        return BookingService(repository = repository)
    }

}