package com.synchronisation.appsynchronisation.mapper;

import com.synchronisation.appsynchronisation.dto.USRDto;
import com.synchronisation.appsynchronisation.models.USR;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    USR mapUserDtoToUser(USRDto usrDto);
    USRDto mapUserToUserDto(USR usr);
}
