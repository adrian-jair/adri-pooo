package pe.edu.upeu.conceptospoo.interfaz;

public class Gato implements Animal {

    @Override
    public void emitirSonido() {
        System.out.println("Miuuu....Miuuu...");
    }

    @Override
    public void dormir() {
        System.out.println("Zzz zzz....");
    }

    public String juega() {
        return "Le gusta jugar con el raton!";
    }

}
