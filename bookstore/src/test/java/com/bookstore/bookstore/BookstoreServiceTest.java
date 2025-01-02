package com.bookstore.bookstore;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("null")
public class BookstoreServiceTest {

    private BookstoreService bookstoreService;

    @BeforeEach
    void setUp() {
        bookstoreService = new BookstoreService();
    }

    @Test
    void testPlaceOrderSuccess() {
        OrderRequest orderRequest = new OrderRequest(List.of(
                new BookOrder("A", 2),
                new BookOrder("B", 1)));

        ResponseEntity<OrderResponse> response = bookstoreService.placeOrder(orderRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(20, response.getBody().totalPrice());
        assertEquals(98, bookstoreService.getStock().get("A").getStock());
        assertEquals(99, bookstoreService.getStock().get("B").getStock());
    }

    @Test
    void testPlaceOrderExceedsMaxPrice() {
        List<BookOrder> orders = List.of(
                new BookOrder("C", 5),
                new BookOrder("D", 1));
        OrderRequest orderRequest = new OrderRequest(orders);

        ResponseEntity<OrderResponse> response = bookstoreService.placeOrder(orderRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Failure, order exceeds $120. Unable to proceed with the order", response.getBody().message());
    }

    @Test
    void testPlaceOrderInsufficientStock() {
        List<BookOrder> orders = List.of(
                new BookOrder("D", 11));
        OrderRequest orderRequest = new OrderRequest(orders);

        ResponseEntity<OrderResponse> response = bookstoreService.placeOrder(orderRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Failure, unable to place order", response.getBody().message());
    }

    @Test
    void testRestockSuccess() {

        final String USER = "Uncle_Bob_1337";
        final String PASSWORD = "TomCruiseIsUnder170cm";

        List<RestockOrder> restockItems = List.of(
                new RestockOrder("A", 10),
                new RestockOrder("B", 20));

        BookAdmin admin = new BookAdmin(USER,PASSWORD);
        RestockRequest restockRequest = new RestockRequest(admin, restockItems);

        ResponseEntity<RestockResponse> response = bookstoreService.restock(restockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Success: Restocked", response.getBody().message());
        assertEquals(110, bookstoreService.getStock().get("A").getStock());
        assertEquals(120, bookstoreService.getStock().get("B").getStock());
    }

    @Test
    void testRestockUnauthorized() {

        final String USER = "WrongUSername";
        final String PASSWORD = "WrongPassword";

        BookAdmin admin = new BookAdmin(USER,PASSWORD);

        List<RestockOrder> restockItems = List.of(
            new RestockOrder("A", 10)
        );
        RestockRequest restockRequest = new RestockRequest(admin, restockItems);

        ResponseEntity<RestockResponse> response = bookstoreService.restock(restockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Failure: Authentication failed", response.getBody().message());
    }

    @Test
    void testRestockInvalidBook() {
        final String USER = "Uncle_Bob_1337";
        final String PASSWORD = "TomCruiseIsUnder170cm";

        List<RestockOrder> restockItems = List.of(
                new RestockOrder("E", 10));

        BookAdmin admin = new BookAdmin(USER,PASSWORD);
        RestockRequest restockRequest = new RestockRequest(admin, restockItems);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookstoreService.restock(restockRequest);
        });

        assertEquals("Cannot restock book E", exception.getMessage());
    }

    @Test
    void testRestockLimitedEditionBook() {
        final String USER = "Uncle_Bob_1337";
        final String PASSWORD = "TomCruiseIsUnder170cm";

        List<RestockOrder> restockItems = List.of(
                new RestockOrder("D", 10));

        BookAdmin admin = new BookAdmin(USER,PASSWORD);
        RestockRequest restockRequest = new RestockRequest(admin, restockItems);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookstoreService.restock(restockRequest);
        });

        assertEquals("Cannot restock book D", exception.getMessage());
    }
}
