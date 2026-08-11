## Why

The LovaMart frontend needs backend product APIs for browsing, search, detail pages, and admin product management. This change implements the product sections from `API_SPEC_DETAILED.md` after the category API foundation is available.

## What Changes

- Add public product endpoints under `/api/products` for listing, detail, search, and suggestions.
- Add admin product CRUD endpoints under `/api/admin/products`.
- Add product persistence and API payloads for product fields required by the frontend contract.
- Use existing category records for product category references.
- Keep upload, cart, order, review, notification, chat, and RabbitMQ behavior out of scope.
- Do not add new infrastructure dependencies.

## Capabilities

### New Capabilities

- `catalog/product-api`: Public and admin product APIs for browsing products and managing product records.

### Modified Capabilities

- None.

## Impact

- Affected code: product package, admin product package, category lookup usage, URL constants, error codes, and security configuration if needed.
- APIs: adds `/api/products/**` and `/api/admin/products/**`.
- Dependencies: none.
- Compatibility: follows `API_SPEC_DETAILED.md` product request/response shapes, ADMIN-only mutation rules, validation rules, and product error codes within this scope.
