package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    @Autowired
    private ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public String getAllClients(Model model) {
        List<Client> allClients = clientService.getAllClients();
        model.addAttribute("clients", allClients);
        return "client/clients";
    }

    @GetMapping("/clients/{clientId}")
    public String getClientById(@PathVariable Integer clientId, Model model) {
        Client clientById = clientService.getClientById(clientId);
        model.addAttribute("clients", List.of(clientById));
        return "client/clients";
    }

    @GetMapping("/clients/new")
    public String createNewClient(Model model) {
        model.addAttribute("client", new Client());
        return "client/new_client";
    }

    @PostMapping("/clients")
    public String createNewClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }

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

    @GetMapping("/clients/{clientId}/orders/{orderId}/update")
    public String getUpdateOrder(@PathVariable Integer clientId,
                                 @PathVariable Integer orderId,
                                 Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("orderId", orderId);
        return "client/update_order";
    }

    @PostMapping("/clients/{clientId}/orders/{orderId}/update")
    public String updateEmployee(@PathVariable Integer clientId,
                                 @PathVariable Integer orderId,
                                 @ModelAttribute Order order) {
        clientService.updateOrderToClient(clientId,orderId,order);
        return "redirect:/clients/%d/orders".formatted(clientId);
    }
}