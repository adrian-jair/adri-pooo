package com.edrastx.pos.controller;

import com.edrastx.pos.dao.ClientDAO;
import com.edrastx.pos.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientsController {
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, String> colDocument;
    @FXML
    private TableColumn<Client, String> colPhone;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDocument;
    @FXML
    private TextField txtPhone;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnDelete;

    private final ClientDAO dao = new ClientDAO();
    private final ObservableList<Client> data = FXCollections.observableArrayList();
    private Client current;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDocument.setCellValueFactory(new PropertyValueFactory<>("document"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tblClients.setItems(data);
        tblClients.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                current = n;
                txtName.setText(n.getName());
                txtDocument.setText(n.getDocument());
                txtPhone.setText(n.getPhone());
            }
        });
        loadData();
    }

    private void loadData() {
        data.clear();
        data.addAll(dao.findAll());
        clearForm();
    }

    private void clearForm() {
        current = null;
        txtName.clear();
        txtDocument.clear();
        txtPhone.clear();
    }

    @FXML
    private void onNew() {
        clearForm();
        tblClients.getSelectionModel().clearSelection();
    }

    @FXML
    private void onSave() {
        if (current == null) {
            Client c = new Client();
            c.setName(txtName.getText());
            c.setDocument(txtDocument.getText());
            c.setPhone(txtPhone.getText());
            dao.save(c);
        } else {
            current.setName(txtName.getText());
            current.setDocument(txtDocument.getText());
            current.setPhone(txtPhone.getText());
            dao.update(current);
        }
        loadData();
    }

    @FXML
    private void onDelete() {
        Client selected = tblClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId());
            loadData();
        }
    }
}
