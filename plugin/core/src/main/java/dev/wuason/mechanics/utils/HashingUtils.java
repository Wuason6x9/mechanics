package dev.wuason.mechanics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {

    /**
     * Hashes the input using the specified hash type.
     *
     * @param file     the file to hash
     * @param hashType the hash type to use
     * @return the hashed input
     */

    public static String hash(File file, HashType hashType) {
        try (FileInputStream fis = new FileInputStream(file)) {
            try (DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance(hashType.getAlgorithm()))) {
                while (dis.read() != -1);
                return NumberUtils.bytesToHex(dis.getMessageDigest().digest());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hashes the input using the specified hash type.
     *
     * @param input    the input to hash
     * @param hashType the hash type to use
     * @return the hashed input
     */

    public static String hash(String input, HashType hashType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType.getAlgorithm());

            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return NumberUtils.bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hashes the input using the specified hash type.
     *
     * @param input    the input to hash
     * @param hashType the hash type to use
     * @return the hashed input
     */
    
    public static String hash(byte[] input, HashType hashType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType.getAlgorithm());
            byte[] encodedHash = digest.digest(input);
            return NumberUtils.bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    

    public enum HashType {
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA512("SHA-512");

        private final String algorithm;

        HashType(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }
}
