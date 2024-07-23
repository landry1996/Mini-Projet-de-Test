package com.synchronisation.appsynchronisation.error;

public class NoteAlreadyExistException extends Exception{

    public NoteAlreadyExistException(String message){
        super(message);
    }
}
