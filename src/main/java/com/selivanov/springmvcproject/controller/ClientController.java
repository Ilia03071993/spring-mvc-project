package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.*;
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


    @Autowired
    private ClientController(ClientService clientService, ProductService productService) {
        this.clientService = clientService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String getAllClients() {
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

//    @GetMapping("/clients/{name}/orders")
//    public String getClientOrders(@PathVariable String name, Model model) {
//        List<Order> orders = clientService.getAllClientOrders(name);
//        model.addAttribute("orders", orders);
//        return "client/orders";
//    }
//
//    @GetMapping("/clients/{name}/orders/new")
//    public String getNewOrder(@PathVariable String name, Model model) {
//        model.addAttribute("name", name);
//        model.addAttribute("order", new Order());
//        return "client/new_order";
//    }
//
//    @PostMapping("/clients/{name}/orders")
//    public String addOrderToClient(@PathVariable String name,
//                                   @ModelAttribute Order order) {
//        clientService.addOrderToClient(name, order);
//        return "redirect:/clients/%s/orders".formatted(name);
//    }
}