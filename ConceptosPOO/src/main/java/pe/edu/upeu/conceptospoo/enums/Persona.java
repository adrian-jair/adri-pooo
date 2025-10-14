package pe.edu.upeu.conceptospoo.enums;

enum GENERO{Masculino,Femenino}
public class Persona {
    enum NACIONALIDAD{Peruano, Venezolano, Boliviano}

   static String nombre;
   static GENERO genero=GENERO.Masculino;
   static NACIONALIDAD nacionalidad=NACIONALIDAD.Peruano;
    public static void main(String[] args) {
        nombre="David";
        System.out.println(nombre+ " es "+nacionalidad+ " y tiene el genero "+genero);

        for (GENERO xx: GENERO.values()){
            System.out.println(xx);
        }
        for (NACIONALIDAD x: NACIONALIDAD.values()){
            System.out.println(x);
        }
    }
}
