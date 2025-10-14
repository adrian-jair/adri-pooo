package pe.edu.upeu.asistencia.control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Controller
public class MainguiController {
    @FXML
    private BorderPane bp;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane tabPane;
    @FXML
    private Menu menu1, menu2=new Menu("Cambiar Estilo");
    @FXML
    private MenuItem menuItem1, menuItem2, menuItemS, menuItem3;
    @Autowired
    private ApplicationContext context;

    // Crear un ComboBox
    ComboBox<String> comboBox = new ComboBox<>();
    CustomMenuItem customItem = new CustomMenuItem(comboBox);

    @FXML
    public void initialize() {
        comboBox.getItems().addAll(
                "Estilo por Defecto",
                "Estilo Oscuro",
                "Estilo Azul",
                "Estilo Verde",
                "Estilo Rosado"
        );
        comboBox.setOnAction(e -> cambiarEstilo());

        customItem.setHideOnClick(false);
        menu2.getItems().add(customItem);
        menuBar.getMenus().addAll(menu2);

        MenuListener mL = new MenuListener();
        MenuItemListener mIL = new MenuItemListener();
        menuItem1.setOnAction(mIL::handle);
        menuItem2.setOnAction(mIL::handle);
        menuItemS.setOnAction(mIL::handle);
        menuItem3.setOnAction(mIL::handle);

    }

    @FXML
    private void cambiarEstilo() {
        String estiloSeleccionado = comboBox.getSelectionModel().getSelectedItem();
        Scene escena = bp.getScene();
        // Limpiar estilos previos
        escena.getStylesheets().clear();
        //escena.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        switch (estiloSeleccionado) {
            case "Estilo Oscuro":
                escena.getStylesheets().add(getClass().getResource("/css/estilo-oscuro.css").toExternalForm());
                break;
            case "Estilo Azul":
                escena.getStylesheets().add(getClass().getResource("/css/estilo-azul.css").toExternalForm());
                break;
            case "Estilo Verde":
                escena.getStylesheets().add(getClass().getResource("/css/estilo-verde.css").toExternalForm());
                break;
            case "Estilo Rosado":
                escena.getStylesheets().add(getClass().getResource("/css/estilo-rosado.css").toExternalForm());
                break;
            default: break;
        }
    }

    class MenuItemListener{
        Map<String, String[]> menuConfig = Map.of(
                "menuItem1", new String[]{"/fxml/main_asistencia.fxml", "Reg. Participante","T"},
                "menuItem2", new String[]{"/fxml/main_participante.fxml", "Participantes","T"},
                "menuItem3", new String[]{"/fxml/main_evento.fxml", "Eventos","T"},
                "mimiregventa",   new String[]{"/fxml/main_venta.fxml", "Ventas","T"},
                "menuItemS",   new String[]{"/fxml/login.fxml", "Salir","S"}
        );

        public void handle(ActionEvent e){
            String id = ((MenuItem) e.getSource()).getId();
            System.out.println("Menu seleccionado: " + id);
            if (menuConfig.containsKey(id)) {
                String[] cfg = menuConfig.get(id);
                if(cfg[2].equals("S") ){
                    Platform.exit();
                    System.exit(0);
                }else {
                    abrirTabConFXML(cfg[0], cfg[1]);
                }
            }
        }

        private void abrirTabConFXML(String fxmlPath, String tituloTab) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                loader.setControllerFactory(context::getBean); // Inyección con Spring
                Parent root = loader.load();

                ScrollPane scrollPane = new ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                Tab newTab = new Tab(tituloTab, scrollPane);
                tabPane.getTabs().clear(); // si quieres siempre limpiar
                tabPane.getTabs().add(newTab);

            } catch (IOException e) {
                throw new RuntimeException("Error al cargar FXML: " + fxmlPath, e);
            }
        }
    }
    class MenuListener{
        public void menuSelected(Event e){
        }
    }
}
