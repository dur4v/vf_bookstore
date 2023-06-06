package com.vattenfall.bookstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookstoreAppContextSpec extends Specification {

    @Autowired
    WebTestClient client

    def "Should rise up the app context and respond"() {
        expect:
        client.get().uri("/books").exchange().expectStatus().isOk()
    }
}