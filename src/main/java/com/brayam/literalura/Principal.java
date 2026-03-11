package com.brayam.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Principal {
    public static void main(String[] args) {
        String direccion = "https://gutendex.com/books/?search=frankenstein";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            Datos datos = mapper.readValue(json, Datos.class);

            System.out.println("Cantidad de resultados: " + datos.cantidad());

            for (DatosLibro libro : datos.resultados()) {
                System.out.println("Título: " + libro.titulo());
                System.out.println("Idiomas: " + libro.idiomas());
                System.out.println("Número de descargas: " + libro.numeroDescargas());

                if (libro.autores() != null) {
                    for (DatosAutor autor : libro.autores()) {
                        System.out.println("Autor: " + autor.nombre());
                        System.out.println("Nacimiento: " + autor.fechaNacimiento());
                        System.out.println("Fallecimiento: " + autor.fechaFallecimiento());
                    }
                }

                System.out.println("--------------------------");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}