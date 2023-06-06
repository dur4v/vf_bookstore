package com.vattenfall.bookstore.domain

interface BookstoreRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
    fun existsByIsbn(isbn: String): Boolean
}
