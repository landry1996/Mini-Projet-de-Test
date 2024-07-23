package com.synchronisation.appsynchronisation.mapper;

import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.models.Note;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface INoteMapper {

    Note mapNoteDtoToNote(NoteDto noteDto);
    NoteDto mapNoteToNoteDto(Note note);
}
