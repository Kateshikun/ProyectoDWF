# Proyecto

## Descripción del Proyecto

Este repositorio contiene el desarrollo del proyecto realizado por el equipo como parte de la asignatura correspondiente.
Aquí se incluyen los recursos, documentación y avances relacionados con el análisis, diseño y desarrollo del sistema.

El objetivo del proyecto es aplicar los conocimientos adquiridos durante el curso para desarrollar una solución funcional, integrando planificación, diseño y documentación del sistema.

---

## Integrantes del Equipo

| Nombre                            | Carnet   | Rol                                                                |
| --------------------------------- | -------- | ------------------------------------------------------------------ |
| Rodrígo Leandro Hernández Ordoñez | HO250329 | Gestión de la Base de Datos                                        |
| Francisco Josue Santos López      | SL251022 | Prototipado de Front-End                                           |
| Daniel Adrian Castillo Garcia     | CG250400 | Descripción del proyecto y Gestión Integral del Proyecto           |
| Rudy Mauricio Gonzalez Pineda     | GP250120 | Prototipado de Front-End                                           |
| Marco Aurelio Zelaya Colocho      | ZC212553 | Portada, Índice, Resultados y Conclusiones, y Anexos del documento |

---

## Link de Notion
https://www.notion.so/Plantilla-de-cronograma-por-sprints-80ee5a18f7c34f3684ec4969064eb55c?source=copy_link

## Application.properties:

spring.application.name=SistemaBoletosAereos

# Configuraci?n de base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/sistemareservasaereas
spring.datasource.username=root
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none


# Configuraci?n de JWT (clave segura de 256 bits)
jwt.secret=e4d8a5f2c7b39a1e0f4d8b6c5a2e1f3d8b7c6a5e4f3d2b1c0a9e8f7d6b5c4a3
jwt.expiration=86400000