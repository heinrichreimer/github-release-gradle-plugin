package com.github.breadmoirai

class RepositoryNotFoundException(
        owner: String,
        repo: String
) : IllegalArgumentException("Repository '$owner/$repo' was not found.")
