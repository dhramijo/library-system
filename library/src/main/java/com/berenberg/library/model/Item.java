package com.berenberg.library.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    @JsonProperty("itemID")
    private int itemID;
    @JsonProperty("itemType")
    private ItemType itemType;
    @JsonProperty("title")
    private String title;
    @JsonProperty("copies")
    private List<ItemCopy> itemCopies;

    public Item(int itemID, ItemType itemType, String title, int totalCopies) {
        this.itemID = itemID;
        this.itemType = itemType;
        this.title = title;
        this.itemCopies = new ArrayList<>();

        for (int i = 0; i < totalCopies; i++) {
            itemCopies.add(new ItemCopy(i + 1, this));
        }
    }

    public int getItemID() {
        return itemID;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getTitle() {
        return title;
    }

    public List<ItemCopy> getItemCopies() {
        return itemCopies;
    }

    public void setItemCopies(List<ItemCopy> itemCopies) {
        this.itemCopies = itemCopies;
    }
    public boolean hasAvailableCopies() {
        return itemCopies.stream()
                .anyMatch(ItemCopy::isAvailable);
    }

    public ItemCopy findCopyById(int uniqueId) {
        return itemCopies.stream()
                .filter(copy -> copy.getUniqueID() == uniqueId)
                .findFirst().get();
    }
}
