package uk.ac.ncl.csc8019backend.business.menu;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class MenuItemTest {

    @Test 
    void hasLargeOption_returnsTrue_whenLargePriceSet(){
        MenuItem latte =new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        assertTrue(latte.hasLargeOption());

    }
    @Test
    void hasLargeOption_returnsFalse_whenLargePriceNull(){
        MenuItem water =new MenuItem("Mineral Water", new BigDecimal("1.00"),null);
        assertFalse(water.hasLargeOption());
    }
    @Test 
    void getPriceForSize_returnRegularPrice_whenSizeRegular(){
        MenuItem latte =new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        assertEquals(new BigDecimal ("2.50"),latte.getPriceForSize("regular"));

    }
    @Test 
    void getPriceForSize_returnRegularPrice_whenSizeLarge(){
        MenuItem latte =new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        assertEquals(new BigDecimal ("3.00"),latte.getPriceForSize("large"));
    }
    @Test
    void getPriceForSize_throwsException_whenLargeRequestedButNotAvailable(){
        MenuItem water =new MenuItem ("Mineral Water", new BigDecimal("1.00"),null);
        assertThrows(IllegalArgumentException.class,() -> water.getPriceForSize("Large"));
    }
    @Test
    void getPriceForSize_isCaseInsensitive(){
        MenuItem latte =new MenuItem("Latte", new BigDecimal("2.50"),new BigDecimal("3.00"));
        assertEquals(new BigDecimal ("3.00"), latte.getPriceForSize("LARGE"));
        assertEquals(new BigDecimal ("2.50"), latte.getPriceForSize("REGULAR"));
    }
}

