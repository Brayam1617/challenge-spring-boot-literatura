package com.brayam.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne
    private Autor autor;

    public Libro() {
    }

    public Libro(String titulo, String idioma, Integer numeroDescargas, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numeroDescargas=" + numeroDescargas +
                ", autor=" + autor +
                '}';
    }
}