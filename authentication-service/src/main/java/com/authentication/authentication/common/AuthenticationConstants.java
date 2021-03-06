package com.authentication.authentication.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationConstants {

    public static final String AUTHENTICATION_PREFIX = "authentication";
    public static final String AUTHENTICATION_VERSION = "v1";
    public static final String AUTHENTICATION_BASE_URL = AUTHENTICATION_PREFIX + "/" + AUTHENTICATION_VERSION;
}
