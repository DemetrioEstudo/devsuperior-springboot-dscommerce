package br.com.klsys.dscommerce.services;

import br.com.klsys.dscommerce.dto.OrderDTO;
import br.com.klsys.dscommerce.dto.ProductDTO;
import br.com.klsys.dscommerce.entities.Order;
import br.com.klsys.dscommerce.entities.Product;
import br.com.klsys.dscommerce.repositories.OrderRepository;
import br.com.klsys.dscommerce.repositories.ProductRepository;
import br.com.klsys.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {


    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Recurso nâo encontrado"));
        return new OrderDTO(order);
    }



}
