package com.rabit.consumer.repo;

import com.rabit.consumer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepo extends JpaRepository<Product,Long> {
}
