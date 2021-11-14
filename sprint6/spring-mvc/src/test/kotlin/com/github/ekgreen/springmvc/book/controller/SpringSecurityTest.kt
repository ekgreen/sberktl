package com.github.ekgreen.springmvc.book.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = ["AUTH_SERVICE_SECRET=elephant_on_the_street_playing_with_boy"])
internal class SpringSecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "Wall-E", password = "eva", authorities = ["ROLE_ADMIN"])
    fun `admin have access to create contact`() {
        mockMvc.perform(
            get("/app/v1/book/add")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
    }

    @Test
    @WithMockUser(username = "Eva", password = "walle", authorities = ["ROLE_APP_OWNER"])
    fun `eva have not access to create contact`() {
        mockMvc.perform(
            get("/app/v1/book/add")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "Wall-E", password = "eva", authorities = ["ROLE_API_OWNER"])
    fun `walle have access to api`() {
        mockMvc.perform(
            get("/api/v1/book/view/0")
                .with(csrf())
        )
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(username = "Eva", password = "walle", authorities = ["ROLE_APP_OWNER"])
    fun `eva have not access to view by api`() {
        mockMvc.perform(
            get("/api/v1/book/view/0")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "Eva", password = "walle", authorities = ["ROLE_APP_OWNER"])
    fun `eva have access to view by app`() {
        mockMvc.perform(
            get("/app/v1/book/view/0")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
    }

    @Test
    @WithMockUser(username = "Alien", password = "walle", authorities = [])
    fun `alien have not access to any page`() {
        mockMvc.perform(
            get("/app/v1/book/view/0")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
    }
}