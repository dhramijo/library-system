package com.berenberg.library.controller;

import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemBorrowed;
import com.berenberg.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/user/{userId}/items/{itemId}/{uniqueId}/borrow")
    public void borrowItem(
            @PathVariable int userId,
            @PathVariable int itemId,
            @PathVariable int uniqueId
    ) {
        libraryService.borrowItem(userId, itemId, uniqueId);
    }

    @PostMapping("/user/{userId}/items/{itemId}/{uniqueId}/return")
    public void returnItem(
            @PathVariable int userId,
            @PathVariable int itemId,
            @PathVariable int uniqueId
    ) {
        libraryService.returnItem(userId, itemId, uniqueId);
    }

    @GetMapping("/user/{userId}/items/borrowed")
    public List<ItemBorrowed> getBorrowedItems(@PathVariable int userId) {
        return libraryService.getBorrowedItems(userId);
    }

    @GetMapping("/items/current-inventory")
    public List<Item> getCurrentInventory() {
        return libraryService.getCurrentInventory();
    }

    @GetMapping("/items/overdue")
    public List<ItemBorrowed> getOverdueItems() {
        return libraryService.getOverdueItems();
    }

    @GetMapping("/items/{itemId}/available")
    public boolean isItemAvailable(@PathVariable int itemId) {
        return libraryService.isItemAvailable(itemId);
    }
}
