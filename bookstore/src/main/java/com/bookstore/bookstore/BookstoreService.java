package com.bookstore.bookstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public OrderResponse placeOrder(OrderRequest order) {

        int totalOrderCost = 0;

        for (BookOrder bookOrder : order.listOfBooks()) {

            Book book = stock.get(bookOrder.key());

            if (book == null || book.getStock() == 0 || bookOrder.quantity() > book.getStock()) {
                return new OrderResponse("Failure, unable to place order", null, 0);
            }

            totalOrderCost += bookOrder.quantity() * book.getPrice();

            if (totalOrderCost > MAXPURCHASEPRICE) {
                return new OrderResponse("Failure, order exceeds $120. Unable to proceed with the order", null, 0);
            }
        }

        List<String> orderedTitles = order.listOfBooks().stream()
                .map(bookOrder -> {
                    Book book = stock.get(bookOrder.key());
                    book.setStock(book.getStock() - bookOrder.quantity());
                    return book.getTitle();
                })
                .collect(Collectors.toList());

        return new OrderResponse("Success", orderedTitles, totalOrderCost);
    }

    public RestockResponse restock(RestockRequest restockRequest) {
        if (!BookAdmin.isAdmin(restockRequest.admin().username(), restockRequest.admin().password())) {
            return new RestockResponse("Failure: Authentication failed");
        }
        restockRequest.restockOrder()
                .forEach(item -> {
                    Book book = stock.get(item.key());
                    if (book == null || book.getTitle().equals("Limited Collectors Edition")) {
                        throw new IllegalArgumentException("Cannot restock book " + item.key());
                    }
                    book.setStock(book.getStock() + item.quantity());
                });

        return new RestockResponse("Success: Restocked");

        //NOTE: Should I add restock possibility in multiples of 10? 

    }

    public Map<String, Book> getStock() {
        return stock;
    }

}
