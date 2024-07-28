package com.synchronisation.appsynchronisation.services.noteServices;

import com.synchronisation.appsynchronisation.dao.NoteRepository;
import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.error.NoteAlreadyExistException;
import com.synchronisation.appsynchronisation.mapper.INoteMapper;
import com.synchronisation.appsynchronisation.models.Note;
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

import static com.synchronisation.appsynchronisation.utils.Utils.NOTE_ALREADY_EXIST;


@Service
@RequiredArgsConstructor
public class NoteSynchronisationServicesImpl implements NoteSynchronisationServices {

    private final NoteRepository noteRepository;

    private final RestTemplate restTemplate;
    private final INoteMapper noteMapper;

    private final String remoteServerUrl = "http://localhost:3306/remoteDatabase";
    @Override
    public NoteDto addNote(NoteDto noteDto) throws NoteAlreadyExistException {


        if (Objects.nonNull(noteDto) && Objects.nonNull(noteDto.getTitle())
        && noteRepository.findNoteByTitle(noteDto.getTitle()).isPresent()){
            throw new NoteAlreadyExistException(NOTE_ALREADY_EXIST);
        }

        Note note = noteRepository.save(noteMapper.mapNoteDtoToNote(noteDto));
        return noteMapper.mapNoteToNoteDto(note);
    }

    @Override
    public List<NoteDto> getAllNote() {
        return noteRepository.findAll()
                .stream().map(noteMapper::mapNoteToNoteDto).toList();
    }

    /**
     * Execute toute les 60 secondes
     */
    @Override
    @Scheduled(fixedDelay = 60000)
    public void synchronizeNotes() {
        try {
            // Synchronize local notes with remote server
            ResponseEntity<List<Note>> response = restTemplate.exchange(remoteServerUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Note>>() {});
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Note> remoteNotes = response.getBody();
                if (remoteNotes != null) {
                    for (Note remoteNote : remoteNotes) {
                        Note localNote = noteRepository.findById(remoteNote.getId()).orElse(null);
                        if (localNote == null) {
                            // Create new note locally if it doesn't exist
                            noteRepository.save(remoteNote);
                        } else {
                            // Update local note with data from remote server
                            localNote.setTitle(remoteNote.getTitle());
                            localNote.setContent(remoteNote.getContent());
                            localNote.setSynchronised(remoteNote.getSynchronised());
                            localNote.setUsr(remoteNote.getUsr());
                            noteRepository.save(localNote);
                        }
                    }
                }
            } else {
                System.err.println("Échec de la récupération des notes à distance:" + response.getStatusCode());
            }
        } catch (RestClientException e) {
            System.err.println("Une erreur s'est produite lors de la récupération des notes à distance " + e.getMessage());
        }

        try {
            // Synchronize remote notes with local data
            List<Note> localNotes = noteRepository.findAll();
            for (Note localNote : localNotes) {
                Note remoteNote = new Note(localNote.getId(), localNote.getTitle(), localNote.getContent(),
                        localNote.getSynchronised(), localNote.getUsr());

                // Send updates to remote server
                restTemplate.put(remoteServerUrl + "/" + localNote.getId(), localNote);
            }
        } catch (RestClientException e) {
            System.err.println("Erreur survenue lors de la mise à jour des notes distants: " + e.getMessage());
        }
    }


}
