package sv.edu.udb.model.enums;

public enum EstadoVuelo {
    PROGRAMADO,
    RETRASADO,
    CANCELADO;

    public static EstadoVuelo fromString(String value) {
        if (value == null) return PROGRAMADO;
        return switch (value.toUpperCase()) {
            case "PROGRAMADO" -> PROGRAMADO;
            case "RETRASADO" -> RETRASADO;
            case "CANCELADO" -> CANCELADO;
            default -> throw new IllegalArgumentException("Estado de vuelo invalido: " + value + ". Debe ser: Programado, Retrasado o Cancelado");
        };
    }

    public String getLabel() {
        return switch (this) {
            case PROGRAMADO -> "Programado";
            case RETRASADO -> "Retrasado";
            case CANCELADO -> "Cancelado";
        };
    }
}
