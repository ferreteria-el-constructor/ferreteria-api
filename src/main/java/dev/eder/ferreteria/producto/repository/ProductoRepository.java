package dev.eder.ferreteria.producto.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.eder.ferreteria.producto.entity.Producto;
import jakarta.persistence.LockModeType;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigoInterno(String codigoInterno);

    boolean existsByCodigoInternoAndIdNot(String codigoInterno, Long id);

    List<Producto> findAllByOrderByNombreAsc();

    @Query("""
            SELECT p FROM Producto p
            WHERE p.activo = true
            AND(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                OR LOWER(p.codigoInterno) LIKE LOWER(CONCAT('%', : texto, '%')))
            ORDER BY p.nombre
            """)
    List<Producto> buscarActivo(@Param("texto") String texto, Pageable page);

    // Ordenado por id: todas las cajas bloquean en el mismo orden (sin deadlocks)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id IN :ids ORDER BY p.id ")
    List<Producto> bloquearPorIds(@Param("ids") Collection<Long> ids);
}
