package com.epam.weather.controller;

import com.epam.weather.response.ResultModel;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private RequestBuilder request;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
    }

    @Test
    public void getTemperature() throws Exception {
        request = get("/api/v1/weather/temperature")
                .param("province", "江苏")
                .param("city", "苏州")
                .param("county", "苏州")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Gson gson = new Gson();
        ResultModel<?> resultModel = gson.fromJson(content, ResultModel.class);
        Assertions.assertEquals("0", resultModel.getCode());
    }

    @Test
    public void getTemperature_exception() throws Exception {
        request = get("/api/v1/weather/temperature")
                .param("province", "江苏")
                .param("city", "苏州")
                .param("county", "suzhou")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Gson gson = new Gson();
        ResultModel<?> resultModel = gson.fromJson(content, ResultModel.class);
        Assertions.assertEquals("-1", resultModel.getCode());
    }
}