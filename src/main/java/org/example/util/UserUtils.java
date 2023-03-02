package org.example.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.RegisterRequest;


public class UserUtils {
    public static RegisterRequest getUniqueUser() {
        return new RegisterRequest(
                RandomStringUtils.randomAlphanumeric(10) + "@test.com",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10)
        );
    }
}
