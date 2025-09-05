package dev.d64.keycloak.sha512whirlpool;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.UUID;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 */
public class Sha512WhirlpoolPasswordHashProvider implements PasswordHashProvider {
    private final int defaultIterations;
    private final String providerId;

    public Sha512WhirlpoolPasswordHashProvider(final String providerId, final int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        final int policyHashIterations = policy.getHashIterations() == -1 ? defaultIterations : policy.getHashIterations();

        return credential.getPasswordCredentialData().getHashIterations() == policyHashIterations
                && providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        byte[] salt = generateSalt();
        String encodedPassword = encodeWithSalt(rawPassword, new String(salt));
        return PasswordCredentialModel.createFromValues(providerId, salt, iterations, encodedPassword);
    }

    private static byte[] generateSalt() {
        // could be a more secure salt, but we want to stay compatible with Humhub for now
        return UUID.randomUUID().toString().toLowerCase().getBytes();
    }

    @Override
    @Deprecated(forRemoval = true)
    public final String encode(String rawPassword, int iterations) {
        byte[] salt = generateSalt();
        return encodeWithSalt(rawPassword, new String(salt));

    }

    public static String encodeWithSalt(final String rawPassword, final String salt) {

        Security.addProvider(new BouncyCastleProvider());
        // hash a string with bouncycastle using whirlpool and sha512
        String saltedPassword = rawPassword + salt;
        String whirlpoolDigest = "";
        String sha512Digest = "";

        MessageDigest crypt;

        try {
            crypt = MessageDigest.getInstance("Whirlpool");
            crypt.update(saltedPassword.getBytes(StandardCharsets.UTF_8));
            whirlpoolDigest = Hex.toHexString(crypt.digest()).toLowerCase();

            crypt.reset();
            crypt = MessageDigest.getInstance("SHA-512");
            crypt.update(whirlpoolDigest.getBytes(StandardCharsets.UTF_8));
            sha512Digest = Hex.toHexString(crypt.digest()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sha512Digest;
    }

    @Override
    public void close() {
        // we have no resources to close

    }

    @Override
    public final boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        final String hash = credential.getPasswordSecretData().getValue();
        final byte[] salt = credential.getPasswordSecretData().getSalt();

        final String saltString = new String(salt);

        return encodeWithSalt(rawPassword, saltString).equals(hash);
    }
}
