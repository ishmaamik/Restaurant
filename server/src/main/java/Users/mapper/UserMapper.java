package Users.mapper;

import Users.DTOs.UserDTO;
import Users.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO MapUsertoUserDTO(User user);
}
