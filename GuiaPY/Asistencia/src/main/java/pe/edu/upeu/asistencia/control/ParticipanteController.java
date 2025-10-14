package pe.edu.upeu.asistencia.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.dto.PersonaDto;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

import java.util.Set;

@Controller
public class ParticipanteController {
    @FXML
    private ComboBox<Carrera> cbxCarrera;
    @FXML
    private ComboBox<TipoParticipante> cbxTipoParticipante;
    @FXML
    private TextField txtDni, txtNombres, txtApellidos;

    ObservableList<Participante> participantes;
    @FXML
    TableView<Participante> tableRegPart;
    private Validator validator;

    @Autowired
    ParticipanteServicioI psi;

    TableColumn<Participante, Void> opcColumn;
    TableColumn<Participante, String> dniColumn, nombreColumn, apellidosColumn, carreraColumn, tipoPartiColumn;
    private int indexEdit=-1;

    @FXML
    public void initialize() {
        //--------------------inicio validacion--------------
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        // Limpiar estilos de error al escribir en los campos
        txtDni.textProperty().addListener((obs, oldV, newV) -> clearErrorStyle(txtDni));
        txtNombres.textProperty().addListener((obs, oldV, newV) -> clearErrorStyle(txtNombres));
        txtApellidos.textProperty().addListener((obs, oldV, newV) -> clearErrorStyle(txtApellidos));
        //--------------------fin validacion--------------
        cbxCarrera.getItems().setAll(Carrera.values());
        cbxCarrera.getSelectionModel().selectLast();

        Carrera seleccion = cbxCarrera.getSelectionModel().getSelectedItem();
        System.out.println("Seleccionado: " + seleccion);

        cbxTipoParticipante.getItems().setAll(TipoParticipante.values());
        cbxTipoParticipante.getSelectionModel().selectLast();
        definirColumnas();
        listarEstudiantes();
    }



    private void listarEstudiantes(){
        dniColumn.setCellValueFactory(cellData ->cellData.getValue().getDni());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        apellidosColumn.setCellValueFactory(cellData -> cellData.getValue().getApellidos());
        carreraColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCarrera().toString())
        );
        tipoPartiColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoParticipante().toString())
        );
        addActionButtonsToTable();
        participantes = FXCollections.observableArrayList(psi.findAll());

        tableRegPart.setItems(participantes);
    }

    @FXML
    public void buscarPorDni(){
        PersonaDto personaDto = psi.findByDni(txtDni.getText());
        if(personaDto!=null){
            txtNombres.setText(personaDto.getNombre());
            txtApellidos.setText(personaDto.getApellidoPaterno()+" "+personaDto.getApellidoMaterno());
        }else{
            txtNombres.setText("");
            txtApellidos.setText("");
        }
    }

    public void limpiarFormulario(){
        txtDni.clear();
        txtNombres.clear();
        txtApellidos.clear();
        cbxCarrera.getSelectionModel().clearSelection();
        cbxTipoParticipante.getSelectionModel().clearSelection();
    }

    public void limpiarEstilosFormulario(){
        clearErrorStyle(txtDni);
        clearErrorStyle(txtNombres);
        clearErrorStyle(txtApellidos);
    }

    @FXML
    public void registrar(){
        //inicio validadcion
        limpiarEstilosFormulario();
        //fin

        Participante p = new Participante();
        p.setDni(new SimpleStringProperty(txtDni.getText()));
        p.setNombre(new SimpleStringProperty(txtNombres.getText()));
        p.setApellidos(new SimpleStringProperty(txtApellidos.getText()));
        p.setCarrera(cbxCarrera.getValue());
        p.setTipoParticipante(cbxTipoParticipante.getValue());

        //inicio Validacion
        Set<ConstraintViolation<Participante>> violations = validator.validate(p); //Validacion
        if(violations.iterator().hasNext()) {
            ConstraintViolation<Participante> firstViolation = violations.iterator().next();
            String errorMessage = firstViolation.getMessage();
        }
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Participante> violation : violations) {
                // Mapear el error al campo de texto correspondiente

                String fieldName = violation.getPropertyPath().toString();

                switch (fieldName) {
                    case "dni":
                        System.out.println("Error:"+violation.getMessage());
                        setErrorStyle(txtDni);
                        txtDni.requestFocus();
                        break;
                    case "nombre":
                        System.out.println("Error:"+violation.getMessage());
                        setErrorStyle(txtNombres);
                        txtNombres.requestFocus();
                        break;
                    case "apellidos":
                        System.out.println("Error:"+violation.getMessage());
                        setErrorStyle(txtApellidos);
                        txtApellidos.requestFocus();
                        break;
                }
            }
            //Toast.showToast(currentStage, errorMessage);
        }else {// fin validacion
            if (indexEdit != -1) {
                psi.update(p, indexEdit);
                indexEdit=-1;
            } else {
                psi.save(p);
            }
            limpiarFormulario();
        }


        listarEstudiantes();
    }

    @FXML
    public void editar(Participante p, int index){
        txtDni.setText(p.getDni().getValue());
        txtNombres.setText(p.getNombre().getValue());
        txtApellidos.setText(p.getApellidos().getValue());
        cbxCarrera.getSelectionModel().select(p.getCarrera());
        cbxTipoParticipante.getSelectionModel().select(p.getTipoParticipante());
       indexEdit=index;
    }

    @FXML
    public void eliminar(int index){
        //Participante p = tableRegPart.getSelectionModel().getSelectedItem();
        psi.delete(index);
        listarEstudiantes();
    }

    public void definirColumnas(){
        dniColumn = new TableColumn<>("DNI");
        nombreColumn = new TableColumn<>("Nombre");
        apellidosColumn = new TableColumn<>("Apellidos");
        apellidosColumn.setMinWidth(200);
        carreraColumn = new TableColumn<>("Carrera");
        tipoPartiColumn = new TableColumn<>("Tipo Participante");
        opcColumn = new TableColumn<>("Opciones");
        opcColumn.setMinWidth(200);
        tableRegPart.getColumns().addAll(dniColumn, nombreColumn, apellidosColumn, carreraColumn, tipoPartiColumn, opcColumn);
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<Participante, Void>, TableCell<Participante, Void>>
                cellFactory = param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            {
                editButton.setOnAction(event -> {
                    Participante p = getTableView().getItems().get(getIndex());
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


    //---------------i---------------------
    private void clearErrorStyle(TextField field) {
        field.getStyleClass().remove("error-field");
    }

    private void setErrorStyle(TextField field) {
        if (!field.getStyleClass().contains("error-field")) {
            field.getStyleClass().add("error-field");
        }
    }
    //-----------f-------------------------

    }
