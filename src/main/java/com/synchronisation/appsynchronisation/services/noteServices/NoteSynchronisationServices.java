package com.synchronisation.appsynchronisation.services.noteServices;

import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.error.NoteAlreadyExistException;
import com.synchronisation.appsynchronisation.error.NoteErrorException;
import com.synchronisation.appsynchronisation.error.NoteNotFoundException;
import com.synchronisation.appsynchronisation.error.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface NoteSynchronisationServices {

    NoteDto addNote(NoteDto noteDto) throws NoteAlreadyExistException;
    List<NoteDto> getAllNote();
    void synchronizeNotes();

}
