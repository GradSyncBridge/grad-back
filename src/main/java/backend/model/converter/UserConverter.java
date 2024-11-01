package backend.model.converter;

import backend.model.DTO.UserDTO;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User UserDTOToUser(UserDTO userDTO);

    UserDTO UserToUserDTO(User user);

}
