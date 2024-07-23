package com.synchronisation.appsynchronisation.services.userServices;

import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.dto.USRDto;
import com.synchronisation.appsynchronisation.error.UserAlreadyExistException;
import com.synchronisation.appsynchronisation.error.UserNotFoundException;

import java.util.List;

public interface UserServices {

    USRDto addUser(USRDto usrDto) throws UserAlreadyExistException;
    List<USRDto> getAllUser();
    USRDto getUserById(Long idUser) throws UserNotFoundException;
    USRDto updateUser(Long idUser, USRDto usrDto) throws UserNotFoundException;
    void deleteUser(Long idUser) throws UserNotFoundException;
    void synchronizeUsers();

}
