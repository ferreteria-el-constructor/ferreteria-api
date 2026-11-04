package dev.eder.ferreteria.producto.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter 
@Setter 
@RequiredArgsConstructor 
public class Producto {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_interno", nullable = false, unique = true, length = 12)
    private String codigoInterno;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 80)
    private String marca;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, length = 5)
    private UnidadMedida unidadMedida;

    // Precio con IGV por unidad de medida (por metro, por kilo, por unidad)
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "venta_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    // 3 decimales: 500.000 metros, 80.250 kilos
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal stock;

    @Column(name = "stock_minimo", nullable = false, precision = 12, scale = 3)
    private BigDecimal stockMinimo;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist 
    void alCrear(){
        this.fechaRegistro=LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }
}
