package sv.edu.udb.model.enums;

public enum EstadoUsuario {
    ACTIVO,
    INACTIVO;

    public static EstadoUsuario fromBoolean(boolean activo) {
        return activo ? ACTIVO : INACTIVO;
    }

    public boolean toBoolean() {
        return this == ACTIVO;
    }
}
