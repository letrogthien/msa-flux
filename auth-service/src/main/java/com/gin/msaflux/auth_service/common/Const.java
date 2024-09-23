package com.gin.msaflux.auth_service.common;

public final class Const {
    private Const() {}
    public static final String AUTH_PART_LOGIN = "/api/v1/auth/login";
    public static final String AUTH_PART_REGISTER = "/api/v1/auth/register";
    public static final String AUTH_PART_FORGET_PASSWORD = "/api/v1/auth/password/forget";
    public static final String AUTH_PART_CHANGE_PASSWORD = "/api/v1/auth/password/change";
    public static final String AUTH_PART_REFRESH_TOKEN = "/api/v1/auth/refresh/token";
    public static final String AUTH_PART_GET_PROFILE = "/api/v1/auth/profile/{id}";
    public static final String AUTH_PART_UPDATE_PROFILE = "/api/v1/auth/profile/update";

}
