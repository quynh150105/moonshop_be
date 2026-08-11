## 1. Domain

- [x] 1.1 Add `Product` entity with category relation, pricing, stock, status, image fields, flags, counters, and timestamps.
- [x] 1.2 Add indexes/unique constraints for slug, status/category filters, and created-at sorting.

## 2. Repository

- [x] 2.1 Add `ProductRepository`.
- [x] 2.2 Add repository support for active public list/detail/search/suggestions.
- [x] 2.3 Add repository support for admin list filters and slug conflict checks.

## 3. DTO / Mapper

- [x] 3.1 Add public product list/detail/search response DTOs.
- [x] 3.2 Add admin product create/update request DTOs.
- [x] 3.3 Add admin product response DTO.
- [x] 3.4 Add `ProductMapper` for entity-to-response mapping.

## 4. Service

- [x] 4.1 Implement public product list with filters and pagination.
- [x] 4.2 Implement public product detail by id or slug.
- [x] 4.3 Implement public product search and suggestions.
- [x] 4.4 Implement admin product listing.
- [x] 4.5 Implement admin product create/update with category lookup, slug generation, slug conflict checks, and price validation.
- [x] 4.6 Implement admin product delete as status `INACTIVE`.

## 5. Controller / Config

- [x] 5.1 Add public `ProductController` endpoints under `/api/products`.
- [x] 5.2 Add `AdminProductController` endpoints under `/api/admin/products`.
- [x] 5.3 Add product URL constants.
- [x] 5.4 Add product error codes required by the spec.
- [x] 5.5 Verify existing security rules expose public product reads and protect admin product APIs.

## 6. Verification

- [x] 6.1 Add focused checks for happy-path public product reads.
- [x] 6.2 Add focused checks for admin validation and business errors.
- [x] 6.3 Add focused checks for ADMIN-only product mutation authorization.
- [x] 6.4 Run `mvn -q -DskipTests compile`.
- [x] 6.5 Review implementation against `BACKEND_CONTRACT.md` and the product spec.
