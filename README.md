# Ktor Freemarker Template Project

## Prerequisites

Before you begin, ensure you have the following installed:
- JDK 21 or higher
- Gradle 8.5 or higher (or use the included Gradle wrapper)
- IntelliJ IDEA (recommended) or any other IDE with Kotlin support

## Quick Start

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd ktor-freemarker-template
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

   The application will start and be available at `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/
│   │       └── example/
│   │           └── Application.kt
│   ├── resources/
│   │   ├── templates/
│   │   │   └── index.ftl
│   │   └── logback.xml
│   └── webapp/
└── test/
    └── kotlin/
        └── com/
            └── example/
                └── ApplicationTest.kt
```

## Available Routes

- `GET /` - Home page
- `POST /greet` - Displays a greeting using JSON data
- `POST /welcome` - Welcome page with JSON data

### JSON Request Format
```json
{
    "message": "Your message here",
    "sender": "Your name"  // Optional, defaults to "Anonymous"
}
```

Example using curl:
```bash
curl -X POST http://localhost:8080/greet \
     -H "Content-Type: application/json" \
     -d '{"message": "Hello, World!", "sender": "John"}'
```

## Running Tests

To run the tests:
```bash
./gradlew test
```

Test results will be displayed in the console and also available in:
`build/reports/tests/test/index.html`

## Development

### Adding New Templates

1. Create new `.ftl` files in `src/main/resources/templates/`
2. Reference them in your route handlers:
   ```kotlin
   get("/your-route") {
       call.respond(FreeMarkerContent("your-template.ftl", model))
   }
   ```

### Adding New Routes

Add new routes in `Application.kt`:
```kotlin
routing {
    get("/your-new-route") {
        // Your route handling code
    }
}
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.