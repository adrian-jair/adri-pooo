package com.edrastx.pos.dao;

import com.edrastx.pos.model.Client;
import com.edrastx.pos.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {
    @Override
    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,name,document,phone from clients order by name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDocument(rs.getString("document"));
                c.setPhone(rs.getString("phone"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Client findById(int id) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,name,document,phone from clients where id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDocument(rs.getString("document"));
                c.setPhone(rs.getString("phone"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client findByName(String name) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,name,document,phone from clients where name=?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDocument(rs.getString("document"));
                c.setPhone(rs.getString("phone"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Client client) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("insert into clients(name,document,phone) values(?,?,?)")) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getDocument());
            ps.setString(3, client.getPhone());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Client client) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("update clients set name=?,document=?,phone=? where id=?")) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getDocument());
            ps.setString(3, client.getPhone());
            ps.setInt(4, client.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from clients where id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
