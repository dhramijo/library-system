package com.berenberg.library.config;

import com.berenberg.library.model.*;
import com.berenberg.library.repository.LibraryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class LibraryInitializer {

    @Bean
    public LibraryRepository libraryRepository() {
        LibraryRepository libraryRepository = new LibraryRepository();

        // Initialize user data
        User user1 = new User(1, "Jonad Dhrami");
        User user2 = new User(2, "Mario Rossi");
        libraryRepository.addUser(user1);
        libraryRepository.addUser(user2);

        // Initialize item data
        Item item1 = new Item(1, ItemType.DVD, "Pi", 2);
        Item item2 = new Item(2, ItemType.VHS, "Pi", 1);
        Item item3 = new Item(3, ItemType.BOOK, "The Art Of Computer Programming Volumes 1-6", 1);
        Item item4 = new Item(4, ItemType.BOOK, "The Pragmatic Programmer", 1);
        Item item5 = new Item(5, ItemType.BOOK, "Java Concurrency In Practice", 1);
        Item item6 = new Item(6, ItemType.BOOK, "Introduction to Algorithms", 2);

        libraryRepository.addItemToInventory(item1);
        libraryRepository.addItemToInventory(item2);
        libraryRepository.addItemToInventory(item3);
        libraryRepository.addItemToInventory(item4);
        libraryRepository.addItemToInventory(item5);
        libraryRepository.addItemToInventory(item6);

        // Initialize borrowed items for testing overdue items
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(10);
        LocalDate pastDate2 = today.minusDays(13);
        LocalDate pastDate3 = today.minusDays(20);
        LocalDate futureDate = today.plusDays(10);

        // Borrowed items with overdue return dates
        ItemCopy overdueCopy1 = new ItemCopy(1, item1);
        overdueCopy1.borrow(1);
        overdueCopy1.setReturnDate(pastDate);

        ItemCopy overdueCopy2 = new ItemCopy(2, item1);
        overdueCopy2.borrow(1);
        overdueCopy2.setReturnDate(pastDate2);

        ItemBorrowed itemBorrowedOverdue = new ItemBorrowed(item1.getItemID());
        itemBorrowedOverdue.setItemCopies(Arrays.asList(overdueCopy1,overdueCopy2));
        libraryRepository.addBorrowedItems(itemBorrowedOverdue);

        ItemCopy overdueCopy3 = new ItemCopy(1, item2);
        overdueCopy3.borrow(2);
        overdueCopy3.setReturnDate(pastDate3);

        ItemBorrowed itemBorrowedOverdue2 = new ItemBorrowed(item2.getItemID());
        itemBorrowedOverdue2.setItemCopies(Arrays.asList(overdueCopy3));
        libraryRepository.addBorrowedItems(itemBorrowedOverdue2);


        // Borrowed items without overdue return dates
        ItemCopy notOverdueCopy1 = new ItemCopy(1, item6);
        notOverdueCopy1.borrow(2);
        notOverdueCopy1.setReturnDate(futureDate);

        ItemBorrowed itemBorrowedNotOverdue = new ItemBorrowed(item6.getItemID());
        itemBorrowedNotOverdue.setItemCopies(Arrays.asList(notOverdueCopy1));
        libraryRepository.addBorrowedItems(itemBorrowedNotOverdue);

        return libraryRepository;
    }
}
