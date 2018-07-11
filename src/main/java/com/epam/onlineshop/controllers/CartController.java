package com.epam.onlineshop.controllers;

import com.epam.onlineshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;

    @GetMapping(value = "/cart")
    public ModelAndView cart(ModelAndView model) {
        model.addObject("products", productService.getAllProducts());
        model.addObject("productService", productService);
        model.setViewName("cart");
        return model;
    }

    @RequestMapping(value = "/cart/{id}/delete", method = RequestMethod.GET)
    public ModelAndView deleteProduct(@PathVariable("id") Long id, ModelAndView model) {
        productService.deleteById(id);
        model.setViewName("redirect:/cart");
        return model;
    }
}