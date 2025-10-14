/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.conceptospoo.encapsulamiento;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author SOFTWARE
 */
//@Setter
//@Getter
@Data
public class PersonaX {
    
    private String nombre;
    private int edad;

    public  void saludar(){
        System.out.println("Hola, soy "+nombre+" y mi edad es "+edad);
    }
    
}
