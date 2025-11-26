package com.edrastx.pos.dao;

import com.edrastx.pos.model.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> findAll();
    Product findById(int id);
    Product findByCode(String code);
    void save(Product product);
    void update(Product product);
    void delete(int id);
}
