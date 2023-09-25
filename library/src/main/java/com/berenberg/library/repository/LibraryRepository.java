package com.berenberg.library.repository;

import com.berenberg.library.exception.*;
import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemBorrowed;
import com.berenberg.library.model.ItemCopy;
import com.berenberg.library.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryRepository {
    private Map<Integer, Item> inventoryItems = new ConcurrentHashMap<>();
    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private Map<Integer, ItemBorrowed> borrowedItems = new ConcurrentHashMap<>();

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void addItemToInventory(Item item) {
        inventoryItems.put(item.getItemID(), item);
    }

    public void addBorrowedItems(ItemBorrowed itemBorrowed) {
        borrowedItems.put(itemBorrowed.getItemId(), itemBorrowed);
    }

    public void borrowItem(int userId, int itemId, int uniqueId) {
        if (isUserBorrowedItem(userId, itemId, uniqueId)) {
            throw new UserAlreadyBorrowedException("User %s already borrowed this item.".formatted(userId));
        }

        Item item = inventoryItems.get(itemId);
        if (item != null) {
            ItemCopy copy = item.findCopyById(uniqueId);
            if (copy != null && copy.isAvailable()) {
                copy.borrow(userId);
                borrowedItems.computeIfAbsent(itemId, k -> new ItemBorrowed(itemId)).addCopy(copy);
            } else {
                throw new ItemCopyNotAvailableException("Item copy %s is not available.".formatted(uniqueId));
            }
        } else {
            throw new ItemNotFoundException("Item %s not found.".formatted(itemId));
        }
    }

    public void returnItem(int userId, int itemId, int uniqueId) {
        borrowedItems.compute(itemId, (k, itemBorrowed) -> {
            if (itemBorrowed != null) {
                List<ItemCopy> copies = new ArrayList<>(itemBorrowed.getCopies());
                Optional<ItemCopy> copyToReturn = copies.stream()
                        .filter(copy -> copy.getUniqueID() == uniqueId && copy.getBorrowedByUserId() == userId)
                        .findFirst();
                if (copyToReturn.isPresent()) {
                    // Set isAvailable to true in the copy to return
                    copyToReturn.get().returnItem();
                    // Remove the copy from the borrowed list
                    copies.remove(copyToReturn.get());
                } else {
                    throw new ItemCopyNotFoundException("ItemCopy %s not found for the specified user and item.".formatted(uniqueId));
                }

                // If the new list is empty, return null to remove the borrowed item
                if (copies.isEmpty()) {
                    return null;
                } else {
                    // Update the itemBorrowed with the modified list
                    itemBorrowed.setItemCopies(copies);
                }
            }
            return itemBorrowed;
        });
    }

    public List<Item> getCurrentInventory() {
        try {
            List<Item> itemsWithAvailableCopies = new ArrayList<>();

            for (Item item : inventoryItems.values()) {
                List<ItemCopy> availableCopies = new ArrayList<>();

                for (ItemCopy copy : item.getItemCopies()) {
                    // Check if the copy is not part of borrowedItems for the same item ID
                    if (!isCopyBorrowed(item.getItemID(), copy.getUniqueID())) {
                        availableCopies.add(copy);
                    }
                }

                if (!availableCopies.isEmpty()) {
                    Item itemWithAvailableCopies = new Item(item.getItemID(), item.getItemType(), item.getTitle(), availableCopies.size());
                    itemWithAvailableCopies.setItemCopies(availableCopies);
                    itemsWithAvailableCopies.add(itemWithAvailableCopies);
                }
            }

            return itemsWithAvailableCopies;

        } catch (Exception e) {
            throw new LibraryException("An error occurred while getting the Current Inventory: " + e.getMessage());
        }
    }

    public List<ItemBorrowed> getOverdueItems() {
        try {
            List<ItemBorrowed> overdueItems = new ArrayList<>();
            LocalDate today = LocalDate.now();

            for (ItemBorrowed borrowedItem : borrowedItems.values()) {
                List<ItemCopy> overdueCopies = new ArrayList<>();

                for (ItemCopy copy : borrowedItem.getCopies()) {
                    LocalDate returnDate = copy.getReturnDate();
                    if (returnDate != null && returnDate.isBefore(today)) {
                        overdueCopies.add(copy);
                    }
                }

                if (!overdueCopies.isEmpty()) {
                    borrowedItem.setItemCopies(overdueCopies);
                    overdueItems.add(borrowedItem);
                }
            }

            return overdueItems;

        } catch (Exception e) {
            throw new LibraryException("An error occurred while getting the Overdue Items: " + e.getMessage());
        }
    }

    public List<ItemBorrowed> getBorrowedItems(int userId) {
        try {
            List<ItemBorrowed> borrowedItemsList = new ArrayList<>();

            for (ItemBorrowed borrowedItem : borrowedItems.values()) {
                List<ItemCopy> borrowedCopies = borrowedItem.getCopies();
                boolean userBorrowed = false;

                for (ItemCopy copy : borrowedCopies) {
                    if (copy.getBorrowedByUserId() == userId) {
                        userBorrowed = true;
                        break;
                    }
                }

                if (userBorrowed) {
                    borrowedItemsList.add(borrowedItem);
                }
            }

            return borrowedItemsList;

        } catch (Exception e) {
            throw new LibraryException("An error occurred while getting Borrowed Items: " + e.getMessage());
        }
    }

    public boolean isItemAvailable(int itemId) {
        Item item = inventoryItems.get(itemId);
        return item != null && item.hasAvailableCopies();
    }

    private boolean isCopyBorrowed(int itemId, int uniqueId) {
        ItemBorrowed borrowedItem = borrowedItems.get(itemId);

        if (borrowedItem != null) {
            for (ItemCopy copy : borrowedItem.getCopies()) {
                if (copy.getUniqueID() == uniqueId) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isUserBorrowedItem(int userId, int itemId, int uniqueId) {
        ItemBorrowed itemBorrowed = borrowedItems.get(itemId);
        return itemBorrowed != null && isCopyInBorrowedList(itemBorrowed, uniqueId, userId);
    }

    private boolean isCopyInBorrowedList(ItemBorrowed itemBorrowed, int uniqueId, int userId) {
        List<ItemCopy> copies = itemBorrowed.getCopies();
        for (ItemCopy copy : copies) {
            if (copy.getUniqueID() == uniqueId && copy.getBorrowedByUserId() == userId) {
                return true;
            }
        }
        return false;
    }
}
