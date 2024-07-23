package com.synchronisation.appsynchronisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private Long id;
    private String title;
    private String content;
    private Boolean synchronised;
}
