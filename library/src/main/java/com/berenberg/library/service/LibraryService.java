package com.berenberg.library.service;

import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemBorrowed;
import com.berenberg.library.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository library;

    @Autowired
    public LibraryService(LibraryRepository library) {
        this.library = library;
    }

    public void borrowItem(int userId, int itemId, int uniqueId) {
        library.borrowItem(userId, itemId, uniqueId);
    }

    public void returnItem(int userId, int itemId, int uniqueId) {
        library.returnItem(userId, itemId, uniqueId);
    }

    public List<Item> getCurrentInventory() {
        return library.getCurrentInventory();
    }

    public List<ItemBorrowed> getOverdueItems() {
        return library.getOverdueItems();
    }

    public List<ItemBorrowed> getBorrowedItems(int userId) {
        return library.getBorrowedItems(userId);
    }

    public boolean isItemAvailable(int itemId) {
        return library.isItemAvailable(itemId);
    }
}
