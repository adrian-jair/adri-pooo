package pe.edu.upeu.conceptospoo.interfaz;

public class Loro implements Animal {

    @Override
    public void emitirSonido() {
        System.out.println("Hola manito,...aprende pues");
    }

    @Override
    public void dormir() {
        System.out.println("Zzz zzz....");
    }
}
