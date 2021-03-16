package com.prosegur.ws.rest;

import com.prosegur.integration.ExternalService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class GeneralControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ExternalService externalService;

    @Test
    public void testAemet() throws Exception {
        Pair<Integer, Object> aemetResult = Pair.of(200, "OK");
        when(externalService.sendRequest(any(String.class),any(String.class))).thenReturn(aemetResult);
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/aemet").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testMyService() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/myService").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/check").accept(MediaType.APPLICATION_JSON).param("val", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkFail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/check").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkFail2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/check").accept(MediaType.APPLICATION_JSON).param("val", "test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void notFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/other").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkLocation() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/general/example").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("controlador_test"));
    }

}
