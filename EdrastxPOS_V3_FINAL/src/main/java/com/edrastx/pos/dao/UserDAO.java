package com.edrastx.pos.dao;

import com.edrastx.pos.model.User;
import com.edrastx.pos.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public User login(String username, String password) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,username,password,role from users where username=? and password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
