# Copilot Instructions for This Repository

## Project overview
- This repository is a Spring Boot web application (Java 21, Maven Wrapper).
- It is intended to expose HTTP endpoints and business logic for a Todo-style application.
- Prefer clear, maintainable server-side code over clever implementations.

## Recommended project architecture
- Keep a layered structure:
  - `controller`: HTTP request/response handling only
  - `service`: business rules and orchestration
  - `repository`: persistence/data access
  - `model`/`entity`: domain and persistence models
  - `dto`: API input/output payloads
  - `config`: framework and application configuration
- Keep controllers thin; put non-trivial logic in services.
- Avoid circular dependencies between layers.

## Coding conventions
- Follow standard Java style with 4-space indentation and UTF-8 source files.
- Prefer small, single-purpose classes and methods.
- Use `final` for fields and local variables when possible.
- Avoid wildcard imports.
- Use Spring annotations consistently (`@RestController`, `@Service`, `@Repository`, `@Configuration`).
- Favor composition and interface-driven design for replaceable components.

## Naming conventions
- Packages: lowercase, dot-separated (for example: `com.appsdeveloperblog.todo...`).
- Classes/interfaces: PascalCase nouns (`TodoService`, `TodoController`).
- Methods/variables: camelCase verbs/nouns (`createTodo`, `dueDate`).
- Constants: UPPER_SNAKE_CASE.
- Test classes: `<ClassName>Tests` for Spring context tests, `<ClassName>Test` for unit tests.

## Dependency injection recommendations
- Prefer constructor injection (single public constructor, no field injection).
- Keep injected dependencies minimal and explicit.
- Inject abstractions (interfaces) where practical.
- Avoid manual bean lookups; let Spring manage wiring.

## Testing expectations
- Add or update tests for every behavior change.
- Prefer fast unit tests for business logic; use Spring integration tests when framework wiring is relevant.
- Keep tests deterministic and isolated.
- Use clear Arrange-Act-Assert structure.
- Run the full test suite before finalizing changes.

## Maven build and test commands
- Use the Maven Wrapper in this repo:
  - `./mvnw clean test` — compile and run tests
  - `./mvnw test` — run tests only
  - `./mvnw spring-boot:run` — run the application locally
  - `./mvnw clean package` — build runnable artifact

## Error handling recommendations
- Validate input at API boundaries and return appropriate HTTP status codes.
- Use centralized exception handling (for example, `@ControllerAdvice`) for consistent error responses.
- Do not expose stack traces or internal implementation details in API responses.
- Log actionable context for failures without logging secrets or sensitive data.

## Additional repository-wide guidance
- Make small, focused changes; avoid unrelated refactors.
- Reuse existing patterns and libraries before introducing new dependencies.
- Keep public APIs backward compatible unless a breaking change is explicitly requested.
- Update related tests and minimal documentation whenever behavior changes.
