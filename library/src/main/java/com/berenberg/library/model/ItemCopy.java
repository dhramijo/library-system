package com.berenberg.library.model;

import com.berenberg.library.config.ItemCopySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

@JsonSerialize(using = ItemCopySerializer.class)
public class ItemCopy {
    private int uniqueID;
    private LocalDate returnDate;
    private boolean isAvailable;
    private int borrowedByUserId;
    private Item item;
    public ItemCopy(int uniqueID, Item item) {
        this.uniqueID = uniqueID;
        this.item = item;
        this.isAvailable = true;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getBorrowedByUserId() {
        return borrowedByUserId;
    }

    public void borrow(int userId) {
        isAvailable = false;
        borrowedByUserId = userId;
        returnDate = LocalDate.now().plusWeeks(1);
    }

    public void returnItem() {
        isAvailable = true;
        borrowedByUserId = 0; // Reset the borrower
        returnDate = null;
    }

    public Item getItem() {
        return item;
    }
}
