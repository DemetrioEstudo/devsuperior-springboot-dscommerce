package br.com.klsys.dscommerce.repositories;

import br.com.klsys.dscommerce.entities.Order;
import br.com.klsys.dscommerce.entities.OrderItem;
import br.com.klsys.dscommerce.entities.OrderItemPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPk> {

}
