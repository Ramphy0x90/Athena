package r16a.Athena.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import r16a.Athena.exceptions.EntityNotFoundException;
import r16a.Athena.mappers.UserMapper;
import r16a.Athena.models.User;
import r16a.Athena.models.dto.UserRestricted;
import r16a.Athena.repositories.UserRepository;

/**
 * Service for user operations
 *
 * @author Ramphy Aquino Nova
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Get restricted user by a given id
     *
     * @param id user id
     * @return restricted user
     */
    public UserRestricted getRestrictedById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), id));
        return userMapper.userToRestricted(user);
    }
}
