package com.example.ourapp;

import com.example.ourapp.dto.ItemDto;
import com.example.ourapp.model.Item;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.ItemRepository;
import com.example.ourapp.repository.UserRepository;
import com.example.ourapp.service.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveItem() {
        // Given
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setOwnerMail("test@example.com");
        itemDto.setCategory("Test Category");
        itemDto.setDate(LocalDate.now());
        itemDto.setLink("http://example.com");
        itemDto.setSharedInfo(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        // When
        itemService.saveItem(itemDto);

        // Then
        verify(itemRepository).save(itemCaptor.capture());

        Item savedItem = itemCaptor.getValue();
        assertEquals("Test Item", savedItem.getName());
        assertEquals("Test Description", savedItem.getDescription());
        assertEquals("test@example.com", savedItem.getOwnerMail());
        // ...assert other properties
    }

    @Test
    public void testDeleteItem() {
        // Given
        Long itemId = 1L;

        // When
        itemService.deleteItem(itemId);

        // Then
        verify(itemRepository).deleteById(itemId);
    }

    @Test
    public void testFindAllItems() {
        // Given
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // When
        List<ItemDto> result = itemService.findAllItems();

        // Then
        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
    }

    @Test
    public void testFindMyItems() {
        // Given
        String ownerMail = "test@example.com";

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setOwnerMail(ownerMail);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setOwnerMail(ownerMail);

        when(itemRepository.findMyItemsByMail(ownerMail)).thenReturn(Arrays.asList(item1, item2));

        // When
        List<ItemDto> result = itemService.findMyItems(ownerMail);

        // Then
        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
    }
}
