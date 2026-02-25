package com.uniquindio.backend.cli;

import com.uniquindio.backend.dto.RatingDTO;
import com.uniquindio.backend.dto.ReviewDTO;
import com.uniquindio.backend.dto.CreateRatingRequest;
import com.uniquindio.backend.dto.CreateReviewRequest;
import com.uniquindio.backend.dto.BookDTO;
import com.uniquindio.backend.service.BookService;
import com.uniquindio.backend.service.RatingService;
import com.uniquindio.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class BookstoreCLI implements CommandLineRunner {

    private final BookService bookService;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    
    @Value("${app.cli.enabled:false}")
    private boolean cliEnabled;
    
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        boolean isCLIMode = Arrays.asList(args).contains("--cli") || cliEnabled;
        if (!isCLIMode) {
            return;
        }
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    searchMenu();
                    break;
                case "2":
                    ratingsMenu();
                    break;
                case "3":
                    reviewsMenu();
                    break;
                case "0":
                    running = false;
                    System.out.println("\nGracias por usar la libreria. Hasta luego.\n");
                    break;
                default:
                    System.out.println("\nOpcion no valida. Intenta de nuevo.\n");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== Libreria (CLI) ===");
        System.out.println("1. Busqueda");
        System.out.println("2. Calificaciones");
        System.out.println("3. Resenas");
        System.out.println("0. Salir");
        System.out.print("\nSelecciona una opcion: ");
    }

    private void searchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Busqueda --");
            System.out.println("1. Basica (titulo o autor)");
            System.out.println("2. Ver todos los libros");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opcion: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    searchBooks();
                    break;
                case "2":
                    showAllBooksTable();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida. Intenta de nuevo.");
            }
        }
    }

    private void ratingsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Calificaciones --");
            System.out.println("1. Ver calificaciones");
            System.out.println("2. Calificar un libro");
            System.out.println("3. Ver todos los libros");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opcion: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewRatings();
                    break;
                case "2":
                    rateBook();
                    break;
                case "3":
                    showAllBooksTable();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida. Intenta de nuevo.");
            }
        }
    }

    private void reviewsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Resenas --");
            System.out.println("1. Ver resenas");
            System.out.println("2. Escribir una resena");
            System.out.println("3. Ver todos los libros");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opcion: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewReviews();
                    break;
                case "2":
                    writeReview();
                    break;
                case "3":
                    showAllBooksTable();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("\nOpcion no valida. Intenta de nuevo.");
            }
        }
    }

    /**
     * Búsqueda básica por palabra o frase en título y autor
     */
    private void searchBooks() {
        System.out.println("\nBusqueda por texto relacionado");
        System.out.print("Ingresa una palabra o frase: ");
        String searchTerm = scanner.nextLine().trim();

        if (searchTerm.isEmpty()) {
            System.out.println("\nDebes ingresar un termino de busqueda.");
            return;
        }

        List<BookDTO> results = bookService.searchBooksByText(searchTerm);

        if (results.isEmpty()) {
            System.out.println("\nNo se encontraron libros con ese termino.");
            return;
        }

        System.out.println("\nLibros relacionados:");
        displaySearchResults(results);
        BookDTO selected = promptForBookSelection("ver detalles", results);
        if (selected != null) {
            System.out.println();
            displayBookDetails(selected);
        }
    }

    /**
     * Mostrar resultados de búsqueda
     */
    private void displaySearchResults(List<BookDTO> books) {
        for (BookDTO book : books) {
            System.out.println(book.getId() + ". " + book.getTitle() + " - " + book.getAuthor());
        }
    }


    private BookDTO promptForBookSelection(String actionLabel, List<BookDTO> options) {
        System.out.print("\nIngresa el ID para " + actionLabel + " o Enter para volver: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }

        Long bookId;
        try {
            bookId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("\nID invalido.");
            return null;
        }

        for (BookDTO option : options) {
            if (option.getId().equals(bookId)) {
                return option;
            }
        }

        System.out.println("\nEl ID no esta en la lista.");
        return null;
    }

    private void displayBookDetails(BookDTO book) {
        System.out.println("ID: " + book.getId());
        System.out.println("Titulo: " + book.getTitle());
        System.out.println("Autor: " + book.getAuthor());
        if (book.getIsbn() != null) System.out.println("ISBN: " + book.getIsbn());
        if (book.getGenre() != null) System.out.println("Genero: " + book.getGenre());
        if (book.getPublishedYear() != null) System.out.println("Anio de publicacion: " + book.getPublishedYear());
        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            System.out.println("Descripcion: " + book.getDescription());
        }
        if (book.getAverageRating() != null) {
            System.out.println("Calificacion promedio: " + book.getAverageRating() + "/5 (" + book.getTotalRatings() + " votos)");
        }
        System.out.println("Resenas: " + book.getTotalReviews());
    }

    private void showAllBooksTable() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("\nNo hay libros disponibles.");
            return;
        }

        System.out.println("\nListado de libros");
        System.out.println("ID  | Titulo                          | Autor                    | ISBN            | Anio");
        System.out.println("----+----------------------------------+--------------------------+-----------------+------");
        for (BookDTO book : books) {
            String id = padRight(String.valueOf(book.getId()), 3);
            String title = padRight(trimToLength(book.getTitle(), 32), 32);
            String author = padRight(trimToLength(book.getAuthor(), 24), 24);
            String isbn = padRight(trimToLength(book.getIsbn(), 15), 15);
            String year = book.getPublishedYear() != null ? String.valueOf(book.getPublishedYear()) : "";

            System.out.println(id + " | " + title + " | " + author + " | " + isbn + " | " + year);
        }
        System.out.println();
    }

    private String padRight(String value, int length) {
        if (value == null) {
            value = "";
        }
        if (value.length() >= length) {
            return value;
        }
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private String trimToLength(String value, int length) {
        if (value == null) {
            return "";
        }
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, Math.max(0, length - 3)) + "...";
    }

    /**
     * Seleccionar un libro para una accion
     */
    private BookDTO selectBook(String actionLabel) {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("\nNo hay libros disponibles.");
            return null;
        }

        System.out.println("\nLibros disponibles:");
        for (BookDTO book : books) {
            System.out.println(book.getId() + ". " + book.getTitle() + " - " + book.getAuthor());
        }

        System.out.print("\nIngresa el ID del libro para " + actionLabel + ": ");
        Long bookId;
        try {
            bookId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\nID invalido.");
            return null;
        }

        try {
            return bookService.getBookById(bookId);
        } catch (Exception e) {
            System.out.println("\nNo se encontro un libro con ese ID.");
            return null;
        }
    }

    private void viewRatings() {
        System.out.println("\nVer calificaciones");
        BookDTO book = selectBook("ver calificaciones");
        if (book == null) {
            return;
        }

        List<RatingDTO> ratings = ratingService.getRatingsByBook(book.getId());
        Double average = ratingService.getAverageRating(book.getId());

        System.out.println("\nLibro: " + book.getTitle());
        if (average != null) {
            System.out.println("Promedio: " + average + "/5");
        }

        if (ratings.isEmpty()) {
            System.out.println("No hay calificaciones para este libro.");
            return;
        }

        System.out.println("\nCalificaciones:");
        for (RatingDTO rating : ratings) {
            String when = rating.getCreatedAt() != null ? " (" + rating.getCreatedAt() + ")" : "";
            System.out.println("- " + rating.getScore() + "/5" + when);
        }
        System.out.println();
    }

    private void viewReviews() {
        System.out.println("\nVer resenas");
        BookDTO book = selectBook("ver resenas");
        if (book == null) {
            return;
        }

        List<ReviewDTO> reviews = reviewService.getPublishedReviewsByBook(book.getId());
        if (reviews.isEmpty()) {
            System.out.println("\nNo hay resenas publicadas para este libro.");
            return;
        }

        System.out.println("\nResenas publicadas:");
        for (ReviewDTO review : reviews) {
            System.out.println("Titulo: " + review.getTitle());
            if (review.getCreatedAt() != null) {
                System.out.println("Fecha: " + review.getCreatedAt());
            }
            System.out.println("Contenido: " + review.getContent());
            System.out.println();
        }
    }

    private void rateBook() {
        System.out.println("\nCalificar un libro");
        BookDTO book = selectBook("calificar");
        if (book == null) {
            return;
        }

        System.out.print("\nIngresa tu calificacion (1-5): ");
        Integer score;
        try {
            score = Integer.parseInt(scanner.nextLine().trim());
            if (score < 1 || score > 5) {
                System.out.println("\nLa calificacion debe estar entre 1 y 5.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nCalificacion invalida.");
            return;
        }

        try {
            CreateRatingRequest request = CreateRatingRequest.builder()
                    .bookId(book.getId())
                    .score(score)
                    .build();

            ratingService.rateBook(request);
            System.out.println("\nGracias. Tu calificacion fue registrada.");
        } catch (Exception e) {
            System.out.println("\nNo se pudo guardar la calificacion. Intenta de nuevo.");
        }
    }

    /**
     * Escribir una reseña de un libro con previa
     */
    private void writeReview() {
        System.out.println("\nEscribir una resena");
        BookDTO bookDTO = selectBook("escribir una resena");
        if (bookDTO == null) {
            return;
        }

        System.out.print("\nTitulo de la resena: ");
        String reviewTitle = scanner.nextLine().trim();
        if (reviewTitle.isEmpty()) {
            System.out.println("\nEl titulo no puede estar vacio.");
            return;
        }

        System.out.println("\nContenido de la resena (presiona Enter dos veces para terminar):");
        StringBuilder reviewContent = new StringBuilder();
        String line;
        int emptyLines = 0;
        while (true) {
            line = scanner.nextLine();
            if (line.isEmpty()) {
                emptyLines++;
                if (emptyLines >= 2) break;
                reviewContent.append("\n");
            } else {
                emptyLines = 0;
                reviewContent.append(line).append("\n");
            }
        }

        String content = reviewContent.toString().trim();
        if (content.isEmpty()) {
            System.out.println("\nEl contenido no puede estar vacio.");
            return;
        }

        // Mostrar vista previa
        showReviewPreview(bookDTO, reviewTitle, content);

        System.out.print("\nDeseas publicar esta resena? (s/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("s")) {
            try {
                CreateReviewRequest request = CreateReviewRequest.builder()
                        .bookId(bookDTO.getId())
                        .title(reviewTitle)
                        .content(content)
                        .build();

                ReviewDTO review = reviewService.createReview(request);
                reviewService.publishReview(review.getId());
                System.out.println("\nResena publicada exitosamente.");
            } catch (Exception e) {
                System.out.println("\nError al guardar la resena. Intenta de nuevo.");
            }
        } else {
            System.out.println("\nResena no publicada.");
        }
    }

    /**
     * Mostrar una vista previa de la reseña
     */
    private void showReviewPreview(BookDTO book, String reviewTitle, String reviewContent) {
        System.out.println("\n--- Vista previa ---");
        System.out.println("Libro: " + book.getTitle());
        System.out.println("Titulo: " + reviewTitle);
        System.out.println("\n---------------------");
        System.out.println(reviewContent);
        System.out.println("---------------------\n");
    }
}