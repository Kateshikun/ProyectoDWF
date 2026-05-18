// === REQUEST DTOs ===

export interface AuthRequest {
  username: string;
  password: string;
}

export interface UsuarioDTO {
  username: string;
  password: string;
  email: string;
  rol: string;
  activo: boolean;
}

export interface VueloDto {
  id_vuelo?: number | null;
  id_ruta: number;
  id_avion: number;
  fecha_salida: string;
  fecha_llegada: string;
  estado: string;
  nombre_piloto: string;
  nombre_copiloto: string;
}

export interface ReservacionDTO {
  idVuelo: number;
  idPasajero: number;
  asientoAsignado: string;
  estadoReserva: string;
}

export interface PagoDTO {
  idReservacion: number;
  monto: number;
  numeroTarjeta: string;
  cvv: string;
}

// === RESPONSE DTOs ===

export interface AuthResponse {
  token: string;
  username: string;
  rol: string;
}

export interface VueloResponse {
  id_vuelo: number;
  id_ruta: number | null;
  origen: string | null;
  ciudad_origen: string | null;
  pais_origen: string | null;
  destino: string | null;
  ciudad_destino: string | null;
  pais_destino: string | null;
  distancia_km: number | null;
  id_avion: number | null;
  capacidad_pasajeros: number | null;
  fecha_salida: string | null;
  fecha_llegada: string | null;
  estado: string | null;
  nombre_piloto: string | null;
  nombre_copiloto: string | null;
}

export interface ReservacionResponse {
  id_reservacion: number;
  id_vuelo: number | null;
  codigo_vuelo: string | null;
  origen: string | null;
  destino: string | null;
  fecha_salida: string | null;
  fecha_llegada: string | null;
  id_pasajero: number | null;
  nombre_pasajero: string | null;
  apellido_pasajero: string | null;
  pasaporte_pasajero: string | null;
  fecha_reservacion: string | null;
  estado_reserva: string | null;
  asiento_asignado: string | null;
}

export interface PagoResponse {
  idPago: number;
  idReservacion: number | null;
  monto: number;
  fecha_pago: string | null;
}

export interface UsuarioResponse {
  id_usuario: number;
  username: string;
  email: string;
  rol: string;
  estado: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
