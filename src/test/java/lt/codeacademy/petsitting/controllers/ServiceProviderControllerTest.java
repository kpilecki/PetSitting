package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.request.ServiceProviderAboutRequest;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getAbout_whenAboutIsRequested_statusIsOk() throws Exception {
       serviceProviderRepository.save( getValidServiceProvider() );
        mockMvc.perform( get("/api/providers/about/" ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getAbout_whenAboutIsRequested_aboutIsReturned() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( get("/api/providers/about/" ))
                .andExpect( status().isOk() )
                .andExpect( content().string( savedServiceProvider.getAbout() ));
    }

    @Test
    void getAbout_whenUnauthorizedUserRequestsAbout_statusIsUnauthorized() throws Exception {
        mockMvc.perform( get("/api/providers/about/" ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateAbout_whenValidAboutIsSupplied_statusIsOk() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );
        String newAbout = "New About Service Provider";
        ServiceProviderAboutRequest request= new ServiceProviderAboutRequest( newAbout );

        mockMvc.perform( post("/api/providers/about/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request ) ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateAbout_whenValidAboutIsSupplied_aboutIsUpdated() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );
        String newAbout = "New About Service Provider";
        ServiceProviderAboutRequest request= new ServiceProviderAboutRequest( newAbout );

        mockMvc.perform( post("/api/providers/about/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request ) ))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, serviceProviderRepository.count() );
        ServiceProvider serviceProvider = serviceProviderRepository.findAll().get( 0 );
        Assertions.assertEquals( newAbout, serviceProvider.getAbout() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateAbout_whenInValidAboutIsSupplied_statusIsBadRequest() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );
        String newAbout = "New About Service Provider".repeat(200);
        ServiceProviderAboutRequest request= new ServiceProviderAboutRequest( newAbout );

        mockMvc.perform( post("/api/providers/about/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request ) ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    void updateAbout_whenUnauthorizedUserUpdatesAbout_statusIsUnauthorized() throws Exception {
        String newAbout = "New About Service Provider";
        ServiceProviderAboutRequest request= new ServiceProviderAboutRequest( newAbout );

        mockMvc.perform( post("/api/providers/about/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request ) ))
                .andExpect( status().isUnauthorized() );
    }
}
