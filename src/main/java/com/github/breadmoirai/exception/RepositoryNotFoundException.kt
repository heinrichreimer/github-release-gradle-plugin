package com.github.breadmoirai.exception

class RepositoryNotFoundException(
        owner: String,
        repo: String
) : IllegalArgumentException("Repository '$owner/$repo' was not found.")
