package pe.edu.upeu.sysventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.repository.ProductoRepository;

import java.awt.*;

@Controller
public class LoginController {
    @Autowired
    ProductoRepository productoRepository;
    @FXML
    private TextField txt_usuario, txt_clave;

    @FXML
    public void initialize() {
        listarParticipantes();
    }

    public void listarParticipantes() {
        //txt_usuario.setText(productoRepository.getUser());

    }

}
