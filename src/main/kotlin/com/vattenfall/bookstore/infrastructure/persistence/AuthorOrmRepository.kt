package com.vattenfall.bookstore.infrastructure.persistence

import com.vattenfall.bookstore.domain.Author
import com.vattenfall.bookstore.domain.AuthorRepository

class AuthorOrmRepository(private val authorDao: AuthorDao) : AuthorRepository {

    override fun findById(authorId: Int): Author? =
        authorDao.findByAuthorId(authorId)?.toDomainObject()

    override fun save(author: Author) {
        AuthorEntity()
            .apply {
                authorId = author.id
                name = author.name
                surname = author.surname
            }
            .also { authorDao.save(it) }
    }
}