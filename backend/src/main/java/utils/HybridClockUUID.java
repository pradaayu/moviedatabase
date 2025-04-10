package utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates compact UUIDs of variable length using a hybrid clock approach.
 * Uses only lowercase letters a-z for encoding.
 */
public class HybridClockUUID {
    // Counter to avoid collisions within the same millisecond
    private static final AtomicLong counter = new AtomicLong(0);
    // SecureRandom for additional entropy
    private static final SecureRandom random = new SecureRandom();
    // Characters used for encoding (only lowercase a-z, 26 characters)
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    // Default length if not specified
    private static final int DEFAULT_LENGTH = 10;

    /**
     * Generates a UUID of the default length (10 characters) using only lowercase letters
     * @return A unique identifier of default length
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * Generates a UUID of specified length using only lowercase letters
     * @param length The length of the UUID to generate (must be at least 5)
     * @return A unique identifier of the specified length
     * @throws IllegalArgumentException if length is less than 5
     */
    public static String generate(int length) {
        // Validate length
        if (length < 5) {
            throw new IllegalArgumentException("UUID length must be at least 5 characters");
        }

        // Get current timestamp (milliseconds since epoch)
        long timestamp = Instant.now().toEpochMilli();

        // Reset counter if it gets too high (adjust based on length)
        long sequence = counter.incrementAndGet() & 0x3FFFF; // Keep only 18 bits

        // Calculate how many random bits we need based on the length
        // Each base26 character encodes ~4.7 bits (log2(26) ≈ 4.7)
        int timestampBits = Math.min(28, (int)(length * 4.7 * 0.5)); // Use about half for timestamp
        int sequenceBits = Math.min(18, (int)(length * 4.7 * 0.3)); // Use about 30% for sequence
        int randomBits = (int)(length * 4.7) - timestampBits - sequenceBits; // Rest for random

        // Get random bits for additional uniqueness
        long randomValue = 0;
        if (randomBits > 0) {
            int randomBytes = (randomBits + 7) / 8; // Convert bits to bytes, rounding up
            byte[] randomBuffer = new byte[randomBytes];
            random.nextBytes(randomBuffer);
            
            // Convert bytes to a long value
            for (int i = 0; i < randomBuffer.length && i < 8; i++) {
                randomValue = (randomValue << 8) | (randomBuffer[i] & 0xFF);
            }
            // Mask to keep only the bits we need
            randomValue = randomValue & ((1L << randomBits) - 1);
        }

        // Combine timestamp (most significant), sequence, and random bits
        long combinedValue = ((timestamp & ((1L << timestampBits) - 1)) << (sequenceBits + randomBits)) | 
                             ((sequence & ((1L << sequenceBits) - 1)) << randomBits) |
                             randomValue;

        // Convert to base26 string of specified length
        return toBase26(combinedValue, length);
    }

    /**
     * Converts a long value to a base26 string of specified length
     */
    private static String toBase26(long value, int length) {
        char[] buffer = new char[length];
        
        // Fill the buffer from right to left
        for (int i = length - 1; i >= 0; i--) {
            buffer[i] = CHARS[(int)(value % 26)];
            value /= 26;
        }
        
        // If we still have value left, we need to incorporate it somehow
        if (value > 0) {
            // XOR the remaining value with some characters to maintain uniqueness
            for (int i = 0; i < Math.min(8, length); i++) {
                int idx = (int)(value % 26);
                buffer[i] = CHARS[((buffer[i] - 'a') ^ idx) % 26];
                value /= 26;
                if (value == 0) break;
            }
        }
        
        return new String(buffer);
    }
}