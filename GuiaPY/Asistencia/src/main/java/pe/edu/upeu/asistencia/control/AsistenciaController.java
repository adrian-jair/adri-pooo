package pe.edu.upeu.asistencia.control;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.dto.ComboBoxOption;
import pe.edu.upeu.asistencia.modelo.Asistencia;
import pe.edu.upeu.asistencia.modelo.Evento;
import pe.edu.upeu.asistencia.servicio.AsistenciaServicioI;
import pe.edu.upeu.asistencia.servicio.EventoServicioI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AsistenciaController {
    @Autowired
    private EventoServicioI esi;
    @Autowired
    private AsistenciaServicioI asi;

    @FXML private ComboBox<ComboBoxOption> cbxEvento;
    @FXML private Label msgNombreEvento, msgFechaActual, msgHoraActual, msgNombreParticipante;
    @FXML private TextField txtDni;
    @FXML private Button btnBuscar;
    String codigoEvento;

    DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<Asistencia> tableView;
    ObservableList<Asistencia> asistencias;
    private TableColumn<Asistencia, String> dniCol, codEventCol, fechaColumn, horaColumn, presenteColumn;


    @FXML private void initialize(){
        esi.findAll();
        cbxEvento.getItems().addAll(esi.listarCombobox());

        msgFechaActual.setText(LocalDate.now().format(fechaFmt));
        msgHoraActual.setText(LocalTime.now().format(horaFmt));
        setMsgHoraActual();
        definirColumnas();

        btnBuscar.setOnAction(e->{
            msgNombreEvento.setText(cbxEvento.getSelectionModel().getSelectedItem().getValue());
            codigoEvento=cbxEvento.getSelectionModel().getSelectedItem().getKey();
            listar();
        });

        txtDni.setOnAction(e -> {
            String dni = txtDni.getText().trim();
            if (!dni.isEmpty()) {
                guardar();
                txtDni.clear();
                listar();
                txtDni.requestFocus();
            }
        });

    }

    public void guardar(){
        Asistencia a=new Asistencia();
        a.setCodigoEvento(codigoEvento);
        a.setDni(txtDni.getText());
        a.setFechaHora(LocalDate.now().atTime(LocalTime.now()));
        a.setPresente("P");
        asi.save(a);
    }

    public void setMsgHoraActual(){
        Timeline reloj = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    msgFechaActual.setText("Fecha: " + LocalDate.now().format(fechaFmt));
                    msgHoraActual.setText("Hora: " + LocalTime.now().format(horaFmt));
                })
        );
        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }
    public void definirColumnas(){
        dniCol = new TableColumn<>("DNI");
        codEventCol = new TableColumn<>("Cod. Evento");
        fechaColumn = new TableColumn<>("Fecha");
        horaColumn = new TableColumn<>("Hora");
        presenteColumn = new TableColumn<>("Asistencia");
        tableView.getColumns().addAll(dniCol, codEventCol, fechaColumn, horaColumn,  presenteColumn);
    }

    private void listar(){
        dniCol.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getDni()));
        codEventCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoEvento()));
        fechaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(fechaFmt.format(cellData.getValue().getFechaHora())));
        horaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(horaFmt.format(cellData.getValue().getFechaHora())));
        presenteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPresente()));

        asistencias = FXCollections.observableArrayList(asi.buscarXCodEvent(codigoEvento));

        tableView.setItems(asistencias);
    }
}
