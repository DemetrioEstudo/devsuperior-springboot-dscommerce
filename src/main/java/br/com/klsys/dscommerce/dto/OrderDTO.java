package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.Order;
import br.com.klsys.dscommerce.entities.OrderItem;
import br.com.klsys.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus status;

    private ClientDTO  client;
    private PaymentDTO pyment;

    @NotEmpty (message = "A ordem deve conter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();


    public OrderDTO() {
    }

    public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO pyment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.pyment = pyment;
    }

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.status = entity.getOrderStatus();
        this.client = new ClientDTO(entity.getClient());
        if (entity.getPayment() != null) {
            this.pyment = new PaymentDTO(entity.getPayment());
        }
        for(OrderItem item : entity.getItems()){
            OrderItemDTO itemDTO = new OrderItemDTO(item);
            items.add(itemDTO);
        }

    }


    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPyment() {
        return pyment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal(){
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }

}
