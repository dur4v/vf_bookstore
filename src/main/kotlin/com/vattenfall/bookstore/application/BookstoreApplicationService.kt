package com.vattenfall.bookstore.application

import com.vattenfall.bookstore.domain.Author
import com.vattenfall.bookstore.domain.AuthorRepository
import com.vattenfall.bookstore.domain.Book
import com.vattenfall.bookstore.domain.BookstoreRepository
import org.springframework.transaction.annotation.Transactional

open class BookstoreApplicationService(
    private val bookstoreRepository: BookstoreRepository,
    private val authorRepository: AuthorRepository
) {
    @Transactional(readOnly = true)
    open fun findBooks(): List<BookDto> = bookstoreRepository.findAll()
        .map { BookDto(it.isbn, it.title, it.authorId) }

    @Transactional
    open fun addBook(bookDto: BookDto) {
        val author: Author = authorRepository.findById(bookDto.authorId) ?: throw AuthorDoesNotExistsException()
        assertBookIsCorrect(bookDto)
        assertBookDoesNotExist(bookDto)

        val book = Book(bookDto.isbn, bookDto.title, author.id)
        bookstoreRepository.save(book)
    }

    private fun assertBookDoesNotExist(bookDto: BookDto) {
        if (bookstoreRepository.existsByIsbn(bookDto.isbn)) {
            throw InvalidBookDataException("Book already exists")
        }
    }

    private fun assertBookIsCorrect(bookDto: BookDto) {
        if (bookDto.isbn.length != 13) {
            throw InvalidBookDataException.forInvalidIsbn()
        }
        if (bookDto.title.isEmpty()) {
            throw InvalidBookDataException.forInvalidTitle()
        }
    }
}
