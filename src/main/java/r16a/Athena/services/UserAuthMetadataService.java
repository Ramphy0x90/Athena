package r16a.Athena.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import r16a.Athena.models.User;
import r16a.Athena.models.UserAuthMetadata;
import r16a.Athena.repositories.UserAuthMetadataRepository;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserAuthMetadataService {
    private final UserAuthMetadataRepository userAuthMetadataRepository;

    public UserAuthMetadata logAuthAttempt(HttpServletRequest request, User user, boolean isAuthenticated) {
        UserAuthMetadata userAuthMetadata = getAuthMetadataOrCreate(user);
        Integer loginAttempts = userAuthMetadata.getLoginAttempts();
        userAuthMetadata.setLoginAttempts(isAuthenticated ? 0 : loginAttempts + 1);
        userAuthMetadata.setLastLogin(LocalDateTime.now());
        userAuthMetadata.setLastLoginSourceIp(getSourceIpFromRequest(request));
        userAuthMetadataRepository.save(userAuthMetadata);

        return userAuthMetadata;
    }

    private UserAuthMetadata getAuthMetadataOrCreate(User user) {
        Optional<UserAuthMetadata> userAuthMetadata = userAuthMetadataRepository.findByUserId(user.getId());

        return userAuthMetadata.orElseGet(() -> UserAuthMetadata.builder()
                .loginAttempts(0)
                .lastLogin(null)
                .lastLoginSourceIp(null)
                .build());
    }

    private String getSourceIpFromRequest(HttpServletRequest request) {
        String sourceIp = request.getHeader("X-Forwarded-For");
        if (sourceIp == null || sourceIp.isEmpty()) {
            sourceIp = request.getRemoteAddr();
        }

        return sourceIp;
    }
}
