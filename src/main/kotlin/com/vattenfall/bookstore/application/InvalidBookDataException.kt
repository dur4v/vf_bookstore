package com.vattenfall.bookstore.application

class InvalidBookDataException(message: String) : Exception(message) {
    companion object {
        fun forInvalidIsbn(): InvalidBookDataException {
            return InvalidBookDataException("Isbn must have 13 signs")
        }

        fun forInvalidTitle(): InvalidBookDataException {
            return InvalidBookDataException("Title must have at least one sign")
        }
    }
}
