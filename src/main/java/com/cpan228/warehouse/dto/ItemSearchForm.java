package com.cpan228.warehouse.dto;

import com.cpan228.warehouse.model.Item;
import lombok.Data;

@Data
public class ItemSearchForm {
    private String name;
    private Item.Brand brand;
}