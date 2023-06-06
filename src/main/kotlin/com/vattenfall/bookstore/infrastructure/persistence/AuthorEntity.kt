package com.vattenfall.bookstore.infrastructure.persistence

import com.vattenfall.bookstore.domain.Author
import jakarta.persistence.*

@Entity
class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var authorId = 0

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var surname: String = ""

    fun toDomainObject() = Author(authorId, name, surname)
}