# Github Repositories API Service

This is a simple API service that interacts with the GitHub API to provide information about user repositories.

## Features

- List all non-fork repositories of a specific user.
- For each repository, retrieve the name and last commit SHA of each branch.
- Provides appropriate HTTP responses based on the input.

## Requirements

- Java 17
- Maven

## Running the Application

1. Clone this repository
2. Navigate to the project directory in your terminal
3. Run `mvn clean install` to build the application
4. Start the application with `mvn spring-boot:run`

## Endpoints

The application has one endpoint:

`GET /api/repos/{username}`

This endpoint retrieves all non-fork repositories of the user specified in the `{username}` path variable. The endpoint expects an `Accept: application/json` header. If this header is not present, or if it's set to a different value, the API will respond with a 406 error.

The JSON response for each repository will include the repository name, owner login, and for each branch its name and last commit SHA.
```
[
   {
        "name": "name",
        "owner": {
            "login": "login"
        },
        "fork": false,
        "branches": [
            {
                "name": "name",
                "commit": {
                    "sha": "sha"
                }
            }
        ]
    }
]
```

If a non-existing GitHub username is given, the API will respond with a 404 error.


## Error Responses

Error responses are given in the following format:

```
{
    "status": responseCode,
    "message": "whyHasItHappened"
}
```

For example, if the `Accept` header is not `application/json`, you will receive:

```
{
    "status": 406,
    "message": "Invalid Accept Header. Only application/json is supported."
}
```

## Tests

Run `mvn test` in your terminal to execute the unit tests.

