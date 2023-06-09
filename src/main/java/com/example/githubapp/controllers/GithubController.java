package com.example.githubapp.controllers;
import com.example.githubapp.exeption.ErrorResponse;
import com.example.githubapp.models.Branch;
import com.example.githubapp.models.Repository;
import com.example.githubapp.services.GithubService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/repos/{username}")
    public ResponseEntity<?> getRepositories(@PathVariable String username, @RequestHeader("Accept") String acceptHeader) {

        if (!acceptHeader.equalsIgnoreCase("application/json")) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid Accept Header. Only application/json is supported.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            List<Repository> repositories = githubService.getRepositoriesNotFork(username);
            return new ResponseEntity<>(repositories, HttpStatus.OK);
        } catch (HttpClientErrorException.NotFound ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
