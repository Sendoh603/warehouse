package com.cpan228.warehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Brand must be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Brand brand;

    @Min(value = 2022, message = "Year of creation must be more than 2021")
    @Column(name = "year_of_creation", nullable = false)
    private int yearOfCreation;

    @DecimalMin(value = "1000.01", message = "Price must be more than 1000")
    @Column(nullable = false)
    private double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private int quantity = 0; // Initialize with default value

    public enum Brand {
        BALENCIAGA("Balenciaga"),
        STONE_ISLAND("Stone Island"),
        DIOR("Dior"),
        GUCCI("Gucci"),
        PRADA("Prada"),
        VERSACE("Versace");

        private final String displayName;

        Brand(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // Constructors
    public Item() {}

    public Item(String name, Brand brand, int yearOfCreation, double price) {
        this.name = name;
        this.brand = brand;
        this.yearOfCreation = yearOfCreation;
        this.price = price;
    }

    public Item(Long id, String name, Brand brand, int yearOfCreation, double price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.yearOfCreation = yearOfCreation;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(int yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand=" + brand +
                ", yearOfCreation=" + yearOfCreation +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return yearOfCreation == item.yearOfCreation &&
                Double.compare(item.price, price) == 0 &&
                quantity == item.quantity &&
                java.util.Objects.equals(id, item.id) &&
                java.util.Objects.equals(version, item.version) &&
                java.util.Objects.equals(name, item.name) &&
                brand == item.brand;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, version, name, brand, yearOfCreation, price, quantity);
    }
}