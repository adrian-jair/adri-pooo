package com.edrastx.pos.dao;

import com.edrastx.pos.model.Sale;
import com.edrastx.pos.model.SaleItem;
import com.edrastx.pos.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO implements ISalesDAO {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    public int saveSale(Sale sale, List<SaleItem> items) {
        int saleId = 0;
        String insertSale = "insert into sales(date_time,client_id,customer_name,total,user) values(?,?,?,?,?)";
        String insertItem = "insert into sale_items(sale_id,product_id,quantity,price,subtotal) values(?,?,?,?,?)";
        try (Connection cn = DBUtil.getConnection()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psSale = cn.prepareStatement(insertSale, Statement.RETURN_GENERATED_KEYS)) {
                psSale.setString(1, sale.getDateTime().toString());
                psSale.setInt(2, sale.getClientId());
                psSale.setString(3, sale.getCustomerName());
                psSale.setDouble(4, sale.getTotal());
                psSale.setString(5, sale.getUser());
                psSale.executeUpdate();
                ResultSet rs = psSale.getGeneratedKeys();
                if (rs.next()) {
                    saleId = rs.getInt(1);
                }
            }
            try (PreparedStatement psItem = cn.prepareStatement(insertItem)) {
                for (SaleItem item : items) {
                    psItem.setInt(1, saleId);
                    psItem.setInt(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getPrice());
                    psItem.setDouble(5, item.getSubtotal());
                    psItem.addBatch();
                    productDAO.updateStock(item.getProductId(), item.getQuantity());
                }
                psItem.executeBatch();
            }
            cn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saleId;
    }

    @Override
    public List<Sale> findByDateRange(LocalDateTime from, LocalDateTime to) {
        List<Sale> list = new ArrayList<>();
        String sql = "select id,date_time,client_id,customer_name,total,user from sales where date_time between ? and ? order by date_time asc";
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, from.toString());
            ps.setString(2, to.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getInt("id"));
                s.setDateTime(LocalDateTime.parse(rs.getString("date_time")));
                s.setClientId(rs.getInt("client_id"));
                s.setCustomerName(rs.getString("customer_name"));
                s.setTotal(rs.getDouble("total"));
                s.setUser(rs.getString("user"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
