package com.synchronisation.appsynchronisation;

import com.synchronisation.appsynchronisation.dto.NoteDto;
import com.synchronisation.appsynchronisation.dto.USRDto;
import com.synchronisation.appsynchronisation.services.noteServices.NoteSynchronisationServices;
import com.synchronisation.appsynchronisation.services.userServices.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class AppSynchronisationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppSynchronisationApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            NoteSynchronisationServices noteServices,
            UserServices userServices
    ){
        return args -> {

            var note1 = noteServices.addNote(NoteDto.builder()
                    .title("Programmation")
                    .content("Contient des astuces en programmations Java avec Spring boot")
                    .synchronised(true)
                    .build());

            var note2 = noteServices.addNote(NoteDto.builder()
                    .title("Recettes Cuisine")
                    .content("Contient des astuces des recettes de cuisines Africaine")
                    .synchronised(true)
                    .build());

            var note3 = noteServices.addNote(NoteDto.builder()
                    .title("Security Spring")
                    .content("Contient des astuces sur Spring security")
                    .synchronised(true)
                    .build());

            var user1 = userServices.addUser(USRDto.builder()
                    .username("Landry")
                    .email("landry@gmail.com")
                    .password("12345")
                    .build());

            var user2 = userServices.addUser(USRDto.builder()
                    .username("Joel")
                    .email("joel@gmail.com")
                    .password("12345")
                    .build());

            var user3 = userServices.addUser(USRDto.builder()
                    .username("Placide")
                    .email("placide@gmail.com")
                    .password("12345")
                    .build());

           /*
            noteServices.assigneNoteToUser(note1.getId(), user1.getId());
            noteServices.assigneNoteToUser(note3.getId(), user2.getId());
            noteServices.assigneNoteToUser(note2.getId(), user3.getId());
            */

        };

    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
