## Why

Admin APIs are currently starting to mix with public/domain packages, which will get noisy as catalog, order, dashboard, chat, and upload admin endpoints are implemented. Grouping admin-facing controllers and DTOs under `admin` keeps the backend modular monolith readable without changing the public API contract.

## What Changes

- Move admin-only API entrypoints under `quynh.ecommerce.moonshop.admin`, split by content area such as `admin.category`, `admin.product`, `admin.order`, `admin.dashboard`, and `admin.chat` as those APIs exist.
- Keep shared domain entities, repositories, services, and public APIs in their domain packages unless the code is admin-only.
- Keep all external endpoint paths unchanged under `/api/admin/**`.
- Preserve current request/response shapes, roles, error codes, and validation behavior.
- Do not add new infrastructure dependencies.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- None. This is a package-structure refactor only, so `.openspec.yaml` sets `skip_specs: true`.

## Impact

- Affected code: admin controllers, admin request/response DTOs, admin service interfaces/implementations where present, imports, constants, tests, and generated/compiled references.
- APIs: no external API path or payload changes.
- Dependencies: none.
- Compatibility: frontend routes mapped to `/api/admin/**` continue to work unchanged.
