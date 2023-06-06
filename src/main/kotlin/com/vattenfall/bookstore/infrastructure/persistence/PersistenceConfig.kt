package com.vattenfall.bookstore.infrastructure.persistence

import com.vattenfall.bookstore.domain.AuthorRepository
import com.vattenfall.bookstore.domain.BookstoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class PersistenceConfig {

    @Bean
    fun authorRepository(authorDao: AuthorDao): AuthorRepository = AuthorOrmRepository(authorDao)

    @Bean
    fun bookstoreRepository(bookDao: BookDao): BookstoreRepository = BookstoreOrmRepository(bookDao)
}