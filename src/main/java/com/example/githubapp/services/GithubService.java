package com.example.githubapp.services;

import com.example.githubapp.models.Branch;
import com.example.githubapp.models.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GithubService {

    private final RestTemplate restTemplate;

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Repository> getRepositories(String username) {

        ResponseEntity<Repository[]> responseEntity =
                restTemplate.getForEntity(
                        "https://api.github.com/users/" + username + "/repos",
                        Repository[].class);
        List<Repository> repositories = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        for (Repository repository : repositories) {
            if (!repository.isFork()) {
                ResponseEntity<Branch[]> branchResponseEntity =
                        restTemplate.getForEntity(
                                "https://api.github.com/repos/" +
                                        username +
                                        "/" +
                                        repository.getName() +
                                        "/branches",
                                Branch[].class);
                repository.setBranches(Arrays.asList(Objects.requireNonNull(branchResponseEntity.getBody())));
            }
        }

        return repositories;
    }

    public List<Repository> getRepositoriesNotFork(String username){
        return getRepositories(username)
                .stream()
                .filter(repository -> !repository.isFork())
                .toList();
    }
}
