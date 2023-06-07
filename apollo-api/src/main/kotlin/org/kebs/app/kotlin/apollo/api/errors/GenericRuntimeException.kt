package org.kebs.app.kotlin.apollo.api.errors

class GenericRuntimeException(val status: Int, override val message: String) : RuntimeException()