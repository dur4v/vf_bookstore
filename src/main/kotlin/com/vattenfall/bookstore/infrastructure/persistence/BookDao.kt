package com.vattenfall.bookstore.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BookDao : JpaRepository<BookEntity, Long> {
    fun existsByIsbn(isbn: String): Boolean
    fun findAllByOrderByIsbn(): List<BookEntity>
}