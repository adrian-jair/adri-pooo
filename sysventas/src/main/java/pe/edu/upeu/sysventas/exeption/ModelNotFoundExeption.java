package pe.edu.upeu.sysventas.exeption;

import lombok.Getter;

@Getter
public class ModelNotFoundExeption extends RuntimeException{

    private final int errorCode;

    public ModelNotFoundExeption(String message) {
        super(message);
        this.errorCode=0;
    }

    public ModelNotFoundExeption(String message, int errorCode) {
        super(message);
        this.errorCode=errorCode;
    }
}
