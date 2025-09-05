package dev.d64.keycloak.sha512whirlpool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.models.credential.PasswordCredentialModel;

import static org.junit.jupiter.api.Assertions.*;

class Sha512WhirlpoolPasswordHashProviderTest {
    private final int iterations = 1;
    private final String id = "sha512whirlpool";
    private final Sha512WhirlpoolPasswordHashProvider provider = new Sha512WhirlpoolPasswordHashProvider(id, iterations);

    @Test
    @DisplayName("Should hash the password successfully")
    void shouldHashThePasswordSuccessfully() {
        String rawPassword = "2rhQc90bCmN43HoQRLHd3rvUAgyLB1";
        String salt = "a203ef54-3fe2-412d-ab1e-59f9d40320f6";
        String hashedPassword = Sha512WhirlpoolPasswordHashProvider.encodeWithSalt(rawPassword, salt);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, salt.getBytes(), iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }
}
