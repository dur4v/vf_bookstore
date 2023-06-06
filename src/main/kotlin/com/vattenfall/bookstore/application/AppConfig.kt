package com.vattenfall.bookstore.application

import com.vattenfall.bookstore.domain.AuthorRepository
import com.vattenfall.bookstore.domain.BookstoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class AppConfig {

    @Bean
    fun bookstoreApplicationService(
        bookstoreRepository: BookstoreRepository,
        authorRepository: AuthorRepository
    ): BookstoreApplicationService = BookstoreApplicationService(bookstoreRepository, authorRepository)
}
