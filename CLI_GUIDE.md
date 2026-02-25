# CLI (Command Line Interface) - User Mode Guide

The application includes an interactive CLI interface that allows users to interact with the bookstore directly from the command line.

## Enabling the CLI

The CLI can be enabled in two ways:

### Option 1: Using Command Line Argument

```bash
java -jar build/libs/backend-*.jar --cli
```

### Option 2: Using Configuration Property

Edit `application.properties` and set:

```properties
app.cli.enabled=true
```

Then run:

```bash
java -jar build/libs/backend-*.jar
```

## CLI Features

The CLI provides the following functionality based on your requirements:

### 1. **Basic Search** 🔍

- **Requirement**: "A user can do a basic simple search that searches for a word or phrase in both the author and title fields."
- Search for books by a word or phrase
- Searches in both title and author fields simultaneously
- Case-insensitive search
- Example: Search for "Gatsby" or "Fitzgerald"

### 2. **Advanced Search** 🔎

- **Requirement**: "A user can search for books by entering values in any combination of author, title and ISBN"
- Search by any combination of:
  - **ISBN**: International Standard Book Number (exact or partial match)
  - **Title**: Book title (partial match)
  - **Author**: Author name (partial match)
- All fields are optional - search by any combination
- Case-insensitive search

### 3. **Rate Books** ⭐

- **Requirement**: "A user can rate books from 1 (bad) to 5 (good). The book does not have to be one the user bought from us"
- Rate any book from 1 (bad) to 5 (good) stars
- No purchase required - anyone can rate any book
- Each rating is recorded with a timestamp
- View average ratings and rating count for each book

### 4. **Write Reviews** ✍️

- **Requirement**: "A user can write a review of a book. She can preview the review before submitting it. The book does not have to be one the user bought from us"
- Write detailed reviews for any book
- Features:
  - Enter a review title
  - Write multi-line review content
  - **Preview review before publishing** - see exactly how it will appear
  - Publish or discard after preview
- No purchase required - anyone can review any book
- Reviews are published immediately after confirmation

## CLI Menu

Upon startup (when CLI is enabled), you'll see:

```
╔════════════════════════════════════════╗
║       📚 BIENVENIDO A LA LIBRERÍA 📚  ║
╚════════════════════════════════════════╝
1. 🔍  Buscar libro (título o autor)
2. 🔎  Búsqueda avanzada (ISBN, título, autor)
3. ⭐ Calificar un libro
4. ✍️  Escribir una reseña
0. 🚪 Salir

Selecciona una opción:
```

## Example CLI Sessions

### Example 1: Basic Search

```
Selecciona una opción: 1
Ingresa la palabra o frase a buscar: Fitzgerald

✅ Se encontraron 1 libro(s):

─────────────────────────────────────────
ID: 1
Título: The Great Gatsby
Autor: F. Scott Fitzgerald
ISBN: 9780743273565
Género: Fiction
Año de publicación: 1925
⭐ Calificación promedio: 4.5/5 (12 votos)
Reseñas: 3
─────────────────────────────────────────
```

### Example 2: Advanced Search

```
Selecciona una opción: 2
(Puedes dejar campos en blanco para omitirlos)

Título del libro: Great
Autor del libro:
ISBN del libro: 9780743273565

✅ Se encontraron 1 libro(s):
[displays matching book results]
```

### Example 3: Rate a Book

```
Selecciona una opción: 3

📚 Libros disponibles:
1. The Great Gatsby - F. Scott Fitzgerald
2. To Kill a Mockingbird - Harper Lee

Ingresa el ID del libro a calificar: 1
Ingresa tu calificación (1-5): 5

✅ ¡Gracias! Tu calificación de 5 estrellas ha sido registrada.
```

### Example 4: Write a Review with Preview

```
Selecciona una opción: 4

📚 Libros disponibles:
1. The Great Gatsby - F. Scott Fitzgerald

Ingresa el ID del libro a reseñar: 1
Título de la reseña: An absolute masterpiece
Contenido de la reseña:
This novel is a stunning exploration of wealth, love, and the American Dream.
Fitzgerald's prose is elegant and his characters are unforgettable.
Highly recommended!

╔════════════════════════════════════════╗
║          VISTA PREVIA DE RESEÑA       ║
╚════════════════════════════════════════╝

📖 Libro: The Great Gatsby
✍️  Título de la reseña: An absolute masterpiece

─────────────────────────────────────────
This novel is a stunning exploration of wealth, love, and the American Dream.
Fitzgerald's prose is elegant and his characters are unforgettable.
Highly recommended!
─────────────────────────────────────────

¿Desias publicar esta reseña? (s/n): s

✅ ¡Reseña publicada exitosamente!
```

## Running the Application

### Build the project

```bash
./gradlew build
```

### Run with CLI mode

```bash
java -jar build/libs/backend-*.jar --cli
```

### Or enable CLI in configuration

```bash
# Edit application.properties and set app.cli.enabled=true
java -jar build/libs/backend-*.jar
```

## CLI vs API Mode

- **CLI Mode** (when `--cli` is used or `app.cli.enabled=true`):
  - The application runs the interactive CLI
  - Does NOT start the REST API server
  - For direct user interaction from terminal

- **API Mode** (default):
  - The application starts the REST API server on port 8080
  - Does NOT run CLI mode
  - For programmatic access and web/mobile applications

- Choose the mode based on your needs:
  - Use **CLI** for direct user interaction from terminal
  - Use **API** for programmatic access via REST endpoints

## Implementation Details

- **User Concept**: As requested, there is no User class. The CLI treats each session as an anonymous user who can:
  - Search for books freely
  - Rate any book
  - Write reviews for any book
  - Preview before publishing reviews

- **No Purchase Requirement**: Any book can be rated or reviewed without needing to be purchased by the user

- **Database**: All ratings and reviews are persisted to the PostgreSQL database

## Search Examples

### Basic Search Examples

- Search: "Gatsby" → Returns any book with "Gatsby" in title or author
- Search: "Classic" → Returns books with "Classic" in title or author

### Advanced Search Examples

- Author: "F. Scott", Title: empty, ISBN: empty → Returns all books by Scott
- Author: empty, Title: "Gatsby", ISBN: empty → Returns all books with "Gatsby" in title
- Author: empty, Title: empty, ISBN: "9780743273565" → Returns book with that ISBN
- Author: "Scott", Title: "Great", ISBN: empty → Returns books by Scott with "Great" in title
