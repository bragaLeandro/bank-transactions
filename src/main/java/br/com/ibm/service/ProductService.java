package br.com.ibm.service;


import br.com.ibm.entity.Product;
import br.com.ibm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    ProductService (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public void inactivateProduct(UUID uuid) {
        Product product = this.findById(uuid);

        if (!product.isEnabled()) {
            throw new IllegalArgumentException("Product is already disabled");
        }

        product.setEnabled(false);

        productRepository.save(product);
    }

    public void activateProduct(UUID uuid) {
        Product product = this.findById(uuid);

        if (product.isEnabled()) {
            throw new IllegalArgumentException("Product is already enabled");
        }

        product.setEnabled(true);

        productRepository.save(product);
    }

    public Product findById(UUID uuid) {
        return productRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product findByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product " + name + " not found"));
    }
}