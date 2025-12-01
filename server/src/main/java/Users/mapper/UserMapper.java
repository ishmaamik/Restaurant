package Users.mapper;

import Users.DTOs.UserDTO;
import Users.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //We don't need to ignore password hash manually since it is not present in the UserDTOs
    UserDTO MapUsertoUserDTO(User user);
}
