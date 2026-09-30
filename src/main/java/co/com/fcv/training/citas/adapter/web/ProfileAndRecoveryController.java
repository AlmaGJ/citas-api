package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.Ports;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.*;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
class ProfileAndRecoveryController {
    record Profile(Long id, String firstName, String lastName, String documentType, String documentNumber, String email, String phone) {}
    record PhonePatch(@NotBlank @Size(max = 40) String phone) {}
    record RecoveryRequest(@NotBlank @Size(max = 254) String email) {}
    record ResetRequest(@NotBlank String token, @NotBlank @Size(min = 8, max = 72) String password,
                        @NotBlank String passwordConfirmation) {}
    record MailboxMessage(String email, String token, Instant expiresAt) {}

    private final JdbcTemplate jdbc;
    private final Ports.Passwords passwords;
    private final Clock clock;
    private final boolean localMailboxEnabled;
    private final Map<String, MailboxMessage> mailbox = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    ProfileAndRecoveryController(JdbcTemplate jdbc, Ports.Passwords passwords, Clock clock,
                                 @Value("${app.password-reset.local-enabled:false}") boolean localMailboxEnabled) {
        this.jdbc = jdbc; this.passwords = passwords; this.clock = clock; this.localMailboxEnabled = localMailboxEnabled;
    }

    @GetMapping("/api/v1/users/me")
    @PreAuthorize("isAuthenticated()")
    Profile me(@AuthenticationPrincipal Jwt jwt) {
        return jdbc.queryForObject("select id,first_name,last_name,document_type,document_number,email,phone from users where id=?",
                (rs, n) -> new Profile(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("document_type"), rs.getString("document_number"), rs.getString("email"), rs.getString("phone")), userId(jwt));
    }

    @PatchMapping("/api/v1/users/me")
    @PreAuthorize("isAuthenticated()")
    Profile patch(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PhonePatch body) {
        jdbc.update("update users set phone=? where id=?", body.phone().trim(), userId(jwt));
        return me(jwt);
    }

    @PostMapping("/api/v1/auth/password-recovery")
    ResponseEntity<Void> recovery(@Valid @RequestBody RecoveryRequest body) {
        jdbc.query("select id,email from users where lower(email)=lower(?) and active=true", rs -> {
            String token = newToken(); Instant expires = clock.instant().plus(Duration.ofMinutes(20));
            jdbc.update("insert into password_reset_tokens(user_id,token_hash,expires_at) values (?,?,?)",
                    rs.getLong("id"), hash(token), LocalDateTime.ofInstant(expires, ZoneOffset.UTC));
            if (localMailboxEnabled) mailbox.put(body.email().toLowerCase(), new MailboxMessage(rs.getString("email"), token, expires));
        }, body.email());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/api/v1/auth/password-reset")
    ResponseEntity<Void> reset(@Valid @RequestBody ResetRequest body) {
        if (!body.password().equals(body.passwordConfirmation())) throw new IllegalArgumentException("Las contraseñas no coinciden");
        if (body.password().getBytes(StandardCharsets.UTF_8).length > 72) throw new IllegalArgumentException("Contraseña demasiado larga");
        String tokenHash = hash(body.token());
        var rows = jdbc.query("select id,user_id,expires_at,used_at from password_reset_tokens where token_hash=?", (rs, n) -> {
            if (rs.getTimestamp("expires_at").toInstant().isBefore(clock.instant()) || rs.getTimestamp("used_at") != null)
                throw new IllegalArgumentException("Token inválido o expirado");
            return new long[]{rs.getLong("id"), rs.getLong("user_id")};
        }, tokenHash);
        if (rows.isEmpty()) throw new IllegalArgumentException("Token inválido o expirado");
        long tokenId = rows.get(0)[0], userId = rows.get(0)[1];
        jdbc.update("update users set password_hash=? where id=?", passwords.hash(body.password()), userId);
        jdbc.update("update refresh_tokens set revoked_at=? where user_id=? and revoked_at is null", LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC), userId);
        jdbc.update("update password_reset_tokens set used_at=? where id=?", LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC), tokenId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/admin/local-mailbox")
    @PreAuthorize("hasRole('ADMIN')")
    Object localMailbox() {
        if (!localMailboxEnabled) throw new IllegalStateException("Buzón local deshabilitado");
        mailbox.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(clock.instant()));
        return mailbox.values();
    }

    private long userId(Jwt jwt) { return Long.parseLong(jwt.getSubject()); }
    private String newToken() { byte[] raw = new byte[32]; random.nextBytes(raw); return Base64.getUrlEncoder().withoutPadding().encodeToString(raw); }
    private String hash(String value) { try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); } catch (Exception e) { throw new IllegalStateException(e); } }
}
