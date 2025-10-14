package pe.edu.upeu.asistencia.control;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.EstadoEvento;
import pe.edu.upeu.asistencia.enums.TipoEvento;
import pe.edu.upeu.asistencia.modelo.Evento;
import pe.edu.upeu.asistencia.servicio.EventoServicioI;


@Controller
public class EventoController {
    @Autowired EventoServicioI es;

    @FXML private Spinner<Integer> spnHora, spnMinut, spnCantidad;
    @FXML private TextField txtNombreEvent;
    @FXML private DatePicker txtFecha;
    @FXML private ComboBox<TipoEvento> cbxTipoEvento;
    @FXML private ComboBox<EstadoEvento> cbxEstadoEvento;

    @FXML private TableView<Evento> tableView;
    ObservableList<Evento> eventos;
    private TableColumn<Evento, String> codEventColumn, nombreColumn, tipoEventColumn, estadoEventColumn, fechaColumn, horaColumn, cantRegColumn;

    private TableColumn<Evento, Void> opcColumn;
    private int indexEdit=-1;



    @FXML private void initialize(){
        // Configurar los spinners
        spnHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        spnHora.setEditable(true);
        spnMinut.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 30));
        spnMinut.setEditable(true);
        spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
        spnCantidad.setEditable(true);
        cbxTipoEvento.getItems().setAll(TipoEvento.values());
        cbxEstadoEvento.getItems().setAll(EstadoEvento.values());

        definirColumnas();
        listar();
    }

    public void definirColumnas(){
        codEventColumn = new TableColumn<>("Cod. Evento");
        nombreColumn = new TableColumn<>("Nombre");
        tipoEventColumn = new TableColumn<>("Tipo Evento");
        estadoEventColumn = new TableColumn<>("Estado Evento");
        fechaColumn = new TableColumn<>("Fecha");
        horaColumn = new TableColumn<>("Hora");
        cantRegColumn = new TableColumn<>("Cant. Reg");
        opcColumn = new TableColumn<>("Opciones");
        opcColumn.setMinWidth(180);
        tableView.getColumns().addAll(codEventColumn, nombreColumn, tipoEventColumn, estadoEventColumn, fechaColumn,horaColumn, cantRegColumn, opcColumn);
    }

    private void listar(){
        codEventColumn.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getCodigoEvento().toString()));
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreEvento()));
        tipoEventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo().name()));
        estadoEventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().name()));
        fechaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha().toString()));
        horaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHora().toString()));
        cantRegColumn.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getCantRegistro())));

        addActionButtonsToTable();
        eventos = FXCollections.observableArrayList(es.findAll());

        tableView.setItems(eventos);
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<Evento, Void>, TableCell<Evento, Void>>
                cellFactory = param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            {
                editButton.setOnAction(event -> {
                    Evento p = getTableView().getItems().get(getIndex());
                    editar(p, getIndex());
                });
                deleteButton.getStyleClass().setAll("button", "delete-button");
                deleteButton.setOnAction(event -> {
                    eliminar(getIndex());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, deleteButton);
                    buttons.setSpacing(10);
                    setGraphic(buttons);
                }
            }
        };
        opcColumn.setCellFactory(cellFactory);
    }

    @FXML
    public void editar(Evento p, int index){
        txtNombreEvent.setText(p.getNombreEvento());
        txtFecha.setValue(p.getFecha());
        spnHora.getValueFactory().setValue(p.getHora().getHour());
        spnMinut.getValueFactory().setValue(p.getHora().getMinute());
        cbxTipoEvento.getSelectionModel().select(p.getTipo());
        cbxEstadoEvento.getSelectionModel().select(p.getEstado());
        spnCantidad.getValueFactory().setValue(p.getCantRegistro());
        indexEdit=index;
    }

    @FXML
    public void eliminar(int index){
        es.delete(index);
        listar();
    }

    public void limpiarFormulario(){
        txtNombreEvent.clear();
        txtFecha.setValue(null);
        spnHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        spnMinut.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 30));
        cbxTipoEvento.getSelectionModel().clearSelection();
        cbxEstadoEvento.getSelectionModel().clearSelection();
        spnCantidad.getValueFactory().setValue(1);
    }

    @FXML
    public void registrar(){

        Evento evento=new Evento();
        evento.setCodigoEvento("EV"+ (String.format("%03d", eventos.size()+1)));
        evento.setNombreEvento(txtNombreEvent.getText());
        evento.setTipo(cbxTipoEvento.getValue());
        evento.setEstado(cbxEstadoEvento.getValue());
        evento.setFecha(txtFecha.getValue());
        evento.setHora(java.time.LocalTime.of(spnHora.getValue(), spnMinut.getValue()));
        evento.setCantRegistro(spnCantidad.getValue());
        if(indexEdit!=-1){
            es.update(evento, indexEdit);
            indexEdit=-1;
        }else{
            es.save(evento);
        }
        limpiarFormulario();
        listar();
    }
}
