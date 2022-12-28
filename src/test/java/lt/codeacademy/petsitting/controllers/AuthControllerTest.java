package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.payload.request.LoginRequest;
import lt.codeacademy.petsitting.pojo.User;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "P4ssword";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp(){
        User user = User.builder()
                .username( USERNAME )
                .password( encoder.encode( PASSWORD ) )
                .email( "test@email.com" )
                .build();
        userRepository.save( user );
    }

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    void authenticateUser_whenValidUsernameAndPasswordSupplied_statusOk() throws Exception {
        mockMvc.perform( post("/api/auth/login" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidLoginRequestAsJson() ))
                .andExpect( status().isOk() );
    }

    @Test
    void authenticateUser_whenInValidUsernameAndPasswordSupplied_statusUnauthorized() throws Exception {
        mockMvc.perform( post("/api/auth/login" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getInValidLoginRequestAsJson() ))
                .andExpect( status().isBadRequest() );
    }

    private String getValidLoginRequestAsJson() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername( USERNAME );
        loginRequest.setPassword( PASSWORD );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( loginRequest );
    }

    private String getInValidLoginRequestAsJson() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername( USERNAME + "1" );
        loginRequest.setPassword( PASSWORD );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( loginRequest );
    }
}
