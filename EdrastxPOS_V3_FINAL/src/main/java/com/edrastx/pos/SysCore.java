package com.edrastx.pos;

import com.edrastx.pos.dao.ProductDAO;
import com.edrastx.pos.model.Product;
import com.edrastx.pos.util.DBUtil;

import java.util.List;

public class SysCore {
    public static void main(String[] args) {
        DBUtil.init();
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.findAll();
        System.out.println("Productos registrados:");
        for (Product p : products) {
            System.out.println(p.getCode() + " - " + p.getName() + " : " + p.getPrice() + " (" + p.getStock() + ")");
        }
    }
}
