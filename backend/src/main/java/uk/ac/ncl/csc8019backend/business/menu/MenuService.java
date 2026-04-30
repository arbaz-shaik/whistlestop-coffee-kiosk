package uk.ac.ncl.csc8019backend.business.menu;

import java.util.List;

import org.springframework.stereotype.Service;

import uk.ac.ncl.csc8019backend.business.common.ItemNotFoundException;

@Service
public class MenuService {
    private final MenuItemRepository menuItemRepository;

    //constructor injection spring automatically passses in the repository
    // we can never write new menuItemRepository, spring will do it for us
    // author: parthbhilare

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
}

/** takes only available items.
 * this is what the customer will see when they look at the menu.
 * author: parthbhilare
 * */

public List<MenuItem> getAllAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue();
}

 /**
look up a menu item by its ID
called by orderservice when validating item in an order.
author: parthbhilare
*/

public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
        .orElseThrow(() -> new ItemNotFoundException("Menu item not found with id: " + id));
    
}
/**
 *staff can mark item in or out of stock.
 does not delete the item just flip the available flag to.
 author: parthbhilare
 */
public MenuItem updateMenuItemAvailability(Long id, Boolean available) {
        MenuItem item = getMenuItemById(id);  // throws ItemNotFoundException if not found
        item.setAvailable(available);

    
        return menuItemRepository.save(item); // save () on existing entity = update 
}
}

