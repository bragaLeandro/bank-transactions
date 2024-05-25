package br.com.ibm.service;


import br.com.ibm.dto.ProductDto;
import br.com.ibm.entity.Product;
import br.com.ibm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    ProductService (ProductRepository productRepository,
                    ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public void createProduct(ProductDto productDto) {
       Product product = modelMapper.map(productDto, Product.class);
       productRepository.save(product);
    }

    public void inactivateProduct(UUID uuid) {
        Product product = this.findById(uuid);

        if (!product.isEnable()) {
            throw new IllegalArgumentException("Product is already disabled");
        }

        product.setEnable(false);

        productRepository.save(product);
    }

    public void activateProduct(UUID uuid) {
        Product product = this.findById(uuid);

        if (product.isEnable()) {
            throw new IllegalArgumentException("Product is already enabled");
        }

        product.setEnable(true);

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