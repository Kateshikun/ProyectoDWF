package sv.edu.udb.service;

import java.util.Map;

public interface ReporteService {
    Double calcularIngresosTotales();
    Map<String, Long> obtenerVuelosMasDemandados(); // Ruta vs Cantidad de Reservas
    Long contarReservasTotales();
}
