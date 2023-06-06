package com.vattenfall.bookstore.infrastructure.endpoint

import com.vattenfall.bookstore.application.AuthorDoesNotExistsException
import com.vattenfall.bookstore.application.BookDto
import com.vattenfall.bookstore.application.BookstoreApplicationService
import com.vattenfall.bookstore.application.InvalidBookDataException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookstoreRestController(private val bookstoreApplicationService: BookstoreApplicationService) {

    @GetMapping
    fun getBooks(): List<BookDto> = bookstoreApplicationService.findBooks()

    @PostMapping
    fun postBook(@RequestBody bookDto: BookDto): ResponseEntity<Unit> =
        try {
            bookstoreApplicationService.addBook(bookDto)
            ResponseEntity.ok().build()
        } catch (e: AuthorDoesNotExistsException) {
            ResponseEntity.badRequest().build()
        } catch (e: InvalidBookDataException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
}
