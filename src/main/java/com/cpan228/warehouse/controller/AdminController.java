package com.cpan228.warehouse.controller;

import com.cpan228.warehouse.dto.DistributionCentre;
import com.cpan228.warehouse.model.Item;
import com.cpan228.warehouse.dto.ItemSearchForm;
import com.cpan228.warehouse.repository.ItemRepository;
import com.cpan228.warehouse.service.DistributionCentreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final DistributionCentreService distributionCentreService;
    private final ItemRepository itemRepository;

    @GetMapping("/distribution-centres")
    public String showDistributionCentres(Model model) {
        List<DistributionCentre> centres = distributionCentreService.getAllDistributionCentres();
        model.addAttribute("distributionCentres", centres);
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("searchForm", new ItemSearchForm());
        return "admin/distribution-centres";
    }

    @PostMapping("/search-centres")
    public String searchCentres(@ModelAttribute ItemSearchForm searchForm, Model model) {
        List<DistributionCentre> availableCentres =
                distributionCentreService.findCentresWithItem(searchForm.getName(), searchForm.getBrand());

        log.info("Found {} centres with item {} ({})", 
            availableCentres.size(), searchForm.getName(), searchForm.getBrand());
        availableCentres.forEach(centre -> 
            log.info("Centre: {} at {}", centre.getName(), centre.getAddress()));

        model.addAttribute("distributionCentres", distributionCentreService.getAllDistributionCentres());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searchResults", availableCentres);

        return "admin/distribution-centres";
    }

    @PostMapping("/request-item")
    @Transactional
    public String requestItem(@RequestParam Long centreId,
                              @RequestParam Item.Brand brand,
                              @RequestParam String name,
                              @RequestParam int quantity,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<Item> requestedItem = distributionCentreService.requestItem(centreId, brand, name, quantity);
            if (!requestedItem.isPresent()) {
                redirectAttributes.addFlashAttribute("error",
                        "Failed to request item from distribution centre.");
                return "redirect:/admin/distribution-centres";
            }

            Optional<Item> existingItem = itemRepository.findByBrandAndName(brand, name);

            if (existingItem.isPresent()) {
                Item item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
                itemRepository.save(item);
            } else {
                // Create a new item instead of using the detached one
                Item newItem = new Item();
                Item distributionItem = requestedItem.get();

                // Copy properties
                newItem.setName(distributionItem.getName());
                newItem.setBrand(distributionItem.getBrand());
                newItem.setYearOfCreation(distributionItem.getYearOfCreation());
                newItem.setPrice(distributionItem.getPrice());
                newItem.setQuantity(quantity);

                itemRepository.save(newItem);
            }

            redirectAttributes.addFlashAttribute("message",
                    String.format("Successfully requested %d units of %s (%s)!",
                            quantity, name, brand.getDisplayName()));

        } catch (Exception e) {
            log.error("Error processing request: ", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error processing request. Please try again.");
        }

        return "redirect:/admin/distribution-centres";
    }
}