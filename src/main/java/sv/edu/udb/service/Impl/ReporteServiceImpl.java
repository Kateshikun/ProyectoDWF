package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.PagoRepository;
import sv.edu.udb.repository.ReservacionRepository;
import sv.edu.udb.service.ReporteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Servicio de reportes para el administrador
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final PagoRepository pagoRepository;
    private final ReservacionRepository reservacionRepository;


    @Override
    public Double calcularIngresosTotales() {
        System.out.println("Calculando ingresos totales del sistema");
        
        Double ingresosTotales = pagoRepository.calcularIngresosTotales();
        
        // Asegurar que no sea nulo
        if (ingresosTotales == null) {
            ingresosTotales = 0.0;
        }
        
        System.out.println("Ingresos totales calculados: $" + ingresosTotales);
        
        return ingresosTotales;
    }


    @Override
    public Map<String, Long> obtenerVuelosMasDemandados() {
        System.out.println("Obteniendo vuelos más demandados");
        
        List<Object[]> resultados = reservacionRepository.obtenerVuelosMasDemandados();
        Map<String, Long> vuelosDemandados = new HashMap<>(); //HashMap es para almacenar pares clave-valor
        
        for (Object[] resultado : resultados) {
            String ruta = (String) resultado[0];
            Long cantidad = (Long) resultado[1];
            vuelosDemandados.put(ruta, cantidad);
            
            System.out.println("Ruta: " + ruta + " - Reservaciones: " + cantidad);
        }
        
        System.out.println("Se encontraron " + vuelosDemandados.size() + " rutas en el reporte de demanda");
        
        return vuelosDemandados;
    }


    @Override
    public Long contarReservasTotales() {
        System.out.println("Contando reservaciones totales del sistema");
        
        Long totalReservas = reservacionRepository.count();
        
        System.out.println("Total de reservaciones encontradas: " + totalReservas);
        
        return totalReservas;
    }
}
