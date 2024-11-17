package com.example.demo.controllers;

import com.example.demo.entity.Menu;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.Restaurant;
import com.example.demo.repositories.MenuItemRepository;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.repositories.RestaurantRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
@SecurityRequirement(name = "Keycloak")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/public/list")
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/public/menu/{restaurantId}")
    public Menu getMenu(@PathVariable Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId);
        if (menu != null) {
            menu.setMenuItems(menuItemRepository.findAllByMenuId(menu.getId()));
        }
        return menu;
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PostMapping("/menu")
    @PreAuthorize("hasRole('manager')")
    public Menu createMenu(@RequestBody Menu menu) {
        menuRepository.save(menu);
        menu.getMenuItems().forEach(menuItem -> {
            menuItem.setMenuId(menu.getId());
            menuItemRepository.save(menuItem);
        });
        return menu;
    }

    @PutMapping("/menu/item/{itemId}/{price}")
    @PreAuthorize("hasRole('owner')")
    public MenuItem updateMenuItemPrice(@PathVariable Long itemId, @PathVariable BigDecimal price) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);
        if (menuItem.isPresent()) {
            MenuItem item = menuItem.get();
            item.setPrice(price);
            menuItemRepository.save(item);
            return item;
        }
        throw new RuntimeException("MenuItem not found");
    }
}
