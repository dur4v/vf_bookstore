package com.vattenfall.bookstore.domain

interface AuthorRepository {
    fun findById(authorId: Int): Author?
    fun save(author: Author)
}
