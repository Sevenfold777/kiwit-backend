package com.kiwit.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = HealthCheckController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class HealthCheckControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @Test
    @DisplayName("Test Health Check - Response 200")
    public void testHealthCheckResponse() throws Exception {
        mockMvc.perform(get("/api/v1/healthCheck"))
                .andExpect(status().isOk());
    }
}
