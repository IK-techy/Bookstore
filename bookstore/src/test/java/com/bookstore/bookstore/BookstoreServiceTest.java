package com.bookstore.bookstore;

import org.junit.jupiter.api.BeforeEach;

public class BookstoreServiceTest {

    private BookstoreService bookstoreService;

    @BeforeEach
    void setUp() {
        bookstoreService = new BookstoreService();
    }

}

// TODO unable to solve issues in testing class for now. Some issue with imports
// and running the tests in VS Code.
// Will not add unit tests.

// @Test
// void testPlaceOrderSuccess() {

// OrderRequest orderRequest = new OrderRequest(List.of(
// new BookOrder("A", 2),
// new BookOrder("B", 1)));

// OrderResponse response = bookstoreService.placeOrder(orderRequest);

// assertEquals("Success", response.message());
// assertEquals(20, response.totalPrice());
// assertEquals(98, bookstoreService.getStock().get("A").getStock());
// assertEquals(99, bookstoreService.getStock().get("B").getStock());
// }