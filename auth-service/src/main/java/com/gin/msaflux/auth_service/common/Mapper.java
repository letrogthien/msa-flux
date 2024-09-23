package com.gin.msaflux.auth_service.common;


import com.gin.msaflux.auth_service.dtos.UserDto;
import com.gin.msaflux.auth_service.models.User;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@org.mapstruct.Mapper
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUser(UserDto userDto);
}
