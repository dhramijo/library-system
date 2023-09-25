
# Berenberg Library Assignment

## The Challenge 
Build part of a simple library system that allows customers to borrow books, DVDs, VHSes, and CDs. Your code should provide the following functionality:
* Borrow and return items - items are loaned out for a period of one week.
  + For example, a customer can borrow WarGames on 21st February and they will be expected to return it by 28th February.
* Determine current inventory - this should show you the current items that are loanable. You should make allowances for multiple copies of the same item (i.e. there can be multiple copies of the same book/movie).
  * For example, if you choose to use the initial inventory, the current inventory should return the titles.
* Determine overdue items. i.e. all items that should have been returned before today.
  * For example, if a book was due on 12th February and today is 15th February, that book should be flagged as overdue.
* Determine the borrowed items for a user.
* Determine if a book is available.

## Approach/Assumptions

For this challenge I have created a 3 Layer application as follow:

* Controller - Where are defined all the resource endpoints of the library.

* Service - Here we can add additional business logic.

* Repository - Where is handled the in memory storage via ConcurrentHashMap.

``` java
private Map<Integer, Item> inventoryItems = new ConcurrentHashMap<>();
private Map<Integer, User> users = new ConcurrentHashMap<>();
private Map<Integer, ItemBorrowed> borrowedItems = new ConcurrentHashMap<>();
```

### Data Initializer

Have created a **LibraryInitializer** which loads sample data during application startup.

### Data Model

For this solution have created 4 main classes, as below:

* **User** - Contains user data
* **Item** - Item information
* **ItemCopy** - Data related to a specific copy of an item
* **ItemBorrowed** - Data related to a borrowed item, in particular it connects an Item to its copy.

## Tech Used

- Spring Boot 3.1.3
- Java 17


## API Endpoints

```
    BORROW ITEM   
    /api/v1/user/{userId}/items/{itemId}/{uniqueId}/borrow

    RETURN ITEM   
    /api/v1/user/{userId}/items/{itemId}/{uniqueId}/return

    GET BORROWED ITEMS
    /api/v1/user/{userId}/items/borrowed

    GET CURRENTY INVENTORY
    /api/v1/items/current-inventory

    GET OVERDUE ITEMS
    /api/v1/items/overdue

    CHECK ITEM AVAILABILITY
    /api/v1/items/{itemId}/available

```

## Running the application

You can run the application by running the following command or by running the main method in your IDE.
```
./mvnw spring-boot:run
```

Once the application is running you can test the APIs either via Postman (see provided Postman collection) or by running curl