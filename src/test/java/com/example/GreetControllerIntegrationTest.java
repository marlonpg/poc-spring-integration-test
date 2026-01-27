package com.example;

import com.example.config.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@WebAppConfiguration
public class GreetControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("greetController"));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsIndexJSPViewName() throws Exception {
        this.mockMvc.perform(get("/homePage")).andDo(print())
          .andExpect(view().name("index"));
    }

    @Test
    public void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/greet"))
          .andDo(print()).andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Hello World!!!"))
          .andReturn();
        
        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenGreetURIWithPathVariable_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc
          .perform(get("/greetWithPathVariable/{name}", "John"))
          .andDo(print()).andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.message").value("Hello World John!!!"));
    }

    @Test
    public void givenGreetURIWithQueryParameter_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc.perform(get("/greetWithQueryVariable")
          .param("name", "John Doe")).andDo(print()).andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.message").value("Hello World John Doe!!!"));
    }

    @Test
    public void givenGreetURIWithPost_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post("/greetWithPost")).andDo(print())
          .andExpect(status().isOk()).andExpect(content()
          .contentType("application/json"))
          .andExpect(jsonPath("$.message").value("Hello World!!!"));
    }

    @Test
    public void givenGreetURIWithPostAndFormData_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post("/greetWithPostAndFormData")
          .param("id", "1")
          .param("name", "John Doe"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.message").value("Hello World John Doe!!!"));
    }
}