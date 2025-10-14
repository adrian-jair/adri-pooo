package pe.edu.upeu.conceptospoo.encapsulamiento;

public class ClaseGeneral{

    public void probar(){
        Persona p = new Persona();
        p.setNombre("Juan");
        p.setEdad(18);
        p.saludar();
    }

    public void aplicandolombok(){
        PersonaX p = new PersonaX();
        p.setNombre("Juan");
        p.setEdad(18);
        p.saludar();
    }
    
    
    public static void main(String[] args) {
        ClaseGeneral cg=new ClaseGeneral();
        
        cg.probar();
        
      /*  Persona persona = new Persona();//persona es un Objeto

        persona.nombre="David";
        persona.edad=18;
        persona.saludar();
        
        persona.setNombre("Maria");
        persona.setEdad(25);

        System.out.println("Nombre: "+persona.getNombre());
        System.out.println("Edad: "+persona.getEdad());

        persona.saludar();*/

    }

}
