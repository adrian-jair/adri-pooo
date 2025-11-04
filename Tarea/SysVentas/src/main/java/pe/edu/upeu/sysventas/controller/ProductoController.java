package pe.edu.upeu.sysventas.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.ComboBoxAutoComplete;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.service.ICategoriaService;
import pe.edu.upeu.sysventas.service.IMarcaService;
import pe.edu.upeu.sysventas.service.IUnidadMedidaService;
import pe.edu.upeu.sysventas.service.ProductoIService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class ProductoController {

    // ----- FXML fields -----
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtPUnit;
    @FXML
    private TextField txtPUnitOld;
    @FXML private TextField txtUtilidad;
    @FXML private TextField txtStock;
    @FXML private TextField txtStockOld;
    @FXML private TextField txtFiltroDato;

    @FXML private ComboBox<ComboBoxOption> cbxMarca;
    @FXML private ComboBox<ComboBoxOption> cbxCategoria;
    @FXML private ComboBox<ComboBoxOption> cbxUnidMedida;

    @FXML private TableView<Producto> tableView;
    @FXML private Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    // ----- DI / state -----
    private Stage stage;
    @Autowired private IMarcaService ms;
    @Autowired private ICategoriaService cs;
    @Autowired private ProductoIService ps;
    @Autowired private IUnidadMedidaService ums;

    private Validator validator;
    private ObservableList<Producto> listarProducto;
    private Producto formulario;
    private Long idProductoCE = 0L;

    // ----- initialize -----
    @FXML
    public void initialize() {
        // --- Defensive checks for FXML injection (useful while debugging) ---
        if (txtPUnit == null) {
            System.err.println("WARN: txtPUnit no fue inyectado desde el FXML (fx:id=\"txtPUnit\").");
        }
        if (txtNombreProducto == null) {
            System.err.println("WARN: txtNombreProducto no fue inyectado desde el FXML (fx:id=\"txtNombreProducto\").");
        }

        // Validator (si no se inyecta via Spring)
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        } catch (Exception e) {
            System.err.println("No se pudo crear Validator: " + e.getMessage());
            validator = null;
        }

        // Esperar a que la escena esté lista para obtener el Stage
        Platform.runLater(() -> {
            if (miContenedor != null && miContenedor.getScene() != null) {
                stage = (Stage) miContenedor.getScene().getWindow();
            }
        });

        // Inicializar combos y tabla (si los servicios están disponibles)
        inicializarCombos();
        inicializarTabla();
        // Listener para filtro (si el TextField existe)
        if (txtFiltroDato != null) {
            txtFiltroDato.textProperty().addListener((obs, oldV, newV) -> filtrarProductos(newV));
        }
        // Cargar lista inicial
        listar();
    }

    // ----- Combos -----
    private void inicializarCombos() {
        try {
            if (cbxMarca != null && ms != null) {
                cbxMarca.setItems(FXCollections.observableArrayList(ms.listarCombobox()));
                new ComboBoxAutoComplete<>(cbxMarca);
            }
            if (cbxCategoria != null && cs != null) {
                cbxCategoria.setItems(FXCollections.observableArrayList(cs.listarCombobox()));
                new ComboBoxAutoComplete<>(cbxCategoria);
            }
            if (cbxUnidMedida != null && ums != null) {
                cbxUnidMedida.setItems(FXCollections.observableArrayList(ums.listarCombobox()));
                new ComboBoxAutoComplete<>(cbxUnidMedida);
            }
        } catch (Exception e) {
            System.err.println("Error inicializando combos: " + e.getMessage());
        }
    }

    // ----- Tabla -----
    private void inicializarTabla() {
        try {
            TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
            LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
            columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0));
            columns.put("Nombre Producto", new ColumnInfo("nombre", 200.0));
            columns.put("P. Unitario", new ColumnInfo("pu", 150.0));
            columns.put("Utilidad", new ColumnInfo("utilidad", 100.0));
            columns.put("Marca", new ColumnInfo("marca.nombre", 150.0));
            columns.put("Categoria", new ColumnInfo("categoria.nombre", 150.0));

            Consumer<Producto> updateAction = this::editForm;
            Consumer<Producto> deleteAction = producto -> {
                if (producto == null) return;
                try {
                    ps.delete(producto.getIdProducto());
                    if (stage != null) {
                        double w = stage.getWidth() / 1.5;
                        double h = stage.getHeight() / 2;
                        Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
                    }
                    listar();
                } catch (Exception e) {
                    System.err.println("Error eliminando producto: " + e.getMessage());
                }
            };

            if (tableView != null) {
                tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
                tableView.setTableMenuButtonVisible(true);
            }
        } catch (Exception e) {
            System.err.println("Error inicializando tabla: " + e.getMessage());
        }
    }

    // ----- Listar y filtrar -----
    public void listar() {
        try {
            if (ps == null) {
                System.err.println("Servicio ProductoIService (ps) no disponible.");
                return;
            }
            List<Producto> encontrados = ps.findAll();
            listarProducto = FXCollections.observableArrayList(encontrados == null ? Collections.emptyList() : encontrados);
            if (tableView != null) {
                tableView.getItems().setAll(listarProducto);
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    private void filtrarProductos(String filtro) {
        if (listarProducto == null || tableView == null) return;
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().setAll(listarProducto);
            return;
        }
        String lower = filtro.toLowerCase();
        List<Producto> filtrados = listarProducto.stream()
                .filter(p -> {
                    if (p == null) return false;
                    boolean okNombre = p.getNombre() != null && p.getNombre().toLowerCase().contains(lower);
                    boolean okPu = String.valueOf(p.getPu()).contains(lower);
                    boolean okUtil = String.valueOf(p.getUtilidad()).contains(lower);
                    boolean okMarca = p.getMarca() != null && p.getMarca().getNombre() != null && p.getMarca().getNombre().toLowerCase().contains(lower);
                    boolean okCat = p.getCategoria() != null && p.getCategoria().getNombre() != null && p.getCategoria().getNombre().toLowerCase().contains(lower);
                    return okNombre || okPu || okUtil || okMarca || okCat;
                })
                .collect(Collectors.toList());
        tableView.getItems().setAll(filtrados);
    }

    // ----- Utilitarios -----
    private double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ----- Limpiar / Clear -----
    @FXML
    public void clearForm() {
        if (txtNombreProducto != null) txtNombreProducto.clear();
        if (txtPUnit != null) txtPUnit.clear();
        if (txtPUnitOld != null) txtPUnitOld.clear();
        if (txtUtilidad != null) txtUtilidad.clear();
        if (txtStock != null) txtStock.clear();
        if (txtStockOld != null) txtStockOld.clear();
        if (cbxMarca != null) cbxMarca.getSelectionModel().clearSelection();
        if (cbxCategoria != null) cbxCategoria.getSelectionModel().clearSelection();
        if (cbxUnidMedida != null) cbxUnidMedida.getSelectionModel().clearSelection();
        idProductoCE = 0L;
        limpiarError();
        if (lbnMsg != null) lbnMsg.setText("");
    }

    public void limpiarError() {
        List<Control> controles = List.of(
                txtNombreProducto, txtPUnit, txtPUnitOld, txtUtilidad, txtStock, txtStockOld,
                cbxMarca, cbxCategoria, cbxUnidMedida
        );
        controles.forEach(c -> {
            if (c != null) c.getStyleClass().remove("text-field-error");
        });
    }

    // ----- Editar -----
    public void editForm(Producto producto) {
        if (producto == null) return;
        formulario = producto;
        if (txtNombreProducto != null) txtNombreProducto.setText(Optional.ofNullable(producto.getNombre()).orElse(""));
        if (txtPUnit != null) txtPUnit.setText(String.valueOf(producto.getPu()));
        if (txtPUnitOld != null) txtPUnitOld.setText(String.valueOf(producto.getPuOld()));
        if (txtUtilidad != null) txtUtilidad.setText(String.valueOf(producto.getUtilidad()));
        if (txtStock != null) txtStock.setText(String.valueOf(producto.getStock()));
        if (txtStockOld != null) txtStockOld.setText(String.valueOf(producto.getStockOld()));

        // seleccionar opciones en comboboxes (si están cargados)
        if (cbxMarca != null && producto.getMarca() != null) {
            seleccionarComboPorId(cbxMarca, producto.getMarca().getIdMarca());
        }
        if (cbxCategoria != null && producto.getCategoria() != null) {
            seleccionarComboPorId(cbxCategoria, producto.getCategoria().getIdCategoria());
        }
        if (cbxUnidMedida != null && producto.getUnidadMedida() != null) {
            seleccionarComboPorId(cbxUnidMedida, producto.getUnidadMedida().getIdUnidad());
        }
        idProductoCE = producto.getIdProducto();
        limpiarError();
    }

    private void seleccionarComboPorId(ComboBox<ComboBoxOption> combo, Long id) {
        if (combo == null || id == null) return;
        combo.getItems().stream()
                .filter(opt -> {
                    try {
                        return Long.parseLong(opt.getKey()) == id;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .ifPresent(opt -> combo.getSelectionModel().select(opt));
    }

    // ----- Validación y procesamiento -----
    @FXML
    public void validarFormulario() {
        // Protección: comprobar que los campos estén inyectados antes de usar getText()
        if (txtNombreProducto == null || txtPUnit == null || txtUtilidad == null) {
            if (lbnMsg != null) {
                lbnMsg.setText("Error interno: campos del formulario no están vinculados. Revise el FXML.");
                lbnMsg.setStyle("-fx-text-fill: red;");
            }
            System.err.println("ERROR: Campos FXML no inyectados (txtNombreProducto o txtPUnit o txtUtilidad).");
            return;
        }

        formulario = new Producto();
        formulario.setNombre(txtNombreProducto.getText());
        formulario.setPuOld(parseDoubleSafe(txtPUnitOld != null ? txtPUnitOld.getText() : null));
        formulario.setPu(parseDoubleSafe(txtPUnit.getText()));
        formulario.setUtilidad(parseDoubleSafe(txtUtilidad.getText()));
        formulario.setStock(parseDoubleSafe(txtStock != null ? txtStock.getText() : null));
        formulario.setStockOld(parseDoubleSafe(txtStockOld != null ? txtStockOld.getText() : null));

        // asignar relaciones (con comprobaciones)
        try {
            String idxM = (cbxMarca != null && cbxMarca.getSelectionModel().getSelectedItem() != null) ?
                    cbxMarca.getSelectionModel().getSelectedItem().getKey() : "0";
            formulario.setMarca("0".equals(idxM) ? null : ms.findById(Long.parseLong(idxM)));
        } catch (Exception e) {
            formulario.setMarca(null);
        }

        try {
            String idxC = (cbxCategoria != null && cbxCategoria.getSelectionModel().getSelectedItem() != null) ?
                    cbxCategoria.getSelectionModel().getSelectedItem().getKey() : "0";
            formulario.setCategoria("0".equals(idxC) ? null : cs.findById(Long.parseLong(idxC)));
        } catch (Exception e) {
            formulario.setCategoria(null);
        }

        try {
            String idxUM = (cbxUnidMedida != null && cbxUnidMedida.getSelectionModel().getSelectedItem() != null) ?
                    cbxUnidMedida.getSelectionModel().getSelectedItem().getKey() : "0";
            formulario.setUnidadMedida("0".equals(idxUM) ? null : ums.findById(Long.parseLong(idxUM)));
        } catch (Exception e) {
            formulario.setUnidadMedida(null);
        }

        // Validar con Jakarta Validation si está disponible
        if (validator != null) {
            Set<ConstraintViolation<Producto>> violaciones = validator.validate(formulario);
            if (violaciones.isEmpty()) {
                procesarFormulario();
            } else {
                mostrarErroresValidacion(new ArrayList<>(violaciones));
            }
        } else {
            // Si no hay validator, procesar igual (opcional)
            procesarFormulario();
        }
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<Producto>> violaciones) {
        limpiarError();

        // mapa campo -> control
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("nombre", txtNombreProducto);
        campos.put("pu", txtPUnit);
        campos.put("puOld", txtPUnitOld);
        campos.put("utilidad", txtUtilidad);
        campos.put("stock", txtStock);
        campos.put("stockOld", txtStockOld);
        campos.put("marca", cbxMarca);
        campos.put("categoria", cbxCategoria);
        campos.put("unidadMedida", cbxUnidMedida);

        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerControlConError = {null};

        for (String campo : campos.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst()
                    .ifPresent(v -> {
                        erroresOrdenados.put(campo, v.getMessage());
                        Control control = campos.get(campo);
                        if (control != null && !control.getStyleClass().contains("text-field-error")) {
                            control.getStyleClass().add("text-field-error");
                        }
                        if (primerControlConError[0] == null) primerControlConError[0] = control;
                    });
        }

        if (!erroresOrdenados.isEmpty()) {
            var primerError = erroresOrdenados.entrySet().iterator().next();
            if (lbnMsg != null) {
                lbnMsg.setText(primerError.getValue());
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
            if (primerControlConError[0] != null) {
                Control finalControl = primerControlConError[0];
                Platform.runLater(finalControl::requestFocus);
            }
        }
    }

    private void procesarFormulario() {
        if (lbnMsg != null) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        }

        try {
            if (idProductoCE != null && idProductoCE > 0L) {
                formulario.setIdProducto(idProductoCE);
                ps.update(formulario);
                if (stage != null) Toast.showToast(stage, "Se actualizó correctamente!!", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
            } else {
                ps.save(formulario);
                if (stage != null) Toast.showToast(stage, "Se guardó correctamente!!", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
            }
        } catch (Exception e) {
            System.err.println("Error guardando producto: " + e.getMessage());
            if (stage != null) Toast.showToast(stage, "Error al guardar producto", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }

        clearForm();
        listar();
    }
}
