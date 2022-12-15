package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
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
public class ServiceProviderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    void registerServiceProvider_whenValidServiceProviderIsSupplied_statusIsOk() throws Exception {
        mockMvc.perform( post("/api/providers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidServiceProviderAsJson() ))
                .andExpect( status().isOk());

    }

    @Test
    void registerServiceProvider_whenExistingServiceProviderIsProvided_errorIsReturned() throws Exception {
        mockMvc.perform( post("/api/providers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidServiceProviderAsJson() ))
                .andExpect( status().isOk());

        mockMvc.perform( post("/api/providers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidServiceProviderAsJson() ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void loadServiceProvider_whenRequestedServiceProviderExists_serviceProviderIsReturned() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( get("/api/providers/get" ))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.id", equalTo( savedServiceProvider.getId().intValue() )));
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void loadCustomer_whenRequestedCustomerDoesNotExist_statusIsOk() throws Exception {
        mockMvc.perform( get("/api/providers/get" ))
                .andExpect( status().isOk() );
    }

    private String getValidServiceProviderAsJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( getValidServiceProvider() );
    }

    private ServiceProvider getValidServiceProvider(){
        return ServiceProvider
                .builder()
                .username( "username" )
                .password( "P4ssword" )
                .firstName( "firstName" )
                .lastName( "lastName")
                .email( "test@email.com" )
                .build();
    }
}
