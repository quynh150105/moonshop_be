package quynh.ecommerce.moonshop.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {
    @Test
    void productEndpointsKeepExpectedAccessRules() throws Exception {
        SecurityConfig securityConfig = new SecurityConfig(null, null, null);

        assertThat(values(securityConfig, "PUBLIC_ENDPOINTS"))
                .contains("/api/products", "/api/products/**");
        assertThat(values(securityConfig, "ADMIN_ENDPOINTS"))
                .contains("/api/admin/**");
    }

    private String[] values(SecurityConfig securityConfig, String fieldName) throws Exception {
        Field field = SecurityConfig.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return Arrays.copyOf((String[]) field.get(securityConfig), ((String[]) field.get(securityConfig)).length);
    }
}
