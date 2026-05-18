package sv.edu.udb.model.enums;

public enum RolUsuario {
    ADMIN,
    CLIENTE;

    public static RolUsuario fromString(String value) {
        if (value == null) return CLIENTE;
        return switch (value.toUpperCase()) {
            case "ADMIN" -> ADMIN;
            case "CLIENTE" -> CLIENTE;
            default -> throw new IllegalArgumentException("Rol invalido: " + value + ". Debe ser ADMIN o CLIENTE");
        };
    }
}
