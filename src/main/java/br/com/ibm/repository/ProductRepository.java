package br.com.ibm.repository;

import br.com.ibm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findProductByName(String name);
}