package com.brayam.literalura;

import com.brayam.literalura.model.Autor;
import com.brayam.literalura.model.Libro;
import com.brayam.literalura.repository.AutorRepository;
import com.brayam.literalura.repository.LibroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    private final Scanner teclado = new Scanner(System.in);

    public LiteraluraApplication(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        muestraElMenu();
    }

    private void muestraElMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Mostrar cantidad de libros por idioma
                    
                    0 - Salir
                    """;

            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar una opción válida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAnio();
                    break;
                case 5:
                    mostrarCantidadLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();

        String tituloBuscado = URLEncoder.encode(tituloLibro, StandardCharsets.UTF_8);
        String direccion = "https://gutendex.com/books/?search=" + tituloBuscado;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            ObjectMapper mapper = new ObjectMapper();
            Datos datos = mapper.readValue(response.body(), Datos.class);

            if (datos.resultados() == null || datos.resultados().isEmpty()) {
                System.out.println("No se encontraron libros para esa búsqueda.");
                return;
            }

            DatosLibro datosLibro = datos.resultados().get(0);

            if (datosLibro.autores() == null || datosLibro.autores().isEmpty()) {
                System.out.println("El libro encontrado no tiene autor informado.");
                return;
            }

            DatosAutor datosAutor = datosLibro.autores().get(0);

            Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> autorRepository.save(
                            new Autor(
                                    datosAutor.nombre(),
                                    datosAutor.fechaNacimiento(),
                                    datosAutor.fechaFallecimiento()
                            )
                    ));

            String idioma = (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty())
                    ? datosLibro.idiomas().get(0)
                    : "desconocido";

            var libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("Ese libro ya está registrado:");
                System.out.println(libroExistente.get());
                return;
            }

            Libro libro = new Libro(
                    datosLibro.titulo(),
                    idioma,
                    datosLibro.numeroDescargas(),
                    autor
            );

            libroRepository.save(libro);

            System.out.println("Libro guardado exitosamente:");
            System.out.println(libro);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ocurrió un error al consultar la API.");
            throw new RuntimeException(e);
        }
    }

    private void listarLibrosRegistrados() {
        var libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("Libros registrados:");
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        var autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        System.out.println("Autores registrados:");
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnDeterminadoAnio() {
        System.out.println("Ingresa el año:");

        try {
            int anio = Integer.parseInt(teclado.nextLine());

            var autoresConFallecimiento = autorRepository
                    .findByFechaNacimientoLessThanEqualAndFechaFallecimientoGreaterThanEqual(anio, anio);

            var autoresSinFallecimiento = autorRepository
                    .findByFechaNacimientoLessThanEqualAndFechaFallecimientoIsNull(anio);

            if (autoresConFallecimiento.isEmpty() && autoresSinFallecimiento.isEmpty()) {
                System.out.println("No se encontraron autores vivos en ese año.");
                return;
            }

            System.out.println("Autores vivos en el año " + anio + ":");

            autoresConFallecimiento.forEach(System.out::println);
            autoresSinFallecimiento.forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Debes ingresar un año válido.");
        }
    }

    private void mostrarCantidadLibrosPorIdioma() {
        var menuIdiomas = """
                
                Ingresa el idioma para buscar la cantidad de libros:
                en - Inglés
                es - Español
                fr - Francés
                pt - Portugués
                """;

        System.out.println(menuIdiomas);
        var idioma = teclado.nextLine().toLowerCase();

        long cantidad = libroRepository.countByIdioma(idioma);

        switch (idioma) {
            case "en":
                System.out.println("Cantidad de libros en inglés: " + cantidad);
                break;
            case "es":
                System.out.println("Cantidad de libros en español: " + cantidad);
                break;
            case "fr":
                System.out.println("Cantidad de libros en francés: " + cantidad);
                break;
            case "pt":
                System.out.println("Cantidad de libros en portugués: " + cantidad);
                break;
            default:
                System.out.println("Idioma no contemplado en el menú.");
        }
    }
}