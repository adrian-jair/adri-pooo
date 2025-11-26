package com.edrastx.pos.dao;

import com.edrastx.pos.model.Client;

import java.util.List;

public interface IClientDAO {
    List<Client> findAll();
    Client findById(int id);
    Client findByName(String name);
    void save(Client client);
    void update(Client client);
    void delete(int id);
}
