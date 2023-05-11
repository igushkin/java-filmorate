package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getGroups() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void create() throws Exception {
        String content = "{ \"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"mail@mail.ru\", \"birthday\": \"1946-08-20\" }";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                ).andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void update() throws Exception {
        String content1 = "{ \"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"mail@mail.ru\", \"birthday\": \"1946-08-20\" }";
        String content2 = "{ \"login\": \"doloreUpdate\", \"name\": \"est adipisicing\", \"id\": 1, \"email\": \"mail@yandex.ru\", \"birthday\": \"1976-09-20\" }";

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(content1));

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content2))
                .andExpect(jsonPath("$.login").value("doloreUpdate"))
                .andExpect(jsonPath("$.name").value("est adipisicing"))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.birthday").value("1976-09-20"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createFailEmail() throws Exception {
        String content = "{ \"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"email.ru\", \"birthday\": \"1946-08-20\" }";

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }


    @Test
    void createFailLogin() throws Exception {
        String content = "{ \"login\": \"dolor e\", \"name\": \"Nick Name\", \"email\": \"mail@email.ru\", \"birthday\": \"1946-08-20\" }";

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void createFailBirthday() throws Exception {
        String content = "{ \"login\": \"dolore\", \"name\": \"Nick Name\", \"email\": \"mail@email.ru\", \"birthday\": \"2023-08-20\" }";

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void updateFailId() throws Exception {
        String content = "{ \"login\": \"doloreUpdate\", \"name\": \"est adipisicing\", \"id\": 100, \"email\": \"mail@yandex.ru\", \"birthday\": \"1976-09-20\" }";

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }
}