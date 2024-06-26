package com.hisham.HomeCentre.constants;

public final class AppConstants {
    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String API_BASE_PATH = "/api/v1";

    public static class ErrorMessages {
        public static final String PRODUCT_NOT_FOUND = "Product not found.";
        public static final String CATEGORY_NOT_FOUND = "Category not found.";
    }

    public static class ValidationPatterns {
        public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
        public static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$";
    }

    public static class Defaults {
        public static final String PAGE_NUMBER = "0";
        public static final String PAGE_SIZE = "10";
        public static final String SORT_BY = "createdAt";
        public static final String SORT = "DESC";
        public static final int MAX_PAGE_SIZE = 50;
    }
}