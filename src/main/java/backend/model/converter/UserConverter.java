package backend.model.converter;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.user.UserProfileVO;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User UserLoginDTOToUser(UserLoginDTO userLoginDTO);

    UserLoginDTO UserToUserLoginDTO(User user);

    @Mapping(target = "uid", source = "id")
    UserProfileVO UserToUserProfileVO(User user);

    User UserRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

    User UserProfileUpdateDTOToUser(UserProfileUpdateDTO userProfileUpdateDTO);
}
