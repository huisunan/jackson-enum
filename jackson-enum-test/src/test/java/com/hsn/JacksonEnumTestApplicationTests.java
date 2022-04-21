package com.hsn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JacksonEnumTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("get param test")
    void testGetIn() throws Exception {
        mockMvc.perform(get("/").param("genderEnum", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(GenderEnum.MALE.getDesc()));

    }

    @Test
    @DisplayName("post request body test")
    void testPostRequestBody() throws Exception {
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"genderEnum\": 2}")
        ).andExpect(status().isOk()).andExpect(content().string(GenderEnum.FEMALE.getDesc()));
    }

    @Test
    @DisplayName("post request body str to int test")
    void testPostRequestBodyStr2Int() throws Exception {
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"genderEnum\": \"2\"}")
        ).andExpect(status().isOk()).andExpect(content().string(GenderEnum.FEMALE.getDesc()));
    }

    @Test
    @DisplayName("test return")
    void testReturn() throws Exception {
        mockMvc.perform(get("/return"))
                .andExpect(content().json("{\"genderEnum\":1}"));
    }


    @Test
    @DisplayName("test extend")
    void testExtend() throws Exception {
        mockMvc.perform(post("/extend")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"genderEnum\": 2}")
        ).andExpect(status().isOk()).andExpect(content().string(GenderEnum.FEMALE.getDesc()));
    }

    @Test
    @DisplayName("get param str test")
    void testStrGetIn() throws Exception {
        mockMvc.perform(get("/str").param("strEnum", "a"))
                .andExpect(status().isOk())
                .andExpect(content().string(StrEnum.A.getDesc()));

    }

}
