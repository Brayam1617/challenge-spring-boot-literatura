package com.brayam.literalura.repository;

import com.brayam.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
    long countByIdioma(String idioma);
}