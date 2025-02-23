package r16a.Athena.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import r16a.Athena.exceptions.*;
import r16a.Athena.mappers.UserMapper;
import r16a.Athena.config.AppConfig;
import r16a.Athena.config.Jwt.JwtUtil;
import r16a.Athena.models.Client;
import r16a.Athena.models.User;
import r16a.Athena.models.UserAuthMetadata;
import r16a.Athena.models.dto.*;
import r16a.Athena.repositories.ClientRepository;
import r16a.Athena.repositories.UserRepository;
import r16a.Athena.util.EmailUtil;

import java.nio.CharBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Service for authentication operations
 * Register, authenticate, send password recovery link and recover password
 * operations
 *
 * @author Ramphy Aquino Nova
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final UserAuthMetadataService userAuthMetadataService;
    private final PasswordEncoder passwordEncoder;
    private final Supplier<String> secretGenerator;
    private final UserMapper userMapper;
    private final AppConfig appConfig;
    private final JwtUtil jwtUtil;
    private final EmailUtil emailUtil;

    private final static int EMAIL_LINK_EXPIRATION_IN_MINUTES = 10;
    private final static int MAX_AUTH_ATTEMPTS = 3;

    /**
     * Register a new user. Check if a user exists with the
     * given user email, if exists throw
     *
     * @param newUser new user data
     * @return generated user
     */
    public UserRegistered register(HttpServletRequest request, UserRegister newUser, Integer clientId) {
        Optional<Client> client = clientRepository.findById(clientId);

        if(client.isEmpty()) {
            throw new ClientNotFoundException(clientId);
        }

        Optional<User> optUser = userRepository.findByEmailOrUserName(newUser.getEmail());

        // If a user already exists, check if the credentials are correct, then,
        // check if it exists for the same or a different client, if is the same
        // client, notify that the user already exists, if is for a different
        // client, update the user with the new client and send confirmation email
        if (optUser.isPresent()) {
            log.info("User {} already exists, checking credentials...", newUser.getEmail());

            try {
                UserAuthenticate credentials = userMapper.userRegisterToAuthenticate(newUser);
                authenticate(request, credentials, clientId);

                boolean isExistingClient = optUser.get().getClients().stream().anyMatch(existingClient -> existingClient.getId().equals(clientId));
                if(!isExistingClient) {
                    long tokenDuration = TimeUnit.MINUTES.toMillis(EMAIL_LINK_EXPIRATION_IN_MINUTES);
                    String tempToken = jwtUtil.generateToken(optUser.get(), tokenDuration);
                    String signUpConfirm = appConfig.getAppBaseUrl() + ":" + appConfig.getServerPort() + "/" + clientId + "/confirm-signup-new-client/" + tempToken;

                    emailUtil.send(
                            optUser.get().getEmail(),
                            "New client sign up confirmation",
                            "<strong>Click the following link to confirm your registration:</strong>" +
                                    "<a href='" + signUpConfirm + "'>Confirm registration</a><br><br>" +
                                    "<i>This link will be valid only for " + EMAIL_LINK_EXPIRATION_IN_MINUTES + " minutes</i>"
                    );

                    return UserRegistered.builder()
                            .redirectUrl("/" + clientId + "/register-in-new-client")
                            .user(userMapper.userToRestricted(userRepository.save(optUser.get())))
                            .build();
                }

                throw new UserAlreadyExistsException(newUser.getEmail());
            } catch(AuthInvalidCredentialsException e) {
                throw new UserAlreadyExistsException(newUser.getEmail());
            }
        }

        User user = User.builder()
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .secret(secretGenerator.get())
                .userName(newUser.getUserName())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .clients(new HashSet<>(List.of(client.get())))
                .build();

        return UserRegistered.builder()
                .redirectUrl("/" + clientId + "/login")
                .user(userMapper.userToRestricted(userRepository.save(user)))
                .build();
    }

    public void registerInClient(String usernameEmail, Integer code) {

    }

    /**
     * Authenticate internal user (admin). Check if user exists and is not
     * locked. Return user data with jwt token attached
     *
     * @param credentials user credentials
     * @return user data with token
     */
    public UserAuthenticated authenticateInternal(HttpServletRequest request, UserAuthenticate credentials) {
        return authenticate(request, credentials, null, true);
    }

    /**
     * Authenticate user. Check if user exists and is not
     * locked. Return user data with jwt token attached
     *
     * @param credentials user credentials
     * @return user data with token
     */
    public UserAuthenticated authenticate(HttpServletRequest request, UserAuthenticate credentials, Integer clientId) {
        return authenticate(request, credentials, clientId, false);
    }

    public UserAuthenticated authenticate(HttpServletRequest request, UserAuthenticate credentials, @Nullable Integer clientId, boolean internal) {
        if(clientId == null && !internal) {
            throw new ClientNotFoundException(-1);
        }

        Optional<User> optUser;
        if(internal) {
            optUser = userRepository.findAdminByEmailOrUserName(credentials.getUsernameEmail());
        } else {
            optUser = userRepository.findByEmailOrUserNameAndClientId(credentials.getUsernameEmail(), clientId);
        }

        if(optUser.isEmpty()) {
            throw new EntityNotFoundException(User.class.getSimpleName(), "email", credentials.getUsernameEmail());
        }

        User user = optUser.get();
        if (user.getStatus().equals(User.UserStatus.LOCKED)) {
            throw new AuthUserDisabledLockedException();
        }

        if (passwordEncoder.matches(CharBuffer.wrap(credentials.getPassword()), user.getPassword())) {
            UserAuthenticated userAuthenticated = userMapper.userToAuthenticated(user);
            userAuthenticated.setToken(jwtUtil.generateToken(user));

            log.info("User {} authenticated", user.getEmail());
            userAuthMetadataService.logAuthAttempt(request, user, true);
            return userAuthenticated;
        }

        UserAuthMetadata userAuthMetadata = userAuthMetadataService.logAuthAttempt(request, user, false);
        if(userAuthMetadata.getLoginAttempts() == MAX_AUTH_ATTEMPTS) {
            user.setStatus(User.UserStatus.LOCKED);
            userRepository.save(user);
        }

        throw new AuthInvalidCredentialsException();
    }

    /**
     * Send password recovery link to a give user email, check if the email
     * is valid and generate the password recovery link, the link is valid
     * only for 10 minutes
     *
     * @param userPasswordRecoverLink data to identify user
     */
    public void sendPasswordRecoveryLink(UserPasswordRecoverLink userPasswordRecoverLink, Integer clientId) {
        User user = userRepository.findByEmailOrUserNameAndClientId(userPasswordRecoverLink.getUsernameEmail(), clientId).orElseThrow(
                () -> new EntityNotFoundException(User.class.getSimpleName(), "email", userPasswordRecoverLink.getUsernameEmail())
        );

        user.setStatus(User.UserStatus.LOCKED);
        userRepository.save(user);

        long tokenDuration = TimeUnit.MINUTES.toMillis(EMAIL_LINK_EXPIRATION_IN_MINUTES);
        String tempToken = jwtUtil.generateToken(user, tokenDuration);
        String resetPasswordLink = appConfig.getAppBaseUrl() + ":" + appConfig.getServerPort() + "/" + clientId + "/password-recovery/" + tempToken;

        emailUtil.send(
                user.getEmail(),
                "Password recovery link",
                "<strong>Click the following link to reset your password:</strong>" +
                        "<a href='" + resetPasswordLink + "'>Password reset</a><br><br>" +
                        "<i>This link will be valid only for " + EMAIL_LINK_EXPIRATION_IN_MINUTES + " minutes</i>"
        );
    }

    /**
     * Recover user password, check if the user exists and the password
     * are the same, if true update the user password
     *
     * @param passwordRecover user password recover data
     * @return restricted user
     */
    public UserRestricted recoverPassword(UserPasswordRecover passwordRecover) {
        User user = userRepository.findByEmailOrUserName(passwordRecover.getUsernameEmail()).orElseThrow(
                () -> new EntityNotFoundException(User.class.getSimpleName(), "userName", passwordRecover.getUsernameEmail())
        );

        if (passwordRecover.getPassword().equals(passwordRecover.getPasswordCheck())) {
            user.setPassword(passwordEncoder.encode(passwordRecover.getPassword()));
            userRepository.save(user);
            log.info("User {} password updated", user.getEmail());
        }

        return userMapper.userToRestricted(user);
    }
}
