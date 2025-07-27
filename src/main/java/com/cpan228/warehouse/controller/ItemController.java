package com.cpan228.warehouse.controller;

import com.cpan228.warehouse.model.Item;
import com.cpan228.warehouse.repository.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public String showInventory(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "inventory";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_EMPLOYEE')")
    public String showAddForm(Model model) {
        model.addAttribute("item", new Item());
        return "addItem";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_EMPLOYEE')")
    public String submitItem(@Valid @ModelAttribute("item") Item item,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "addItem";
        }
        itemRepository.save(item);
        redirectAttributes.addFlashAttribute("message", 
            "Item '" + item.getName() + "' has been added successfully!");
        return "redirect:/inventory";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        itemRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Item has been deleted successfully!");
        return "redirect:/inventory";
    }
}