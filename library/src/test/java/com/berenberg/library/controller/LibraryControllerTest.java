package com.berenberg.library.controller;

import com.berenberg.library.model.Item;
import com.berenberg.library.model.ItemBorrowed;
import com.berenberg.library.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {

    @InjectMocks
    private LibraryController libraryController;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testBorrowItem() throws Exception {
        int userId = 1;
        int itemId = 1;
        int uniqueId = 1;

        doNothing().when(libraryService).borrowItem(userId, itemId, uniqueId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/{userId}/items/{itemId}/{uniqueId}/borrow", userId, itemId, uniqueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).borrowItem(userId, itemId, uniqueId);
    }

    @Test
    public void testGetBorrowedItems() throws Exception {
        int userId = 1;

        List<ItemBorrowed> borrowedItems = Collections.emptyList();
        when(libraryService.getBorrowedItems(userId)).thenReturn(borrowedItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{userId}/items/borrowed", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).getBorrowedItems(userId);
    }

    @Test
    public void testReturnItem() throws Exception {
        int userId = 1;
        int itemId = 1;
        int uniqueId = 1;

        doNothing().when(libraryService).returnItem(userId, itemId, uniqueId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/{userId}/items/{itemId}/{uniqueId}/return", userId, itemId, uniqueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).returnItem(userId, itemId, uniqueId);
    }

    @Test
    public void testGetCurrentInventory() throws Exception {
        List<Item> currentInventory = Collections.emptyList();
        when(libraryService.getCurrentInventory()).thenReturn(currentInventory);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/current-inventory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).getCurrentInventory();
    }

    @Test
    public void testGetOverdueItems() throws Exception {
        List<ItemBorrowed> overdueItems = Collections.emptyList();
        when(libraryService.getOverdueItems()).thenReturn(overdueItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/overdue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).getOverdueItems();
    }

    @Test
    public void testIsItemAvailable() throws Exception {
        int itemId = 1;

        when(libraryService.isItemAvailable(itemId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/{itemId}/available", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(libraryService, times(1)).isItemAvailable(itemId);
    }
}
