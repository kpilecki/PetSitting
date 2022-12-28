package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.ServiceProviderProfileInfo;
import lt.codeacademy.petsitting.pojo.PaymentMethod;
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

import java.util.List;

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
    void getProfileInfo_whenInfoIsRequested_statusIsOk() throws Exception {
       serviceProviderRepository.save( getValidServiceProvider() );
        mockMvc.perform( get("/api/providers/info/" ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getProfileInfo_whenInfoIsRequested_infoIsReturned() throws Exception {
        ServiceProvider savedServiceProvider = serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( get("/api/providers/info/" ))
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.about" , equalTo( savedServiceProvider.getAbout() )) );
    }

    @Test
    void getProfileInfo_whenUnauthorizedUserRequestsInfo_statusIsUnauthorized() throws Exception {
        mockMvc.perform( get("/api/providers/info/" ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateInfo_whenValidInfoIsSupplied_statusIsOk() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/providers/info/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getValidServiceProviderProfileInfo() ) ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateInfo_whenValidInfoIsSupplied_infoIsUpdated() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );
        ServiceProviderProfileInfo info = getValidServiceProviderProfileInfo();

        mockMvc.perform( post("/api/providers/info/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( info ) ))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, serviceProviderRepository.count() );
        ServiceProvider serviceProvider = serviceProviderRepository.findAll().get( 0 );
        Assertions.assertEquals( info.getAbout(), serviceProvider.getAbout() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void updateInfo_whenInValidInfoIsSupplied_statusIsBadRequest() throws Exception {
        serviceProviderRepository.save( getValidServiceProvider() );

        mockMvc.perform( post("/api/providers/info/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getInValidServiceProviderProfileInfo() ) ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    void updateInfo_whenUnauthorizedUserUpdatesInfo_statusIsUnauthorized() throws Exception {

        mockMvc.perform( post("/api/providers/info/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getValidServiceProviderProfileInfo() ) ))
                .andExpect( status().isUnauthorized() );
    }

    private ServiceProviderProfileInfo getValidServiceProviderProfileInfo(){
        return ServiceProviderProfileInfo.builder()
                .yearsOfExperience( 10 )
                .headline( "Headline" )
                .acceptedPaymentMethods(List.of( PaymentMethod.PAYPAL, PaymentMethod.CASH ))
                .skillDescription( "Skill description" )
                .about( "About" )
                .build();
    }
    private ServiceProviderProfileInfo getInValidServiceProviderProfileInfo(){
        return ServiceProviderProfileInfo.builder()
                .yearsOfExperience( 0 )
                .headline( null )
                .acceptedPaymentMethods( null )
                .skillDescription( null )
                .about( null )
                .build();
    }
}
