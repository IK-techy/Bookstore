package com.bookstore.bookstore;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/books")
    public Map<String, Book> getBooks() {
        return bookstoreService.getStock();
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest orderRequest) {
        return bookstoreService.placeOrder(orderRequest);
    }

    @PostMapping("/restock")
    public ResponseEntity<RestockResponse> restock(@RequestBody RestockRequest restockRequest) {
        return bookstoreService.restock(restockRequest);
    }

}
