package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class FilmControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getGroups() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void create() throws Exception {
        String content = "{ \"name\": \"name\", \"description\": \"description\", \"releaseDate\": \"1967-03-25\", \"duration\": 100, \"mpa\": { \"id\": 1} }";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                ).andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.releaseDate").value("1967-03-25"))
                .andExpect(jsonPath("$.duration").value(100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void update() throws Exception {
        String content1 = "{ \"name\": \"name\", \"description\": \"description\", \"releaseDate\": \"1967-03-25\", \"duration\": 100, \"mpa\": { \"id\": 1} }";
        String content2 = "{ \"id\": 1, \"name\": \"Film Updated\", \"releaseDate\": \"1989-04-17\", \"description\": \"New film update decription\", \"duration\": 190, \"mpa\": { \"id\": 1}}";

        mvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(content1));

        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content2))
                .andExpect(jsonPath("$.name").value("Film Updated"))
                .andExpect(jsonPath("$.description").value("New film update decription"))
                .andExpect(jsonPath("$.releaseDate").value("1989-04-17"))
                .andExpect(jsonPath("$.duration").value(190))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createFailName() throws Exception {
        String content = "{ \"name\": \"\", \"description\": \"description\", \"releaseDate\": \"1967-03-25\", \"duration\": 100, \"mpa\": { \"id\": 1} }";

        mvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void createFailDescription() throws Exception {
        String content = "{ \"name\": \"name\", \"description\": \"asdasASddescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiona13Asd19823asdasi.hADSa\", \"releaseDate\": \"1967-03-25\", \"duration\": 100, \"mpa\": { \"id\": 1} }";

        mvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void createFailReleaseDate() throws Exception {
        String content = "{ \"name\": \"name\", \"description\": \"description\", \"releaseDate\": \"1895-12-27\", \"duration\": 100, \"mpa\": { \"id\": 1} }";

        mvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void createFailDuration() throws Exception {
        String content = "{ \"name\": \"name\", \"description\": \"description\", \"releaseDate\": \"1967-03-25\", \"duration\": -1, \"mpa\": { \"id\": 1} }";

        mvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void updateFailId() throws Exception {
        String content = "{ \"id\": 100, \"name\": \"Film Updated\", \"releaseDate\": \"1989-04-17\", \"description\": \"New film update decription\", \"duration\": 190, \"mpa\": { \"id\": 1}}";

        mvc.perform(put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().is4xxClientError());
    }
}