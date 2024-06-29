package com.hisham.HomeCentre.constants;

import java.util.Set;

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

    public static class ImageUpload{
        public static final Set<String> ALLOWED_FILE_TYPES = Set.of("image/jpeg", "image/png", "image/gif");
        public static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    }

    public static class Redis{
        public static final String PRODUCT_KEY_PREFIX = "product:";
        public static final String CATEGORY_KEY_PREFIX = "category:";
        public static final String REVIEW_KEY_PREFIX = "review:";
        public static final Long TIME_TO_LIVE = 3600L; // Cached for one hour
    }
}
