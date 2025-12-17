package app.Users.mapper;

import app.Users.DTOs.UserDTO;
import app.Users.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //We don't need to ignore password hash manually since it is not present in the UserDTOs
    UserDTO MapUsertoUserDTO(User user);
}
