package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.*;
import com.selivanov.springmvcproject.service.CartElementService;
import com.selivanov.springmvcproject.service.ClientService;
import com.selivanov.springmvcproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;
    private final ProductService productService;
    private final CartElementService cartElementService;

    @Autowired
    private ClientController(ClientService clientService, ProductService productService, CartElementService cartElementService) {
        this.clientService = clientService;
        this.productService = productService;
        this.cartElementService = cartElementService;
    }

    @GetMapping("/clients")
    public String getAllClients(Model model) {
        List<Client> allClients = clientService.getAllClients();
        model.addAttribute("clients", allClients);
        return "client/clients";
    }

    @GetMapping("/clients/register")
    public String createNewClient(Model model) {
        model.addAttribute("client", new Client());
        return "client/register_client";
    }

    @PostMapping("/clients")
    public String createNewClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return "redirect:/clients/login";
    }

    @GetMapping("/clients/login")
    public String loginClient() {
        return "client/login_client";
    }

    @PostMapping("/clients/login")
    public String loginClient(@RequestParam String name, @RequestParam String email, Model model) {
        Client client = clientService.getClientByName(name);

        if (name != null && client.getEmail().equals(email)) {
            // Аутентификация успешна
            return "redirect:/clients/%s/shop".formatted(name);
        } else {
            // Неудачная аутентификация
            model.addAttribute("error", "Invalid credentials");
            return "client/login_client";
        }
    }

    @GetMapping("/clients/{clientId}")
    public String getClientById(@PathVariable Integer clientId, Model model) {
        Client clientById = clientService.getClientById(clientId);
        model.addAttribute("clients", List.of(clientById));
        return "client/clients";
    }


    @GetMapping("/clients/{name}/shop")
    public String shopClient(@PathVariable String name,
                             Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "client/shop";
    }

//    @GetMapping("/clients/{name}/cart-element")
//    public String cartClient(@PathVariable String name, Integer productId, CartElement cartElement, Model model) {
//        if (productId != null) {
//            cartElementService.addProductToCartElement(productId, cartElement);
//        }
//        model.addAttribute("productId", productId);
//        model.addAttribute("cartElement", new CartElement());
//        return "client/cart_element";
//    }

    @GetMapping("/clients/{clientId}/orders")
    public String getClientOrders(@PathVariable Integer clientId, Model model) {
        List<Order> orders = clientService.getAllClientOrders(clientId);
        model.addAttribute("orders", orders);
        return "client/orders";
    }

    @GetMapping("/clients/{clientId}/orders/new")
    public String getNewOrder(@PathVariable Integer clientId, Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("order", new Order());
        return "client/new_order";
    }

    @PostMapping("/clients/{clientId}/orders")
    public String addOrderToClient(@PathVariable Integer clientId,
                                   @ModelAttribute Order order) {
        clientService.addOrderToClient(clientId, order);
        return "redirect:/clients/%d/orders".formatted(clientId);
    }
}