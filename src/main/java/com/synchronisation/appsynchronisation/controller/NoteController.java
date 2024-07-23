package com.synchronisation.appsynchronisation.controller;

import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.error.NoteAlreadyExistException;
import com.synchronisation.appsynchronisation.services.noteServices.NoteSynchronisationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class NoteController {

    private final NoteSynchronisationServices services;

    @PostMapping(path = "/notes")
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto) throws NoteAlreadyExistException {
        return new ResponseEntity<>(services.addNote(noteDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/listNote")
    public ResponseEntity<List<NoteDto>> getListAllNotes(){
        return new ResponseEntity<>(services.getAllNote(), HttpStatus.OK);
    }

    @GetMapping(path = "/synchronizeNotes")
    public ResponseEntity<Void> synchronizeNotes()  {

        services.synchronizeNotes();
        return  ResponseEntity.ok().build();
    }

}
