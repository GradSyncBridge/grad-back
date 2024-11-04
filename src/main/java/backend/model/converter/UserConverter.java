package backend.model.converter;

import backend.model.DTO.UserLoginDTO;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    public User UserDTOToUser(UserLoginDTO userLoginDTO);

    UserLoginDTO UserToUserDTO(User user);

}
