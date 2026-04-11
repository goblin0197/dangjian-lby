# Project Context - Party Building Backend (dangjian-lby-backend)

## Project Overview

This is a **Party Building management system** backend built with Java Spring Boot 2.7.2 and MyBatis-Plus. It provides APIs for managing activities, notices, organization members, trainer relations, development stages, and relation transfers within a party organization structure.

## Tech Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 2.7.2 |
| Language | Java 11 |
| Database | MySQL |
| ORM | MyBatis + MyBatis-Plus 3.5.2 |
| Cache | Redis |
| API Docs | Knife4j (OpenAPI 2) |
| Utilities | Hutool, Lombok, EasyExcel |
| Storage | Tencent Cloud COS |

## Project Structure

```
src/main/java/com/coder/springbootinit/
├── annotation/          # Custom annotations (@AuthCheck)
├── aop/                 # Aspect-oriented programming (AuthInterceptor, LogInterceptor)
├── common/              # Common classes (BaseResponse, ErrorCode, PageRequest)
├── config/              # Configuration classes (Cors, Redis, MyBatis-Plus)
├── constant/            # Constants (ActivityConstant, UserConstant, etc.)
├── controller/          # REST controllers
├── entity/              # Database entities
├── dto/                 # Data Transfer Objects (add/update/query requests)
├── enums/               # Enumerations (UserRoleEnum, ActivityStatusEnum, etc.)
├── vo/                  # View Objects for API responses
├── exception/           # Exception handling (BusinessException, GlobalExceptionHandler)
├── manager/             # External service managers (CosManager)
├── mapper/              # MyBatis mappers
├── model/               # Model classes
├── service/             # Service interfaces and implementations
└── utils/               # Utility classes
```

## Key Conventions

### Response Format

All API responses use `BaseResponse<T>` wrapper:

```java
public class BaseResponse<T> {
    private int code;      // Error code (0 = success)
    private T data;        // Response data
    private String message;// Error message
}
```

Use `ResultUtils.success(data)` or `ResultUtils.error(errorCode)` for consistent responses.

### Naming Conventions

- **Controllers**: `{Entity}Controller.java` - Handle HTTP requests
- **Services**: `{Entity}Service.java` (interface) + `{Entity}ServiceImpl.java` (implementation)
- **Mappers**: `{Entity}Mapper.java` - Database operations
- **DTOs**: 
  - `{Entity}AddRequest.java` - Create operations
  - `{Entity}UpdateRequest.java` - Update operations  
  - `{Entity}QueryRequest.java` - Query operations
- **VOs**: `{Entity}VO.java` - Response objects
- **Entities**: `{Entity}.java` - Database table mappings

### Permission Checks

Use `@AuthCheck(mustRole = {...})` annotation on controller methods:

```java
@AuthCheck(mustRole = {"ADMIN"})
@PostMapping("/add")
public BaseResponse<Long> add(@RequestBody ActivityAddRequest request) { ... }
```

### Exception Handling

- Use `ThrowUtils.throwIf(condition, errorCode)` for validation
- Use `BusinessException` for custom exceptions
- All exceptions caught by `GlobalExceptionHandler`

### Database Operations

- Extend `BaseMapper<Entity>` for CRUD operations
- Use `LambdaQueryWrapper<Entity>` for type-safe queries
- Pagination enabled via MyBatis-Plus `Page<Entity>`

## Development Guidelines

### Adding a New Feature

1. **Entity**: Create in `model/entity/`
2. **Enum** (if needed): Create in `model/enums/`
3. **DTOs**: Create Add/Update/Query requests in `model/dto/{entity}/`
4. **VO**: Create response object in `model/vo/`
5. **Mapper**: Extend `BaseMapper<Entity>`
6. **Service**: Define interface + implementation
7. **Controller**: Define REST endpoints with `@AuthCheck` where needed
8. **SQL**: Add table/column definitions to SQL scripts

### Code Style

- Use Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Implement `Serializable` for all model classes
- Use `ResultUtils` for response creation
- Validate inputs in service layer
- Use `ThrowUtils` for guard clauses

### Testing

Test files located in `src/test/java/com/coder/springbootinit/`:
- `MainApplicationTests.java` - Application tests
- `UserServiceTest.java` - Service layer tests
- `CosManagerTest.java` - Manager tests
- `EasyExcelTest.java` - Utility tests

## Running the Project

### Prerequisites

- Java 11+
- MySQL 5.7+
- Redis (for session management)

### Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/party_building
    username: <your-username>
    password: <your-password>
  redis:
    host: localhost
    port: 6379
```

### Start Commands

```bash
# Run with Maven
mvn spring-boot:run

# Or run MainApplication.java directly from IDE
```

Access API docs at: `http://localhost:8101/api/doc.html`

## Git Workflow

- Default branch: `master`
- Commit format: `type(scope): description` (e.g., `feat(sys): add user management`)
- Always verify builds pass before committing

## Related Projects

- Frontend: Vue.js application (separate repository)
- Database migrations: Check `sql/` directory for schema scripts
