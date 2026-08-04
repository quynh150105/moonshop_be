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

    }


}
