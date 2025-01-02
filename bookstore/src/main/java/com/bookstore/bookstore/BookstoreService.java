package com.bookstore.bookstore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final Integer MAXPURCHASEPRICE = 120;
    private final Map<String, Book> stock = new HashMap<>();

    {
        stock.put("A", new Book("Fellowship of the book", 5, 100));
        stock.put("B", new Book("Books and the chamber of books", 10, 100));
        stock.put("C", new Book("The Return of the Book", 15, 100));
        stock.put("D", new Book("Limited Collectors Edition", 75, 10));
    }

    public ResponseEntity<OrderResponse> placeOrder(OrderRequest order) {

        int totalOrderCost = 0;

        for (BookOrder bookOrder : order.listOfBooks()) {

            Book book = stock.get(bookOrder.key());

            if (book == null || book.getStock() == 0 || bookOrder.quantity() > book.getStock()) {
                return ResponseEntity.badRequest().body(new OrderResponse("Failure, unable to place order", null, 0));
            }

            totalOrderCost += bookOrder.quantity() * book.getPrice();

            if (totalOrderCost > MAXPURCHASEPRICE) {
                return ResponseEntity.badRequest().body(new OrderResponse("Failure, order exceeds $120. Unable to proceed with the order", null, 0));
            }
        }

        List<String> orderedTitles = order.listOfBooks().stream()
                .map(bookOrder -> {
                    Book book = stock.get(bookOrder.key());
                    book.setStock(book.getStock() - bookOrder.quantity());
                    return book.getTitle();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OrderResponse("Success", orderedTitles, totalOrderCost));
    }

    public ResponseEntity<RestockResponse> restock(RestockRequest restockRequest) {
        if (!BookAdmin.isAdmin(restockRequest.admin().username(), restockRequest.admin().password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new RestockResponse("Failure: Authentication failed"));
        }
        
        List<Map.Entry<Book, Integer>> updates = restockRequest.restockOrder().stream()
            .map(item -> {
                Book book = stock.get(item.key());
                if (book == null || book.getTitle().equals("Limited Collectors Edition")) {
                    throw new IllegalArgumentException("Cannot restock book " + item.key());
                }

                //NOTE: Should I add restock possibility in multiples of 10? The assignment stated "can" not "should".

                return Map.entry(book, item.quantity());
            })
        .toList();

        updates.forEach(entry -> {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            book.setStock(book.getStock() + quantity);
        });

        return ResponseEntity.ok(new RestockResponse("Success: Restocked"));

    }

    public Map<String, Book> getStock() {
        return stock;
    }

}
