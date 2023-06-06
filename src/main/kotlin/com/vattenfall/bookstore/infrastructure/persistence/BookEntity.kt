package com.vattenfall.bookstore.infrastructure.persistence

import com.vattenfall.bookstore.domain.Book
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId

@Entity
class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @NaturalId
    @Column(nullable = false)
    var isbn: String = ""

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var authorId: Int = 0

    fun toDomainObject(): Book = Book(isbn, title, authorId)
}