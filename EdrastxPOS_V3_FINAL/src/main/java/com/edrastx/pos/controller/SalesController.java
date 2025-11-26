package com.edrastx.pos.controller;

import com.edrastx.pos.dao.ClientDAO;
import com.edrastx.pos.dao.ProductDAO;
import com.edrastx.pos.dao.SalesDAO;
import com.edrastx.pos.model.Client;
import com.edrastx.pos.model.Product;
import com.edrastx.pos.model.Sale;
import com.edrastx.pos.model.SaleItem;
import com.edrastx.pos.util.SessionManager;
import com.edrastx.pos.util.TicketPrinter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class SalesController {
    @FXML
    private ComboBox<Client> cbClient;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TableView<SaleItem> tblCart;
    @FXML
    private TableColumn<SaleItem, Integer> colQty;
    @FXML
    private TableColumn<SaleItem, String> colProduct;
    @FXML
    private TableColumn<SaleItem, Double> colPrice;
    @FXML
    private TableColumn<SaleItem, Double> colSubtotal;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnTestPrint;

    private final ProductDAO productDAO = new ProductDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final SalesDAO salesDAO = new SalesDAO();
    private final ObservableList<SaleItem> cart = FXCollections.observableArrayList();
    private double total;

    @FXML
    private void initialize() {
        cbClient.setItems(FXCollections.observableArrayList(clientDAO.findAll()));
        txtQuantity.setText("1");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblCart.setItems(cart);
        updateTotal();
    }

    @FXML
    private void onAdd() {
        String code = txtProductCode.getText();
        if (code == null || code.isBlank()) {
            return;
        }
        Product p = productDAO.findByCode(code);
        if (p == null) {
            return;
        }
        int qty;
        try {
            qty = Integer.parseInt(txtQuantity.getText());
        } catch (NumberFormatException e) {
            return;
        }
        if (qty <= 0) {
            return;
        }
        SaleItem item = new SaleItem();
        item.setProductId(p.getId());
        item.setQuantity(qty);
        item.setPrice(p.getPrice());
        item.setSubtotal(p.getPrice() * qty);
        item.setProductName(p.getName());
        cart.add(item);
        txtProductCode.clear();
        txtQuantity.setText("1");
        updateTotal();
    }

    @FXML
    private void onRemove() {
        SaleItem selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cart.remove(selected);
            updateTotal();
        }
    }

    @FXML
    private void onClear() {
        cart.clear();
        updateTotal();
    }

    @FXML
    private void onConfirm() {
        if (cart.isEmpty()) {
            return;
        }
        Client client = cbClient.getSelectionModel().getSelectedItem();
        String nameFromText = txtCustomerName.getText();
        String finalName;
        int clientId;
        if (client != null) {
            clientId = client.getId();
            finalName = client.getName();
        } else {
            clientId = 0;
            finalName = nameFromText;
        }
        Sale sale = new Sale();
        sale.setClientId(clientId);
        sale.setCustomerName(finalName);
        LocalDateTime now = LocalDateTime.now();
        sale.setDateTime(now);
        sale.setTotal(total);
        sale.setUser(SessionManager.getCurrentUser());
        int saleId = salesDAO.saveSale(sale, cart);
        TicketPrinter.printTicket(String.valueOf(saleId), SessionManager.getCurrentUser(), now, finalName, cart, total);
        cart.clear();
        updateTotal();
        txtCustomerName.clear();
        cbClient.getSelectionModel().clearSelection();
    }

    @FXML
    private void onTestPrint() {
        TicketPrinter.printTicket("TEST", SessionManager.getCurrentUser(), LocalDateTime.now(), "Prueba", cart, total);
    }

    private void updateTotal() {
        total = cart.stream().mapToDouble(SaleItem::getSubtotal).sum();
        lblTotal.setText(String.format("Total: %.2f", total));
    }
}
