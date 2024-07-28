package com.synchronisation.appsynchronisation.services.userServices;

import com.synchronisation.appsynchronisation.dao.UserRepository;
import com.synchronisation.appsynchronisation.dto.USRDto;
import com.synchronisation.appsynchronisation.error.UserAlreadyExistException;
import com.synchronisation.appsynchronisation.error.UserNotFoundException;
import com.synchronisation.appsynchronisation.mapper.IUserMapper;
import com.synchronisation.appsynchronisation.models.Note;
import com.synchronisation.appsynchronisation.models.USR;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.synchronisation.appsynchronisation.utils.Utils.USER_ALREADY_EXIST;
import static com.synchronisation.appsynchronisation.utils.Utils.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;
    private final IUserMapper userMapper;
    private final String remoteServerUrl = "http://localhost:3306/remoteDatabase";

    @Override
    public USRDto addUser(USRDto usrDto) throws UserAlreadyExistException {

        if (Objects.nonNull(usrDto) && Objects.nonNull(usrDto.getEmail())
                && userRepository.findUSRByEmail(usrDto.getEmail()).isPresent()){
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        USR usr = userRepository.save(userMapper.mapUserDtoToUser(usrDto));
        return userMapper.mapUserToUserDto(usr);
    }

    @Override
    public List<USRDto> getAllUser() {
        return userRepository.findAll()
                .stream().map(userMapper::mapUserToUserDto).toList();
    }

    @Override
    public USRDto getUserById(Long idUser) throws UserNotFoundException {
        return userRepository.findById(idUser)
                .map(userMapper::mapUserToUserDto)
                .orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public USRDto updateUser(Long idUser, USRDto usrDto) throws UserNotFoundException {
        if (userRepository.findById(idUser).isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        USR usr = userRepository.findById(idUser).get();
        usr.setUsername(userMapper.mapUserDtoToUser(usrDto).getUsername());
        usr.setEmail(userMapper.mapUserDtoToUser(usrDto).getEmail());
        usr.setPassword(userMapper.mapUserDtoToUser(usrDto).getPassword());
        return userMapper.mapUserToUserDto(userRepository.save(usr));
    }

    @Override
    public void deleteUser(Long idUser) throws UserNotFoundException {
         if (userRepository.findById(idUser).isEmpty()){
             throw new UserNotFoundException(USER_NOT_FOUND);

         }
         userRepository.deleteById(idUser);
    }

    /**
     * Execute toute les 60 secondes
     */
    @Override
    @Scheduled(fixedDelay = 60000)
    public void synchronizeUsers() {
        try {
            // Synchronize local users with remote server
            ResponseEntity<List<USR>> response = restTemplate.exchange(remoteServerUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<USR>>() {});
            if (response.getStatusCode().is2xxSuccessful()) {
                List<USR> remoteUsers = response.getBody();
                if (remoteUsers != null) {
                    for (USR remoteUser : remoteUsers) {
                        USR localUser = userRepository.findById(remoteUser.getId()).orElse(null);
                        if (localUser == null) {
                            // Create new user locally if it doesn't exist
                            userRepository.save(remoteUser);
                        } else {
                            // Update local user with data from remote server
                            localUser.setUsername(remoteUser.getUsername());
                            localUser.setEmail(remoteUser.getEmail());
                            localUser.setPassword(remoteUser.getPassword());
                            localUser.setListeNote(remoteUser.getListeNote());
                            userRepository.save(localUser);
                        }
                    }
                }
            } else {
                System.err.println("Échec de la récupération des utilisateurs à distance: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            System.err.println("Une erreur s'est produite lors de la récupération des utilisateurs à distance " + e.getMessage());
        }

        try {
            // Synchronize remote users with local data
            List<USR> localUsers = userRepository.findAll();
            for (USR localUser : localUsers) {
                USR remoteUser = new USR(localUser.getId(), localUser.getUsername(), localUser.getEmail(),
                        localUser.getPassword(), localUser.getListeNote());

                // Send updates to remote server
                restTemplate.put(remoteServerUrl + "/" + localUser.getId(), localUser);
            }
        } catch (RestClientException e) {
            System.err.println("Erreur survenue lors de la mise à jour des utilisateurs distants: " + e.getMessage());
        }
    }

}
