package com.berenberg.library.repository;

import com.berenberg.library.config.LibraryInitializer;
import com.berenberg.library.exception.ItemNotFoundException;
import com.berenberg.library.exception.UserAlreadyBorrowedException;
import com.berenberg.library.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LibraryInitializer.class)
public class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    public void testAddBorrowedItems() {
        Item item = new Item(3, ItemType.BOOK, "The Art Of Computer Programming Volumes 1-6", 1);

        ItemCopy copy = new ItemCopy(5, item);
        copy.borrow(3);

        ItemBorrowed itemBorrowed = new ItemBorrowed(3);
        itemBorrowed.setItemCopies(Arrays.asList(copy));

        libraryRepository.addBorrowedItems(itemBorrowed);

        assertEquals(itemBorrowed, libraryRepository.getBorrowedItems(3).get(0));
    }

    @Test
    public void testBorrowItem_WhenItemIsAvailable() {
        User user = new User(3, "Jonad");
        Item item = new Item(7, ItemType.BOOK, "Introduction to Algorithms", 1);
        ItemCopy copy = new ItemCopy(1, item);
        item.setItemCopies(Arrays.asList(copy));
        libraryRepository.addUser(user);
        libraryRepository.addItemToInventory(item);

        libraryRepository.borrowItem(3, 7, 1);

        assertFalse(copy.isAvailable());
    }

    @Test
    public void testBorrowItem_WhenUserAlreadyBorrowed() {
        User user = new User(1, "Jonad");
        Item item = new Item(1, ItemType.BOOK, "Introduction to Algorithms", 1);
        ItemCopy copy = new ItemCopy(1, item);
        item.setItemCopies(Arrays.asList(copy));
        libraryRepository.addUser(user);
        libraryRepository.addItemToInventory(item);

        assertThrows(UserAlreadyBorrowedException.class, () -> libraryRepository.borrowItem(1, 1, 1));
    }

    @Test
    public void testBorrowItem_WhenItemNotFound() {
        User user = new User(1, "Jonad");
        libraryRepository.addUser(user);

        assertThrows(ItemNotFoundException.class, () -> libraryRepository.borrowItem(1, 8, 1));
    }

    @Test
    public void testReturnItem() {
        User user = new User(1, "Jonad");
        Item item = new Item(7, ItemType.BOOK, "Introduction to Algorithms", 1);
        ItemCopy copy = new ItemCopy(1, item);
        item.setItemCopies(Arrays.asList(copy));
        libraryRepository.addUser(user);
        libraryRepository.addItemToInventory(item);

        libraryRepository.borrowItem(1, 7, 1);
        assertFalse(copy.isAvailable());

        libraryRepository.returnItem(1, 7, 1);
        assertTrue(copy.isAvailable());
    }

    @Test
    public void testGetCurrentInventory() {
        Item expectedItem4 = new Item(4, ItemType.BOOK, "The Pragmatic Programmer", 1);

        List<Item> currentInventory = libraryRepository.getCurrentInventory();

        assertEquals(4, currentInventory.size());
        assertThat(currentInventory.get(1))
                .usingRecursiveComparison()
                .isEqualTo(expectedItem4);
    }

    @Test
    public void testGetOverdueItems() {
        List<ItemBorrowed> overdueItems = libraryRepository.getOverdueItems();

        assertEquals(2, overdueItems.size());
        assertEquals(1, overdueItems.get(0).getItemId());
        assertEquals(2, overdueItems.get(0).getCopies().size());
        assertEquals(2, overdueItems.get(1).getItemId());
        assertEquals(1, overdueItems.get(1).getCopies().size());
    }

    @Test
    public void testGetBorrowedItems() {
        List<ItemBorrowed> borrowedItemsUser1 = libraryRepository.getBorrowedItems(1);

        assertEquals(1, borrowedItemsUser1.size());
        assertEquals(1, borrowedItemsUser1.get(0).getItemId());

        List<ItemBorrowed> borrowedItemsUser2 = libraryRepository.getBorrowedItems(2);
        assertEquals(2, borrowedItemsUser2.size());
        assertEquals(2, borrowedItemsUser2.get(0).getItemId());
        assertEquals(6, borrowedItemsUser2.get(1).getItemId());
    }
}
