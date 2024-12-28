package com.bookstore.bookstore;

import java.util.List;

public record OrderResponse(String message, List<String> books, int totalPrice) {
}