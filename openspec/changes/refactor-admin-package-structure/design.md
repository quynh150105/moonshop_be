## Context

The backend is a Spring Boot modular monolith with packages by capability. Current and upcoming admin endpoints share the `/api/admin/**` surface from `BACKEND_CONTRACT.md`, but some admin-only code can drift into public/domain packages as catalog and order work grows.

There are existing uncommitted category API files. This refactor must move only admin-facing category pieces when applied and preserve that work instead of rewriting it.

## Goals / Non-Goals

**Goals:**

- Put admin-only controllers and admin request/response DTOs under `quynh.ecommerce.moonshop.admin.<area>`.
- Keep public controllers and shared domain logic in domain packages such as `category`, `product`, `order`, and `chat`.
- Keep endpoint paths, request bodies, response bodies, auth roles, validation, and error codes unchanged.
- Leave package boundaries obvious for future admin areas: `admin.category`, `admin.product`, `admin.order`, `admin.dashboard`, and `admin.chat`.

**Non-Goals:**

- Do not implement new admin APIs.
- Do not change database entities, table names, repositories, or migrations.
- Do not change JWT claims, role checks, security rules, or frontend route mappings.
- Do not add dependencies.

## Decisions

- **Admin-only code moves under `admin.<area>`; shared code stays in domain packages.**
  - Rationale: admin controllers and admin DTOs are presentation/API concerns, while repositories/entities/services often represent shared business concepts.
  - Alternative considered: move entire domain modules under `admin`. Rejected because category/product/order entities and repositories are also needed by public or user APIs.

- **Keep services shared unless their behavior is admin-only.**
  - Rationale: category CRUD and public category list can share one category service without duplicating slug validation and persistence rules.
  - Alternative considered: create separate admin service interfaces for every area immediately. Rejected until behavior actually diverges.

- **Use package organization rather than URL logic to express admin ownership.**
  - Rationale: `@RestApiV1` and `UrlConstant.Admin.*` already keep paths stable; package moves should not alter routing.
  - Alternative considered: add new base annotations like `@AdminApiV1`. Rejected as extra abstraction with no current payoff.

- **No spec delta.**
  - Rationale: the refactor changes implementation structure only. External API behavior from `BACKEND_CONTRACT.md` remains unchanged.

## Risks / Trade-offs

- Import churn can break MapStruct or Spring component scanning -> run Maven compile after moves.
- Moving DTO packages can break callers silently if imports are missed -> use `rg` for old package paths and compile.
- Admin package can become a dumping ground -> split by content area immediately, but only for areas with real code.
- Existing uncommitted category work may be in progress -> move files without changing behavior and review diff before compile.

## Migration Plan

1. Move admin-only category controller and admin category DTOs into `admin.category`.
2. Move the existing admin login/user controller code into a scoped admin package only if doing so does not change auth behavior.
3. Update imports and URL constant references.
4. Search for old package references.
5. Run `mvn -q -DskipTests compile`.
6. Rollback is a pure file move revert because no schema or API behavior changes are planned.
