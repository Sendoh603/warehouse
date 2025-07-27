package com.cpan228.warehouse.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cpan228.warehouse.model.Item;
import com.cpan228.warehouse.model.User;
import com.cpan228.warehouse.repository.ItemRepository;
import com.cpan228.warehouse.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Load users
        loadUsers();
        // Load initial inventory items
        loadInventoryItems();
    }

    private void loadUsers() {
        // Create admin user if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.UserRole.ROLE_ADMIN);
            userRepository.save(adminUser);
        }

        // Create employee user if it doesn't exist
        if (userRepository.findByUsername("employee").isEmpty()) {
            User employeeUser = new User();
            employeeUser.setUsername("employee");
            employeeUser.setPassword(passwordEncoder.encode("emp123"));
            employeeUser.setRole(User.UserRole.ROLE_WAREHOUSE_EMPLOYEE);
            userRepository.save(employeeUser);
        }
    }

    private void loadInventoryItems() {
        // Only load items if the repository is empty
        if (itemRepository.count() == 0) {
            // Add sample items
            Item item1 = new Item();
            item1.setName("Sport T-Shirt");
            item1.setBrand(Item.Brand.BALENCIAGA); // Using available brand
            item1.setYearOfCreation(2024);
            item1.setPrice(1299.99);
            item1.setQuantity(100);
            itemRepository.save(item1);

            Item item2 = new Item();
            item2.setName("Running Shoes");
            item2.setBrand(Item.Brand.STONE_ISLAND); // Using available brand
            item2.setYearOfCreation(2025);
            item2.setPrice(1499.99);
            item2.setQuantity(50);
            itemRepository.save(item2);

            Item item3 = new Item();
            item3.setName("Training Pants");
            item3.setBrand(Item.Brand.DIOR); // Using available brand
            item3.setYearOfCreation(2023);
            item3.setPrice(1199.99);
            item3.setQuantity(75);
            itemRepository.save(item3);
        }
    }
}