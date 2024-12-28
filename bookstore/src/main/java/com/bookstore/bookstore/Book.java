package com.bookstore.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Book {
    private final String title;
    private final Integer price;
    @JsonIgnore
    private Integer stock;

    public Book(String title, int price, int stock) {
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
