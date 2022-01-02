package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class CartControllerTest {
    public static final String TEST_USERNAME = "test";
    public static final String ITEM_NAME = "item_name";
    public static final String PASSWORD = "password";
    @InjectMocks
    private CartController cartController;
    @Mock
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Mock
    private final UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private final CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test()
    public void addToCartFail() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();
        Cart cart1 = new Cart();
        CreateRequest(modifyCartRequest, user, item, cart1);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);
        Mockito.when(itemRepository.findById(2l)).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private void CreateRequest(ModifyCartRequest modifyCartRequest, User user, Item item, Cart cart) {
        modifyCartRequest.setUsername(TEST_USERNAME);
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        user.setId(1l);
        user.setUsername(TEST_USERNAME);
        user.setPassword(PASSWORD);
        item.setId(1l);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.valueOf(199.99));
        cart.setUser(user);
        user.setCart(cart);
    }

    @Test()
    public void addToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();
        Cart cart = new Cart();
        CreateRequest(modifyCartRequest, user, item, cart);
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);
        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart changedCart = response.getBody();
        assertNotNull(changedCart);
        assertNotNull(changedCart.getUser());
        assertEquals(user, changedCart.getUser());
        assertEquals(item, changedCart.getItems().get(0));
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();
        Cart cart = new Cart();
        CreateRequest(modifyCartRequest, user, item, cart);
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart changedCart = response.getBody();
        assertNotNull(changedCart);
        assertEquals(0, changedCart.getItems().size());
    }

    @Test()
    public void removeFromCartFail() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();
        Cart cart = new Cart();
        CreateRequest(modifyCartRequest, user, item, cart);
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        Mockito.when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}