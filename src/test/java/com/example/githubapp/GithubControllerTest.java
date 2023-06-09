package com.example.githubapp;

import com.example.githubapp.services.GithubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubService githubService;

    @Test
    public void getRepositories_WithJsonHeader_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/repos/Yuliaskor")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(githubService, times(1)).getRepositoriesNotFork("Yuliaskor");
    }

    @Test
    public void getRepositories_WithXmlHeader_ReturnsNotAcceptable() throws Exception {
        mockMvc.perform(get("/api/repos/Yuliaskor")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());

        verify(githubService, never()).getRepositories("Yuliaskor");
    }

    @Test
    public void getRepositories_WithNonexistentUser_ReturnsNotFound() throws Exception {
        when(githubService.getRepositoriesNotFork("Yuliaskorbach")).thenThrow(HttpClientErrorException.NotFound.class);

        mockMvc.perform(get("/api/repos/Yuliaskorbach")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(githubService, times(1)).getRepositoriesNotFork("Yuliaskorbach");
    }
}
