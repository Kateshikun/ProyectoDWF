package sv.edu.udb.model.enums;

public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA;

    public static EstadoReserva fromString(String value) {
        if (value == null) return PENDIENTE;
        return switch (value.toUpperCase()) {
            case "PENDIENTE" -> PENDIENTE;
            case "CONFIRMADA" -> CONFIRMADA;
            case "CANCELADA" -> CANCELADA;
            default -> throw new IllegalArgumentException("Estado de reserva invalido: " + value);
        };
    }

    public String getLabel() {
        return switch (this) {
            case PENDIENTE -> "Pendiente";
            case CONFIRMADA -> "Confirmada";
            case CANCELADA -> "Cancelada";
        };
    }
}
