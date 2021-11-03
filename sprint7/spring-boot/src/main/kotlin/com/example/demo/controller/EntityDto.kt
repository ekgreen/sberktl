package com.example.demo.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class EntityDto @JsonCreator constructor(
    @JsonProperty("id")     var id: Long?,
    @JsonProperty("amount") var amount: Long?
)