package uk.ac.ncl.csc8019backend.business.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.ac.ncl.csc8019backend.business.common.ItemNotFoundException;

public class MenuItemTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuService menuService;

    // getAllAvailableItems

    @Test

    void getAllAvailableMenuItems_returnsOnlyAvailable(){
        // ARRANGE - Set up fake data 

        MenuItem latte = new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        MenuItem water = new MenuItem ("Mineral Water", new BigDecimal("1.00"),null);
        when(menuItemRepository.findByAvailableTrue()).thenReturn(List.of(latte,water));
       
        // Act -call the method 
        List<MenuItem> result = menuService.getAllAvailableMenuItems();

        // ASSERT - check the result 
        assertEquals(2,result.size());
        verify(menuItemRepository, times(1)).findByAvailableTrue();
    }
    @Test 
    void getAllAvailableMenuItems_returnsEmptyList_whenNoItemsAvailable(){
        when(menuItemRepository.findByAvailableTrue()).thenReturn(List.of());

        List<MenuItem> result = menuService.getAllAvailableMenuItems();

        assertTrue(result.isEmpty());
    }

     //getMenuItemsById

     @Test 
     void getMenuItemById_returnItem_whenExists(){
        MenuItem latte =new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(latte));

        MenuItem result =menuService.getMenuItemById(1L);

        assertThrows(ItemNotFoundException.class, () -> menuService.getMenuItemById(99L));
     }

     // updateMenuItemAvailability 

     @Test 

     void updateMenuItemAvailability_setAvailableToFalse(){
        MenuItem latte = new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(latte));
        when(menuItemRepository.save(latte)).thenReturn(latte);

        MenuItem result = menuService.updateMenuItemAvailability(1L, false);

        assertFalse(result.getAvailable());
        verify(menuItemRepository, times(1)).save(latte);
        

     }
}
