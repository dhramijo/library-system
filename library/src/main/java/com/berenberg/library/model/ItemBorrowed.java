package com.berenberg.library.model;

import java.util.ArrayList;
import java.util.List;

public class ItemBorrowed {
    private int itemId;
    private List<ItemCopy> copies;

    public ItemBorrowed(int itemId) {
        this.itemId = itemId;
        this.copies = new ArrayList<>();
    }

    public int getItemId() {
        return itemId;
    }

    public List<ItemCopy> getCopies() {
        return copies;
    }

    public void setItemCopies(List<ItemCopy> itemCopies) {
        this.copies = itemCopies;
    }

    public void removeCopy(int uniqueId, int userId) {
        copies.removeIf(copy -> copy.getUniqueID() == uniqueId && copy.getBorrowedByUserId() == userId);
    }

    public void addCopy(ItemCopy copy) {
        copies.add(copy);
    }
}
