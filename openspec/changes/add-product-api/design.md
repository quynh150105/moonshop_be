## Context

See `proposal.md` for motivation. The backend already has category APIs, `ProductStatus`, shared error handling, URL constants, Spring Security, MapStruct, and JPA conventions. Product APIs should follow the same controller/service/repository layering and return DTOs directly via `ResponseEntity`.

## Goals / Non-Goals

**Goals:**

- Add a minimal product model that satisfies the public and admin product API contract.
- Reuse `Category` for product category ownership and category display fields.
- Keep public reads limited to `ACTIVE` products.
- Keep admin endpoints under `/api/admin/**` and protected by existing ADMIN security rules.

**Non-Goals:**

- No Cloudinary upload implementation.
- No cart, order, review, notification, chat, RabbitMQ, or mail behavior.
- No new dependencies.
- No separate product image/specification tables in this change unless the implementation needs them for the agreed response shape.

## Decisions

- **Use one `Product` entity with a primary image URL/public id instead of image/spec tables for this phase.**
  - Rationale: admin product payload currently sends one `image` and optional `imagePublicId`; public `images` can return a one-item list. This covers the frontend path with less schema and code.
  - Alternative considered: add `ProductImage` and `ProductSpecification` immediately. Rejected until multiple images or dynamic specifications are implemented.

- **Use generated slugs from product names.**
  - Rationale: Product detail accepts id or slug, and duplicate slugs must return `PRODUCT_SLUG_ALREADY_EXISTS`.
  - Alternative considered: accept slug from clients. Rejected to keep the API smaller and consistent with category creation.

- **Use Spring Data `Page` and simple repository queries/specification logic.**
  - Rationale: product list needs pagination and filters, but this scope does not need full-text search or a search engine.
  - Alternative considered: introduce search infrastructure. Rejected as premature for current data size and dependencies.

- **Soft delete by setting `ProductStatus.INACTIVE`.**
  - Rationale: the contract warns that ordered products should not be hard deleted. Soft delete is safe before order integration exists.
  - Alternative considered: hard delete. Rejected because it would be harder to evolve once orders reference products.

## Risks / Trade-offs

- Single-image storage limits richer product galleries -> add `ProductImage` when admin upload/multiple images are required.
- No product specifications in phase one means detail can return an empty map -> add `ProductSpecification` when the frontend needs editable specs.
- Simple `contains` search may be slow on large catalogs -> add indexed/search-backed search only after real volume requires it.

## Migration Plan

1. Add product table fields through Hibernate `ddl-auto=update` in the local project environment.
2. Deploy with no data migration required for existing auth/user/category tables.
3. Rollback by reverting the code change; created product rows can remain unused.
