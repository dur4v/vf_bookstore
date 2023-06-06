package com.vattenfall.bookstore

import com.vattenfall.bookstore.application.AppConfig
import com.vattenfall.bookstore.application.AuthorDoesNotExistsException
import com.vattenfall.bookstore.application.BookDto
import com.vattenfall.bookstore.application.BookstoreApplicationService
import com.vattenfall.bookstore.application.InvalidBookDataException
import com.vattenfall.bookstore.domain.Author
import com.vattenfall.bookstore.domain.AuthorRepository
import com.vattenfall.bookstore.infrastructure.persistence.PersistenceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import static com.vattenfall.bookstore.BookstoreAppSpecHelper.*

@Import([AppConfig.class, PersistenceConfig.class])
@DataJpaTest
class BookstoreAppBookProcessingSpec extends Specification {

    @Autowired
    BookstoreApplicationService service

    @Autowired
    AuthorRepository authorRepository

    def "Should successfully add a new book for known author"() {
        given: "book author already exists"
        createTestAuthor()

        and: "new book is created"
        def bookDto = createTestBookDto(VALID_ISBN, VALID_BOOK_TITLE)

        when: "book is added"
        service.addBook(bookDto)

        then: "book is available in list"
        with(service.findBooks()) {
            it.size() == 1
            it.get(0) == bookDto
        }
    }

    def "Should successfully return empty list of books"() {
        expect:
        service.findBooks().isEmpty()
    }

    def "Should successfully return list of available books"() {
        given: "book author already exists"
        createTestAuthor()

        and: "new different books are added"
        def bookDto1 = createTestBookDto(VALID_ISBN, VALID_BOOK_TITLE)
        service.addBook(bookDto1)

        def bookDto2 = createTestBookDto(OTHER_VALID_ISBN, OTHER_VALID_BOOK_TITLE)
        service.addBook(bookDto2)

        when: "book are listed"
        def bookList = service.findBooks()

        then: "all books are available"
        with(bookList) {
            it.size() == 2
            it.contains(bookDto1)
            it.contains(bookDto2)
        }
    }

    def "Should fail with exception when adding book for not existing author"() {
        given: "a book for not existing author"
        def book = createTestBookDto(VALID_ISBN, VALID_BOOK_TITLE)

        when: "book is added"
        service.addBook(book)

        then: "exception is thrown"
        thrown(AuthorDoesNotExistsException)
    }

    def "Should fail with exception when adding book that already exists"() {
        given: "book author already exists"
        createTestAuthor()

        and: "the book is already added"
        def book = createTestBookDto(VALID_ISBN, VALID_BOOK_TITLE)
        service.addBook(book)

        when: "the same book is added again"
        service.addBook(book)

        then: "exception is thrown"
        def exception = thrown(InvalidBookDataException)
        with(exception) {
            it.getMessage() == "Book already exists"
        }
    }

    def "Should fail with exception when adding book with invalid ISBN"() {
        given: "book author already exists"
        createTestAuthor()

        when: "a book with invalid ISBN is added"
        service.addBook(createTestBookDto(invalidISBN, VALID_BOOK_TITLE))

        then: "exception is thrown"
        def exception = thrown(InvalidBookDataException)
        with(exception) {
            it.getMessage() == "Isbn must have 13 signs"
        }

        where:
        invalidISBN << ["", "01234567890", "01234567891234"]
    }

    def "Should fail with exception when adding book with empty title"() {
        given: "book author already exists"
        createTestAuthor()

        when: "a book is added"
        service.addBook(createTestBookDto(VALID_ISBN, invalidTitle))

        then: "exception is thrown"
        def exception = thrown(InvalidBookDataException)
        with(exception) {
            it.getMessage() == "Title must have at least one sign"
        }

        where:
        invalidTitle << [""]
    }

    def createTestAuthor(def id = AUTHOR_ID, def name = AUTHOR_NAME, def surname = AUTHOR_SURNAME) {
        authorRepository.save(new Author(id, name, surname))
        id
    }

    def createTestBookDto(def author = AUTHOR_ID, def isbn, def title) {
        return new BookDto(isbn, title, author)
    }
}
