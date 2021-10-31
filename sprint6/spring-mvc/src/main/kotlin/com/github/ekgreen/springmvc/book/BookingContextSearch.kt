package com.github.ekgreen.springmvc.book

data class BookingContextSearch(
    var lastName: String? = null,
    var firstName: String? = null,
    var location: String? = null
){
    fun setLastName(lastName: String?): BookingContextSearch{
        this.lastName = lastName
        return this
    }
    fun setFirstName(firstName: String?): BookingContextSearch{
        this.firstName = firstName
        return this
    }
    fun setLocation(location: String?): BookingContextSearch{
        this.location = location
        return this
    }
}
