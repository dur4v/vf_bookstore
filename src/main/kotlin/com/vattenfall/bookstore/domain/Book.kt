package com.vattenfall.bookstore.domain

data class Book(
    val isbn: String,
    val title: String,
    val authorId: Int
)
