package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.Service;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import lt.codeacademy.petsitting.repositories.ServiceRepository;
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

import java.util.List;

import static lt.codeacademy.petsitting.controllers.Utils.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ServiceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @AfterEach
    void cleanUp(){
        serviceProviderRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getAllProvidersServices_whenValidServiceExists_statusIsOk() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );
        Service savedService = serviceRepository.save( getValidService() );
        savedServiceProvider.setServices( List.of( savedService ));
        serviceProviderRepository.save( savedServiceProvider );


        mockMvc.perform( get("/api/services/" ))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.services[0].id", equalTo( savedService.getId().intValue() )));
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getAllProvidersServices_whenNoValidServiceExists_statusIsOk() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( get("/api/services/" ))
                .andExpect( status().isOk() );
    }

    @Test
    void getAllProvidersServices_whenUnauthorizedUserMakesRequest_statusIsUnauthorized() throws Exception {
        mockMvc.perform( get("/api/services/" ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void saveService_whenValidServiceIsProvider_statusIsOk() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  getValidServiceAsJson() ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void saveService_whenValidServiceIsProvider_serviceIsSaved() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  getValidServiceAsJson() ))
                .andExpect( status().isOk() );
        Assertions.assertEquals( 1, serviceRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void saveService_whenInValidServiceIsProvider_statusIsBadRequest() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  getInValidServiceAsJson() ))
                .andExpect( status().isBadRequest() );
        Assertions.assertEquals( 0, serviceRepository.count() );
    }

    @Test
    void saveService_whenUnauthorizedUserMakesRequest_statusIsUnauthorized() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  getInValidServiceAsJson() ))
                .andExpect( status().isUnauthorized() );
        Assertions.assertEquals( 0, serviceRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateService_whenValidServiceIsProvided_statusIsOK() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );
        Service savedService = serviceRepository.save( getValidService() );
        savedServiceProvider.setServices( List.of( savedService ));
        serviceProviderRepository.save( savedServiceProvider );

        savedService.setDescription( "New Description" );

        mockMvc.perform( put("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  serializeObjectToJSON( savedService ) ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateService_whenValidServiceIsProvided_serviceIsUpdated() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );
        Service savedService = serviceRepository.save( getValidService() );
        savedServiceProvider.setServices( List.of( savedService ));
        serviceProviderRepository.save( savedServiceProvider );

        savedService.setDescription( "New Description" );

        mockMvc.perform( put("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  serializeObjectToJSON( savedService ) ))
                .andExpect( status().isOk() );
        Assertions.assertEquals( 1, serviceRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateService_whenInValidServiceIsProvided_statusIsBadRequest() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );
        Service savedService = serviceRepository.save( getValidService() );
        savedServiceProvider.setServices( List.of( savedService ));
        serviceProviderRepository.save( savedServiceProvider );

        savedService.setDescription( null );

        mockMvc.perform( put("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  serializeObjectToJSON( savedService ) ))
                .andExpect( status().isBadRequest() );

    }

    @Test
    void updateService_whenUnauthorizedUserMakesRequest_statusIsUnauthorized() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );
        Service savedService = serviceRepository.save( getValidService() );
        savedServiceProvider.setServices( List.of( savedService ));
        serviceProviderRepository.save( savedServiceProvider );

        savedService.setDescription( "New description" );

        mockMvc.perform( put("/api/services/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(  serializeObjectToJSON( savedService ) ))
                .andExpect( status().isUnauthorized() );

    }
}
