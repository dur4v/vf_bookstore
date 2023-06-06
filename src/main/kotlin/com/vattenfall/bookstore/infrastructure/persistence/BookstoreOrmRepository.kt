package com.vattenfall.bookstore.infrastructure.persistence

import com.vattenfall.bookstore.domain.Book
import com.vattenfall.bookstore.domain.BookstoreRepository

class BookstoreOrmRepository(
    private val bookDao: BookDao
) : BookstoreRepository {

    override fun save(book: Book) {
        BookEntity()
            .apply {
                isbn = book.isbn
                title = book.title
                authorId = book.authorId
            }
            .also { bookDao.save(it) }
    }

    override fun findAll(): List<Book> =
        bookDao.findAllByOrderByIsbn()
            .map(BookEntity::toDomainObject)

    override fun existsByIsbn(isbn: String): Boolean = bookDao.existsByIsbn(isbn)
}