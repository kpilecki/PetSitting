package lt.codeacademy.petsitting.controllers;

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

import static lt.codeacademy.petsitting.controllers.Utils.getValidServiceProvider;
import static lt.codeacademy.petsitting.controllers.Utils.serializeObjectToJSON;
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
                        .content( serializeObjectToJSON( getValidServiceProvider() ) ))
                .andExpect( status().isOk());

    }

    @Test
    void registerServiceProvider_whenExistingServiceProviderIsProvided_errorIsReturned() throws Exception {
        mockMvc.perform( post("/api/providers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getValidServiceProvider() ) ))
                .andExpect( status().isOk());

        mockMvc.perform( post("/api/providers/signup" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getValidServiceProvider() ) ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void loadServiceProvider_whenRequestedServiceProviderExists_serviceProviderIsReturned() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( get("/api/providers/get" ))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.id", equalTo( savedServiceProvider.getId().intValue() )));
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void loadServiceProvider_whenRequestedProviderDoesNotExist_statusIsOk() throws Exception {
        mockMvc.perform( get("/api/providers/get" ))
                .andExpect( status().isOk() );
    }

}
