package quynh.ecommerce.moonshop.common.constanst;

public class UrlConstant {
    public static class Auth{
        private static final String PRE_FIX = "/auth";
        public static final String LOGIN = PRE_FIX + "/login";
        public static final String REFRESH_TOKEN = PRE_FIX + "/refresh-token";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String INTROSPECT = PRE_FIX + "/introspect";
        public static final String REGISTER = PRE_FIX + "/register";
        public static final String ME = PRE_FIX + "/me";

        private Auth(){}
    }

    public static class User {
        private static final String PRE_FIX = "/user";
        public static final String GET_PROFILE = PRE_FIX + "/profile";
        public static final String UPDATE_PROFILE = PRE_FIX + "/profile";
        public static final String CHANGE_PASSWORD = PRE_FIX + "/change-password";
        private User() {
        }
    }

    public static class Admin {
        private static final String PRE_FIX = "/admin";
        public static final String LOGIN = PRE_FIX + "/login";
        public static final String ALL_USER = PRE_FIX + "/users";
        public static final String CATEGORIES = PRE_FIX + "/categories";
        public static final String CATEGORY_BY_ID = CATEGORIES + "/{id}";
        public static final String PRODUCTS = PRE_FIX + "/products";
        public static final String PRODUCT_BY_ID = PRODUCTS + "/{id}";

    }

    public static class Category {
        public static final String CATEGORIES = "/categories";

        private Category() {
        }
    }

    public static class Product {
        public static final String PRODUCTS = "/products";
        public static final String PRODUCT_BY_ID_OR_SLUG = PRODUCTS + "/{idOrSlug}";
        public static final String SEARCH = PRODUCTS + "/search";
        public static final String SUGGESTIONS = PRODUCTS + "/suggestions";

        private Product() {
        }
    }

    public static class Cart {
        public static final String CART = "/cart";
        public static final String CART_ITEM = CART + "/{productId}";
        public static final String CART_ITEM_SELECTED = CART_ITEM + "/selected";
        public static final String SELECT_ALL = CART + "/select-all";
        public static final String MERGE = CART + "/merge";
        public static final String VALIDATE = CART + "/validate";

        private Cart() {
        }
    }

}
