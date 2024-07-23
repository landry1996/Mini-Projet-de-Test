package com.synchronisation.appsynchronisation.services.noteServices;

import com.synchronisation.appsynchronisation.dao.NoteRepository;
import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.error.NoteAlreadyExistException;
import com.synchronisation.appsynchronisation.mapper.INoteMapper;
import com.synchronisation.appsynchronisation.models.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
    public void synchronizeNotes(){
        // Synchronize local notes with remote server
      List<Note> remoteNotes = restTemplate.getForObject(remoteServerUrl, List.class, Note.class);

      for (Note remoteNote: remoteNotes){
          Note localNote = noteRepository.findById(remoteNote.getId()).orElse(null);
          if (localNote == null){

              // Create new note locally if it doesn't exist
              noteRepository.save(remoteNote);
          }
          else {
              // Update local note with data from remote server
              localNote.setTitle(remoteNote.getTitle());
              localNote.setContent(remoteNote.getContent());
              localNote.setSynchronised(remoteNote.getSynchronised());
              localNote.setUsr(remoteNote.getUsr());
              noteRepository.save(localNote);
          }
      }

        // Synchronize remote notes with local data
        List<Note> localNotes = noteRepository.findAll();
      for (Note localNote: localNotes){
          Note remoteNote = new Note(localNote.getId(), localNote.getTitle(), localNote.getContent(),
                  localNote.getSynchronised(), localNote.getUsr());

          // Send updates to remote server

          restTemplate.put(remoteServerUrl + "/" + localNote.getId(), localNote);

      }
    }


}
