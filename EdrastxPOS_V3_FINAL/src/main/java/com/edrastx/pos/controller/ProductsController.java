package com.edrastx.pos.controller;

import com.edrastx.pos.dao.ProductDAO;
import com.edrastx.pos.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductsController {
    @FXML
    private TableView<Product> tblProducts;
    @FXML
    private TableColumn<Product, Integer> colId;
    @FXML
    private TableColumn<Product, String> colCode;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStock;
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtStock;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnDelete;

    private final ProductDAO dao = new ProductDAO();
    private final ObservableList<Product> data = FXCollections.observableArrayList();
    private Product current;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tblProducts.setItems(data);
        tblProducts.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                current = n;
                txtCode.setText(n.getCode());
                txtName.setText(n.getName());
                txtPrice.setText(String.valueOf(n.getPrice()));
                txtStock.setText(String.valueOf(n.getStock()));
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
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtStock.clear();
    }

    @FXML
    private void onNew() {
        clearForm();
        tblProducts.getSelectionModel().clearSelection();
    }

    @FXML
    private void onSave() {
        if (current == null) {
            Product p = new Product();
            p.setCode(txtCode.getText());
            p.setName(txtName.getText());
            p.setPrice(Double.parseDouble(txtPrice.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));
            dao.save(p);
        } else {
            current.setCode(txtCode.getText());
            current.setName(txtName.getText());
            current.setPrice(Double.parseDouble(txtPrice.getText()));
            current.setStock(Integer.parseInt(txtStock.getText()));
            dao.update(current);
        }
        loadData();
    }

    @FXML
    private void onDelete() {
        Product selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId());
            loadData();
        }
    }
}
