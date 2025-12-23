package br.com.klsys.dscommerce.repositories;

import br.com.klsys.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



}
