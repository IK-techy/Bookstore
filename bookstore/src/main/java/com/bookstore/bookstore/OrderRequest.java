package com.bookstore.bookstore;

import java.util.List;

public record OrderRequest(List<BookOrder> listOfBooks) {

}
