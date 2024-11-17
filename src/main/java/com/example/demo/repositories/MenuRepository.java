package com.example.demo.repositories;

import com.example.demo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository  extends JpaRepository<Menu, Long> {

    Menu findByRestaurantId(Long restaurantId);
}