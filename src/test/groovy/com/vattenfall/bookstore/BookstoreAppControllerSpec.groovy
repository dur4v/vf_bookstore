package com.vattenfall.bookstore

import com.vattenfall.bookstore.application.AuthorDoesNotExistsException
import com.vattenfall.bookstore.application.BookDto
import com.vattenfall.bookstore.application.BookstoreApplicationService
import com.vattenfall.bookstore.application.InvalidBookDataException
import org.hamcrest.Matchers
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static com.vattenfall.bookstore.BookstoreAppSpecHelper.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest
class BookstoreAppControllerSpec extends Specification {

    @SpringBean
    BookstoreApplicationService serviceMock = Mock()

    @Autowired
    private MockMvc mvc

    def "Should successfully respond with empty book list"() {
        given:
        1 * serviceMock.findBooks() >> []

        expect: "Status is 200 and the response is empty list"
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_BOOKS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$", Matchers.hasSize(0)))
    }

    def "Should successfully respond with book list"() {
        given:
        1 * serviceMock.findBooks() >> [new BookDto(VALID_ISBN, VALID_BOOK_TITLE, AUTHOR_ID),
                                        new BookDto(OTHER_VALID_ISBN, OTHER_VALID_BOOK_TITLE, AUTHOR_ID)]

        expect: "Status is 200 and the response is empty list"
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_BOOKS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$", Matchers.hasSize(2)))
                .andExpect(jsonPath("\$[0].isbn", Matchers.equalToIgnoringCase(VALID_ISBN)))
                .andExpect(jsonPath("\$[0].title", Matchers.equalToIgnoringCase(VALID_BOOK_TITLE)))
                .andExpect(jsonPath("\$[0].authorId", Matchers.equalTo(AUTHOR_ID)))
                .andExpect(jsonPath("\$[1].isbn", Matchers.equalToIgnoringCase(OTHER_VALID_ISBN)))
                .andExpect(jsonPath("\$[1].title", Matchers.equalToIgnoringCase(OTHER_VALID_BOOK_TITLE)))
                .andExpect(jsonPath("\$[1].authorId", Matchers.equalTo(AUTHOR_ID)))
    }

    def "Should successfully respond when new book is added"() {
        given:
        1 * serviceMock.addBook(new BookDto(VALID_ISBN, VALID_BOOK_TITLE, AUTHOR_ID))

        expect: "Status is 200"
        postNewBook().andExpect(status().isOk())
    }

    def "Should fail with BadRequest response when input is invalid"() {
        expect: "Status is 400"
        postNewBook(input).andExpect(status().isBadRequest())

        where:
        input                                                         | _
        """{"title":" $VALID_BOOK_TITLE ","authorId": $AUTHOR_ID }""" | _
        """{"isbn":"$VALID_ISBN","authorId": $AUTHOR_ID }"""          | _
        """invalid input"""                                           | _
    }

    def "Should fail with proper error when book cannot be added"() {
        given:
        1 * serviceMock.addBook(_) >> { throw serviceMockException }

        expect: "Proper status"
        postNewBook().andExpect(status().is(expectedHttpStatus))

        where:
        serviceMockException                    | expectedHttpStatus
        new Exception()                         | 500
        new InvalidBookDataException("message") | 400
        new AuthorDoesNotExistsException()      | 400

    }

    def postNewBook(def content = """
                    {"isbn":"$VALID_ISBN",
                    "title":"$VALID_BOOK_TITLE",
                    "authorId":$AUTHOR_ID}
                """) {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_BOOKS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
    }
}