package com.github.breadmoirai.exception

import okhttp3.Response
import java.io.IOException

class IllegalNetworkResponseCodeException(
        response: Response
) : IOException("Network error ${response.code()}: ${response.message()}")
