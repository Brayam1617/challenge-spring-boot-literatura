package com.brayam.literalura.repository;

import com.brayam.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    List<Autor> findByFechaNacimientoLessThanEqualAndFechaFallecimientoGreaterThanEqual(Integer anioNacimiento, Integer anioFallecimiento);

    List<Autor> findByFechaNacimientoLessThanEqualAndFechaFallecimientoIsNull(Integer anio);
}