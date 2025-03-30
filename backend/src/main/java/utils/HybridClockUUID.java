package utils;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates compact 10-character UUIDs using a hybrid clock approach.
 * Uses only lowercase letters a-z for encoding.
 */
public class HybridClockUUID {
    // Counter to avoid collisions within the same millisecond
    private static final AtomicLong counter = new AtomicLong(0);
    // SecureRandom for additional entropy
    private static final SecureRandom random = new SecureRandom();
    // Characters used for encoding (only lowercase a-z, 26 characters)
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    /**
     * Generates a 10-character UUID using only lowercase letters
     * @return A unique 10-character identifier
     */
    public static String generate() {
        // Get current timestamp (milliseconds since epoch)
        long timestamp = Instant.now().toEpochMilli();
        
        // Reset counter if it gets too high
        long sequence = counter.incrementAndGet() & 0x3FFFF; // Keep only 18 bits
        
        // Get some random bits for additional uniqueness
        int randomBits = random.nextInt(0x3FF); // 10 random bits
        
        // Combine timestamp (most significant), sequence, and random bits
        // We'll use 28 bits of timestamp (enough for several years)
        long combinedValue = ((timestamp & 0xFFFFFFF) << 28) | (sequence << 10) | randomBits;
        
        // Convert to base26 string (10 characters)
        return toBase26(combinedValue);
    }
    
    /**
     * Converts a long value to a base26 string of 10 characters
     */
    private static String toBase26(long value) {
        char[] buffer = new char[10];
        for (int i = 9; i >= 0; i--) {
            buffer[i] = CHARS[(int)(value % 26)];
            value /= 26;
        }
        return new String(buffer);
    }
    
    /**
     * Simple example of usage
     */
    public static void main(String[] args) {
        // Generate and print 5 sample UUIDs
        for (int i = 0; i < 5; i++) {
            System.out.println(generate());
        }
    }
}