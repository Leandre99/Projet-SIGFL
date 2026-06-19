# SIGFL Backend — Architecture (template)

This project is a **modular monolith** organised by **DDD**. Each business
capability is a *module*; inside a module the code is split into the classic
DDD layers. Use the existing `auth`, `storage` and `mail` modules as the
blueprint for new ones.

## Package layout

```
bj.mef.sigfl_backend
├── SigflBackendApplication        # entry point (@ConfigurationPropertiesScan)
│
├── shared/                        # cross-cutting code reused by every module
│   ├── config/                    # JpaConfig (auditing), OpenApiConfig
│   ├── domain/                    # BaseEntity, exceptions (DomainException, ...)
│   └── web/                       # GlobalExceptionHandler, ApiError, PingController
│
├── auth/                          # ── module: authentication (Keycloak) ──
│   ├── api/                       #   REST controllers (MeController)
│   └── infrastructure/            #   SecurityConfig, Keycloak role converter
│
├── storage/                       # ── module: file storage (S3/MinIO|local) ──
│   ├── api/                       #   FileController (upload/download/delete)
│   ├── application/               #   StorageService (use cases)
│   ├── domain/                    #   StorageProvider (port), model/ (FileObject)
│   └── infrastructure/            #   StorageProperties, s3/ + local/ adapters
│
└── mail/                          # ── module: email (SMTP|log) ──
    ├── application/               #   MailService
    ├── domain/                    #   MailSender (port), model/ (EmailMessage)
    └── infrastructure/            #   MailProperties, Smtp/Log adapters
```

## The layers inside a module

| Layer            | Contains                                              | Depends on            |
|------------------|-------------------------------------------------------|-----------------------|
| `domain`         | Entities, value objects, **ports** (interfaces), domain rules | nothing (pure Java)   |
| `application`    | Application services / use cases, DTOs                 | `domain`              |
| `infrastructure` | Adapters implementing ports (JPA repos, S3, SMTP), `@ConfigurationProperties`, module `@Configuration` | `domain` (+ frameworks) |
| `api`            | REST controllers, request/response records            | `application`         |

**Dependency rule:** dependencies point *inward* (api → application → domain).
The domain never imports `infrastructure`. Controllers never touch a provider
directly — they go through the application service.

## Pluggable providers (switch via env, no code change)

Both `storage` and `mail` define a **port** in `domain` and ship multiple
adapters in `infrastructure`, selected with `@ConditionalOnProperty`:

- **Storage** — `STORAGE_PROVIDER=s3` (MinIO / AWS / any S3) or `local`
  (filesystem). The S3 adapter talks to MinIO or AWS via the same client
  (endpoint + path-style configurable).
- **Mail** — `MAIL_PROVIDER=smtp` (MailDev / real SMTP) or `log` (no-op, for
  tests/CI).

This is the pattern to copy whenever a capability may have several backends.

## Reference module: `invoice`

The `invoice` module is the **template CRUD slice** — copy it for new domains:

```
invoice/
├── domain/
│   ├── model/Invoice.java          # entity extends BaseEntity
│   ├── model/InvoiceStatus.java    # enum value object
│   └── InvoiceRepository.java      # port (Spring Data JpaRepository)
├── application/
│   ├── dto/InvoiceRequest.java     # inbound DTO (Bean Validation)
│   ├── dto/InvoiceResponse.java    # outbound DTO
│   ├── mapper/InvoiceMapper.java   # plain @Component: entity <-> DTO
│   └── InvoiceService.java         # use cases, @Transactional
└── api/InvoiceController.java      # REST, @Valid, @PreAuthorize for ADMIN
```

Endpoints: `GET/POST /api/invoices`, `GET/PUT/DELETE /api/invoices/{id}`
(`DELETE` requires the `ADMIN` realm role). Backed by Flyway migration
`db/migration/V2__invoice.sql`.

### Mapping (plain Java)

Entity ⇄ DTO mapping is a **hand-written `@Component`** — no library, no code
generation, fully explicit and compiler-checked. Each mapper exposes the same
method set, copy it as-is:

- `toResponse(entity)` — entity → response DTO
- `toList(entities)` — list of entities → list of response DTOs
- `toEntity(request)` — request DTO → new entity (create)
- `updateEntity(request, entity)` — copy non-null fields onto an existing entity (update)

The service injects the mapper and calls these methods; controllers never see
entities. When a copy becomes tedious, that's the moment to consider a mapping
library — not before.

### Adding a new module

1. `mymod/domain/model/Foo.java` extending `shared.domain.BaseEntity`.
2. Port `mymod/domain/FooRepository.java` (extend `JpaRepository`).
3. DTOs `mymod/application/dto/FooRequest.java` + `FooResponse.java`.
4. `mymod/application/mapper/FooMapper.java` (plain `@Component`, the 4 methods above).
5. Use cases in `mymod/application/FooService.java`.
6. REST in `mymod/api/FooController.java` (DTOs in/out, never entities).
7. Flyway migration `db/migration/V3__foo.sql`.

## Configuration & secrets

- Config lives in `application.yml` + `application-{dev,test,prod}.yml`.
- **No secret in YAML** — values come from the environment via `${VAR}`.
- Locally, `spring-dotenv` loads `.env` (gitignored) into those placeholders.
- `.env.example` documents every variable and matches the docker-compose stack.

## Cross-cutting conventions

- Entities extend `BaseEntity` (id, version, audit timestamps).
- Errors: throw `ResourceNotFoundException` / a `DomainException` subclass;
  `GlobalExceptionHandler` renders them as the `ApiError` JSON shape.
- Schema changes go through Flyway — never edit an applied migration.
- Security is JWT/Keycloak; protect endpoints with `@PreAuthorize`.
