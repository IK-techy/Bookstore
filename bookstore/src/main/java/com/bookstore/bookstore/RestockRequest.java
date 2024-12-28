package com.bookstore.bookstore;

import java.util.List;

public record RestockRequest(BookAdmin admin, List<RestockOrder> restockOrder) {

}
