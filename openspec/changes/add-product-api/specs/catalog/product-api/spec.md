## Purpose

Product APIs let the LovaMart frontend browse, search, inspect, and manage products through stable public and admin REST endpoints.

## ADDED Requirements

### Requirement: Public product listing
The system SHALL expose `GET /api/products` as a public endpoint that returns paged active products. The endpoint MUST support optional `page`, `size`, `category`, `sort`, `q`, `minPrice`, and `maxPrice` query parameters.

#### Scenario: List active products
- **WHEN** a client requests `GET /api/products`
- **THEN** the system returns a paged response containing active products only.

#### Scenario: Filter product list
- **WHEN** a client requests `GET /api/products` with supported filter query parameters
- **THEN** the system returns active products matching those filters.

### Requirement: Public product detail
The system SHALL expose `GET /api/products/{idOrSlug}` as a public endpoint that returns one active product by id or slug.

#### Scenario: Product exists
- **WHEN** a client requests an existing active product by id or slug
- **THEN** the system returns the product detail response.

#### Scenario: Product missing or inactive
- **WHEN** a client requests a missing or inactive product
- **THEN** the system returns `PRODUCT_NOT_FOUND`.

### Requirement: Public product search helpers
The system SHALL expose public endpoints for product search results and product name suggestions.

#### Scenario: Search products
- **WHEN** a client requests `GET /api/products/search?q=ao`
- **THEN** the system returns matching active product summaries.

#### Scenario: Suggest product names
- **WHEN** a client requests `GET /api/products/suggestions?q=ao`
- **THEN** the system returns matching product name suggestions.

### Requirement: Admin product listing
The system SHALL expose `GET /api/admin/products` as an ADMIN-only endpoint that returns paged products. The endpoint MUST support optional `q`, `categoryId`, `sort`, `page`, and `size` query parameters.

#### Scenario: Admin lists products
- **WHEN** an authenticated ADMIN requests `GET /api/admin/products`
- **THEN** the system returns a paged admin product response.

#### Scenario: Non-admin lists products
- **WHEN** a non-admin client requests `GET /api/admin/products`
- **THEN** the system rejects the request with an authorization error.

### Requirement: Admin product mutation
The system SHALL expose ADMIN-only endpoints to create, update, and delete products under `/api/admin/products`.

#### Scenario: Create product
- **WHEN** an authenticated ADMIN submits a valid product create request
- **THEN** the system creates the product and returns the admin product response with status `201`.

#### Scenario: Update product
- **WHEN** an authenticated ADMIN submits a valid product update request for an existing product
- **THEN** the system updates the product and returns the admin product response.

#### Scenario: Delete product
- **WHEN** an authenticated ADMIN deletes an existing product
- **THEN** the system deletes or deactivates the product and returns status `204`.

#### Scenario: Product validation fails
- **WHEN** an authenticated ADMIN submits invalid product data
- **THEN** the system returns `VALIDATION_ERROR`.

#### Scenario: Category is missing
- **WHEN** an authenticated ADMIN references a missing category
- **THEN** the system returns `CATEGORY_NOT_FOUND`.

#### Scenario: Product slug conflicts
- **WHEN** an authenticated ADMIN creates or updates a product to a duplicate slug
- **THEN** the system returns `PRODUCT_SLUG_ALREADY_EXISTS`.
