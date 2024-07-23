package com.synchronisation.appsynchronisation.controller;

import com.synchronisation.appsynchronisation.dto.USRDto;
import com.synchronisation.appsynchronisation.error.UserAlreadyExistException;
import com.synchronisation.appsynchronisation.error.UserNotFoundException;
import com.synchronisation.appsynchronisation.services.userServices.UserServices;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PostMapping(path = "/users")
    public ResponseEntity<USRDto> createUser(@RequestBody USRDto usrDto) throws UserAlreadyExistException {
        return new ResponseEntity<>(userServices.addUser(usrDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/listUser")
    public ResponseEntity<List<USRDto>> getListAllUser(){
        return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{idUser}")
    public ResponseEntity<USRDto> getUserById(@PathVariable Long idUser) throws UserNotFoundException {
        return new ResponseEntity<>(userServices.getUserById(idUser), HttpStatus.OK);
    }

    @PutMapping(path = "/updateUser/{idUser}")
    public ResponseEntity<USRDto> updateUser(@PathVariable Long idUser, @RequestBody USRDto usrDto) throws UserNotFoundException {
        return new ResponseEntity<>(userServices.updateUser(idUser, usrDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteUser/{idUser}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long idUser) throws UserNotFoundException {
        userServices.deleteUser(idUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/synchronizeUsers")
    public ResponseEntity<Void> SynchronisedUsers(){
        userServices.synchronizeUsers();
        return ResponseEntity.ok().build();
    }
}
