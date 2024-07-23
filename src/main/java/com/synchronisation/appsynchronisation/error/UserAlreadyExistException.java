package com.synchronisation.appsynchronisation.error;

public class UserAlreadyExistException extends Exception{

    public UserAlreadyExistException(String message){
        super(message);
    }
}
