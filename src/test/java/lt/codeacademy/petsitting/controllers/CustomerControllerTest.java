package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    void registerCustomer_whenValidCustomerIsProvided_successMessageIsReturned() throws Exception {
        mockMvc.perform( post("/api/customers/signup" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( getValidCustomerAsJson() ))
                .andExpect( status().isOk());
    }

    @Test
    void registerCustomer_whenExistingCustomerIsProvided_errorIsReturned() throws Exception {
        mockMvc.perform( post("/api/customers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidCustomerAsJson() ))
                .andExpect( status().isOk());

        mockMvc.perform( post("/api/customers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidCustomerAsJson() ))
                .andExpect( status().isBadRequest() );
    }

    private String getValidCustomerAsJson() throws JsonProcessingException {
        Customer customer = Customer
                .builder()
                .username( "username" )
                .password( "P4ssword" )
                .firstName( "firstName" )
                .lastName( "lastName")
                .email( "test@email.com" )
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( customer );
    }
}
