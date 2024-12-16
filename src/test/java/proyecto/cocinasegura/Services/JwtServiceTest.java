package proyecto.cocinasegura.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import proyecto.cocinasegura.Services.JwtService;

import java.util.Date;
import java.security.Key;

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
        void testExtractUsernameWithInvalidToken() {
                String invalidToken = "invalid.token";
                assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken),
                                "Should throw an exception for invalid token");
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
        void testTokenExpired() {
                String expiredToken = Jwts.builder()
                                .setSubject("testUser")
                                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                                .compact();

                assertTrue(jwtService.validateToken(expiredToken, User.builder()
                                .username("testUser")
                                .password("testPassword")
                                .roles("USER")
                                .build()) == false, "The token should be expired");
        }

        @Test
        void testTokenWithoutClaims() {
                String tokenWithoutClaims = Jwts.builder()
                                .setSubject("testUser")
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                                .compact();

                assertDoesNotThrow(() -> jwtService.extractUsername(tokenWithoutClaims),
                                "Should not throw an exception for a token without claims");
        }

        @Test
        void testValidateInvalidToken() {
                String invalidToken = "invalid.token.part";
                UserDetails userDetails = User.builder()
                                .username("testUser")
                                .password("testPassword")
                                .roles("USER")
                                .build();

                assertFalse(jwtService.validateToken(invalidToken, userDetails),
                                "Invalid token should return false when validating");
        }

        @Test
        void testInvalidSignature() {
                String invalidSignatureToken = Jwts.builder()
                                .setSubject("testUser")
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                                .signWith(Keys.hmacShaKeyFor(
                                                "wrongSecretKeyForTest123456789012345678901234567890".getBytes()),
                                                SignatureAlgorithm.HS256)
                                .compact();

                assertThrows(Exception.class, () -> jwtService.extractUsername(invalidSignatureToken),
                                "Should throw an exception for a token with an invalid signature");
        }

        @Test
        void testGetSignKey() {
                Key key = jwtService.getSignKey();
                assertNotNull(key, "Signing key should not be null");
        }

        @Test
        void testValidateToken_UsernameNotMatch() {
                UserDetails userDetails = mock(UserDetails.class);
                when(userDetails.getUsername()).thenReturn("expectedUser");
                // Extracted username is different
                when(jwtService.extractUsername("someToken")).thenReturn("differentUser");
                when(jwtService.isTokenExpired("someToken")).thenReturn(false);

                boolean result = jwtService.validateToken("someToken", userDetails);
                assertFalse(result); // covers username.equals(...) == false branch
        }

        @Test
        void testValidateToken_TokenExpired() {
                UserDetails userDetails = mock(UserDetails.class);
                when(userDetails.getUsername()).thenReturn("testUser");
                when(jwtService.extractUsername("someToken")).thenReturn("testUser");
                // Token is expired
                when(jwtService.isTokenExpired("someToken")).thenReturn(true);

                boolean result = jwtService.validateToken("someToken", userDetails);
                assertFalse(result); // covers !isTokenExpired(token) == false branch
        }

        @Test
        void testValidateToken_AllGood() {
                UserDetails userDetails = mock(UserDetails.class);
                when(userDetails.getUsername()).thenReturn("testUser");
                when(jwtService.extractUsername("someToken")).thenReturn("testUser");
                when(jwtService.isTokenExpired("someToken")).thenReturn(false);

                boolean result = jwtService.validateToken("someToken", userDetails);
                assertTrue(result); // covers the true && true scenario
        }

}
