package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.Client;

import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderService orderService;

    @Autowired
    public ClientService(ClientRepository clientRepository, OrderService orderService) {
        this.clientRepository = clientRepository;
        this.orderService = orderService;
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    public Client getClientById(Integer id) {
        return clientRepository.getClientById(id)
                .orElseThrow(() -> new NoSuchElementException("Client is not found with id = '%d')".formatted(id)));
    }
    public Client getClientByName(String name) {
        return clientRepository.getClientByName(name)
                .orElseThrow(() -> new NoSuchElementException("Client is not found with name = '%s')".formatted(name)));
    }

    public Client getClientWithCart(String name) {
        return clientRepository.getClientWithCart(name)
                .orElseThrow(() -> new NoSuchElementException("Client is not found with name = '%s')".formatted(name)));
    }

    public List<Order> getAllClientOrders(String name) {
        return clientRepository.getAllOrdersByClientName(name);
    }
//    public List<Order> getAllClientOrders(Integer clientId) {
//        return clientRepository.getAllOrdersByClientId(clientId);
//    }

    public void saveClient(Client client) {
        client.setCart(new Cart());
        clientRepository.saveClient(client);
    }

    public void addOrderToClient(String name, Order order) {
        clientRepository.getClientByName(name)
                .ifPresent(client -> {
                    client.setOrders(List.of(order));
                    clientRepository.saveClient(client);
                });
    }

//    public void updateOrderToClient(Integer clientId,Integer orderId ,Order order) {
//       if (clientId != null && orderId != null) {
//           clientRepository.getClientById(clientId)
//                   .ifPresent(client -> {
//                       orderService.updateOrder(orderId, order);
//                       client.updateOrder(orderId,order);
//                   });
//       }
//    }
}