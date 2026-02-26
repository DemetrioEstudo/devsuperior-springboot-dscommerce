package br.com.klsys.dscommerce.services;

import br.com.klsys.dscommerce.dto.OrderDTO;
import br.com.klsys.dscommerce.dto.OrderItemDTO;
import br.com.klsys.dscommerce.dto.ProductDTO;
import br.com.klsys.dscommerce.entities.*;
import br.com.klsys.dscommerce.repositories.OrderItemRepository;
import br.com.klsys.dscommerce.repositories.OrderRepository;
import br.com.klsys.dscommerce.repositories.ProductRepository;
import br.com.klsys.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {


    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthService authService;


    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Recurso nâo encontrado"));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional(readOnly = true)
    public  OrderDTO insert(OrderDTO dto) {
        Order order = new Order();

        order.setMoment(Instant.now());
        order.setOrderStatus(OrderStatus.WAITING_PAYMENT);
       User user = userService.authenticated();
        order.setClient(user);
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);

        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);

    }
}
