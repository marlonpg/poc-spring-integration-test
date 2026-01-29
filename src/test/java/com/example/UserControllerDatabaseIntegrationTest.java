package com.example;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
public class UserControllerDatabaseIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        userRepository.deleteAll();
    }

    @Test
    public void givenUser_whenCreateUser_thenUserIsSavedInDatabase() throws Exception {
        User user = new User("John Doe", "john@example.com");
        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.id").exists());

        // Verify user is saved in database
        assertEquals(1, userRepository.count());
        User savedUser = userRepository.findByName("John Doe");
        assertNotNull(savedUser);
        assertEquals("john@example.com", savedUser.getEmail());
    }

    @Test
    public void givenUserInDatabase_whenGetUserById_thenReturnUser() throws Exception {
        // Save user to database
        User user = new User("Jane Smith", "jane@example.com");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", savedUser.getId()))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void givenUsersInDatabase_whenGetAllUsers_thenReturnAllUsers() throws Exception {
        // Save multiple users to database
        userRepository.save(new User("User1", "user1@example.com"));
        userRepository.save(new User("User2", "user2@example.com"));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[1].name").value("User2"));

        // Verify database state
        assertEquals(2, userRepository.count());
    }

    @Test
    public void givenUserInDatabase_whenGetUserByName_thenReturnUser() throws Exception {
        // Save user to database
        User user = new User("Alice Johnson", "alice@example.com");
        userRepository.save(user);

        mockMvc.perform(get("/users/name/{name}", "Alice Johnson"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    public void givenNonExistentUserId_whenGetUserById_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNonExistentUserName_whenGetUserByName_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/name/{name}", "NonExistent"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}