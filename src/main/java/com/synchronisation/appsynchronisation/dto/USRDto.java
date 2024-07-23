package com.synchronisation.appsynchronisation.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class USRDto {

    private Long id;
    private String username;
    private String email;
    private String password;
}
