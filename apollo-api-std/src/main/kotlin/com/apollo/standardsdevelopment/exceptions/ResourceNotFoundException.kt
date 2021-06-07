package com.apollo.standardsdevelopment.exceptions

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String?) : Exception(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}