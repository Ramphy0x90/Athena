package r16a.Athena.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import r16a.Athena.models.User;
import r16a.Athena.models.dto.UserAuthenticate;
import r16a.Athena.models.dto.UserAuthenticated;
import r16a.Athena.models.dto.UserRegister;
import r16a.Athena.models.dto.UserRestricted;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserRestricted userToRestricted(User user) {
        return modelMapper.map(user, UserRestricted.class);
    }

    public UserAuthenticate userRegisterToAuthenticate(UserRegister userRegister) {
        if (userRegister == null) {
            return null;
        }

        UserAuthenticate userAuthenticate = new UserAuthenticate();
        userAuthenticate.setUsernameEmail(userRegister.getEmail());
        userAuthenticate.setPassword(userRegister.getPassword());

        return userAuthenticate;
    }

    public User UserRestrictedToUser(UserRestricted userRestricted) {
        return modelMapper.map(userRestricted, User.class);
    }

    public UserAuthenticated userToAuthenticated(User user) {
        return modelMapper.map(user, UserAuthenticated.class);
    }
}
