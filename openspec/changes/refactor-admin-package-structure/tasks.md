## 1. Inventory

- [x] 1.1 List current admin-only classes and DTOs, including category admin endpoints and the existing `AdminController`.
- [x] 1.2 Confirm public category/auth/user classes that must stay in their current domain packages.

## 2. Package Moves

- [x] 2.1 Move admin-only category controller code under `quynh.ecommerce.moonshop.admin.category`.
- [x] 2.2 Move admin-only category DTOs under `quynh.ecommerce.moonshop.admin.category.dto`.
- [x] 2.3 Keep shared category entity, repository, mapper, and service in `quynh.ecommerce.moonshop.category` unless an admin-only implementation already exists.
- [x] 2.4 Move or split the existing `AdminController` into scoped admin packages without changing `/api/admin/login` or `/api/admin/users`.

## 3. References

- [x] 3.1 Update Java package declarations and imports after file moves.
- [x] 3.2 Verify `UrlConstant.Admin.*`, `SecurityConfig`, and controller mappings still expose the same `/api/admin/**` paths.
- [x] 3.3 Search with `rg` for stale package paths and deleted class references.

## 4. Verification

- [x] 4.1 Run `mvn -q -DskipTests compile`.
- [x] 4.2 Review the final diff to confirm no endpoint path, request/response shape, role rule, error code, or database entity/table change was introduced.
