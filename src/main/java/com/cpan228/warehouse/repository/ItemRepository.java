package com.cpan228.warehouse.repository;

import com.cpan228.warehouse.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Add this method
    Optional<Item> findByBrandAndName(Item.Brand brand, String name);

    @Query("SELECT i FROM Item i WHERE i.brand = :brand AND i.yearOfCreation = 2022")
    List<Item> findByBrandAndYear2022(@Param("brand") Item.Brand brand);

    List<Item> findAllByOrderByBrandAsc();
    List<Item> findByBrand(Item.Brand brand);
    List<Item> findByYearOfCreation(int year);
    List<Item> findByPriceGreaterThan(double price);
}