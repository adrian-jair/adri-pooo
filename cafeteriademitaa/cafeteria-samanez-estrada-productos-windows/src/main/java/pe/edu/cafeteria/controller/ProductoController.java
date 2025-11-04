package pe.edu.cafeteria.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.cafeteria.model.Producto;
import pe.edu.cafeteria.service.ProductoService;

public class ProductoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, String> colPrecio;
    @FXML private TableColumn<Producto, String> colStock;

    private final ProductoService service = new ProductoService();
    private final ObservableList<Producto> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getNombre()));
        colDescripcion.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(String.format("S/ %.2f", p.getValue().getPrecio())));
        colStock.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(String.valueOf(p.getValue().getStock())));

        tablaProductos.setItems(lista);

        // ejemplo de datos iniciales sencillos (puedes quitar si quieres)
        service.agregar(new Producto("Café Americano", "Taza 250ml", 4.50, 20));
        service.agregar(new Producto("Cappuccino", "Con leche espumada", 6.00, 15));
        lista.setAll(service.listar());
    }

    @FXML
    public void agregar() {
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y precio son obligatorios", ButtonType.OK);
            a.showAndWait();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Precio inválido", ButtonType.OK);
            a.showAndWait();
            return;
        }
        try {
            stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Stock inválido", ButtonType.OK);
            a.showAndWait();
            return;
        }

        Producto p = new Producto(nombre, desc, precio, stock);
        service.agregar(p);
        lista.add(p);
        limpiar();
    }

    @FXML
    public void eliminar() {
        Producto sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel != null) {
            service.eliminar(sel);
            lista.remove(sel);
        }
    }

    @FXML
    public void editar() {
        int idx = tablaProductos.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            String nombre = txtNombre.getText().trim();
            String desc = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String stockStr = txtStock.getText().trim();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y precio son obligatorios", ButtonType.OK);
                a.showAndWait();
                return;
            }

            double precio;
            int stock;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Precio inválido", ButtonType.OK);
                a.showAndWait();
                return;
            }
            try {
                stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);
            } catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Stock inválido", ButtonType.OK);
                a.showAndWait();
                return;
            }

            Producto p = new Producto(nombre, desc, precio, stock);
            service.editar(idx, p);
            lista.set(idx, p);
            limpiar();
        }
    }

    @FXML
    public void cargarSeleccion() {
        Producto sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel != null) {
            txtNombre.setText(sel.getNombre());
            txtDescripcion.setText(sel.getDescripcion());
            txtPrecio.setText(String.valueOf(sel.getPrecio()));
            txtStock.setText(String.valueOf(sel.getStock()));
        }
    }

    private void limpiar() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
    }
}
