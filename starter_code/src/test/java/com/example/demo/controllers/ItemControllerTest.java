package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    public static final String ITEM = "item_name";
    public static final double PRICE = 199.99;
    @InjectMocks
    private ItemController itemController;
    @Mock
    private final ItemRepository itemRepository= mock(ItemRepository.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void byId() {
        Item item = createItem();
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1l);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item returnedItem = response.getBody();
        assertNotNull(returnedItem);
        assertEquals(ITEM, returnedItem.getName());
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1l);
        item.setName(ITEM);
        item.setPrice(BigDecimal.valueOf(PRICE));
        return item;
    }

    @Test
    public void getItems() {
        Item item = createItem();

        Mockito.when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
    }

    @Test
    public void byIdFail() {
        long itemId = 1l;

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        final ResponseEntity<Item> response = itemController.getItemById(itemId);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void byName() {
        Item item = createItem();
        Mockito.when(itemRepository.findByName(ITEM)).thenReturn(Arrays.asList(item));
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(ITEM);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals(ITEM, itemList.get(0).getName());
        assertEquals(1, itemList.size());
    }

    @Test
    public void byNameFail() {
        Mockito.when(itemRepository.findByName(ITEM)).thenReturn(Collections.emptyList());
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(ITEM);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}