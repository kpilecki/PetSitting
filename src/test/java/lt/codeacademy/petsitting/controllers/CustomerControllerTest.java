package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

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

    @Test
    void registerCustomer_whenInvalidCustomerIsProvided_errorIsReturned() throws Exception {
        Customer invalidCustomer = Customer
                .builder()
                .username( "" )
                .password( "" )
                .firstName( "" )
                .lastName( "")
                .email( "" )
                .build();

        mockMvc.perform( post("/api/customers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeCustomerToJSON( invalidCustomer ) ))
                .andExpect( status().isBadRequest() );

    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void loadCustomer_whenRequestedCustomerExists_customerIsReturned() throws Exception {
        Customer savedCustomer = customerRepository.save( getValidCustomer() );

        mockMvc.perform( get("/api/customers/get" ))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.id", equalTo( savedCustomer.getId().intValue() )));

    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void loadCustomer_whenRequestedCustomerDoesNotExist_statusIsOk() throws Exception {
        mockMvc.perform( get("/api/customers/get" ))
                .andExpect( status().isOk() );
    }

    private String getValidCustomerAsJson() throws JsonProcessingException {

        return serializeCustomerToJSON( getValidCustomer() );
    }

    private String serializeCustomerToJSON( Customer customer ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( customer );
    }

    private Customer getValidCustomer(){
        return Customer
                .builder()
                .username( "username" )
                .password( "P4ssword" )
                .firstName( "firstName" )
                .lastName( "lastName")
                .email( "test@email.com" )
                .build();
    }
}
