package proyecto.cocinasegura.Services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtServiceTest {

    private JwtService jwtService;
    private String token;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("testPassword")
                .roles("USER")
                .build();
        token = jwtService.generateToken(userDetails.getUsername());
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token, "The token should not be null");
    }

    @Test
    void testExtractUsername() {
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username, "The username should match the one used to generate the token");
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtService.extractExpiration(token);
        assertTrue(expiration.after(new Date()), "The expiration date should be in the future");
    }

    @Test
    void testValidateToken() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("testPassword")
                .roles("USER")
                .build();
        assertTrue(jwtService.validateToken(token, userDetails),
                "The token should be valid for the given user details");
    }

    @Test
    void testIsTokenExpired() throws InterruptedException {
        // Simulating an expired token by setting a short expiration time
        String shortLivedToken = jwtService.generateToken("shortLivedUser");
        Thread.sleep(2000); // Wait for 2 seconds to ensure token expiration
        assertFalse(jwtService.validateToken(shortLivedToken, User.builder()
                .username("shortLivedUser")
                .password("testPassword")
                .roles("USER")
                .build()), "The token should be expired");
    }
}
