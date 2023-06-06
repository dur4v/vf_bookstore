package com.vattenfall.bookstore.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorDao : JpaRepository<AuthorEntity, Long> {
    fun findByAuthorId(authorId: Int): AuthorEntity?
}