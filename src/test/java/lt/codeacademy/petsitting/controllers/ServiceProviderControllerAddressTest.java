package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.AddressRepository;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static lt.codeacademy.petsitting.controllers.Utils.*;
import static lt.codeacademy.petsitting.controllers.Utils.getValidAddress;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ServiceProviderControllerAddressTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PasswordEncoder encoder;

    private ServiceProvider savedServiceProvider;

    @BeforeEach
    void setUp(){
        ServiceProvider serviceProvider = getValidServiceProvider();
        serviceProvider.setPassword( encoder.encode( serviceProvider.getPassword() ));
        savedServiceProvider = serviceProviderRepository.save( serviceProvider );
    }

    @AfterEach
    void cleanUp(){
        serviceProviderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "username", authorities = {  "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER"  })
    void addOrUpdatePublicAddress_whenNewValidAddressIsSupplied_addressIsSavedAndStatusIsOk() throws Exception {
        Address addressToSave = getValidAddress();

        mockMvc.perform( post("/api/providers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( addressToSave )))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.street",
                        equalTo( addressToSave.getStreet() )));

        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void addOrUpdatePublicAddress_whenInValidAddressIsSupplied_statusIsBadRequest() throws Exception {

        mockMvc.perform( post("/api/providers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getInValidAddress() )))
                .andExpect( status().isBadRequest() );

        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    void addOrUpdatePublicAddress_whenUnauthorizedUserSuppliesAddress_statusIsUnauthorized() throws Exception {

        mockMvc.perform( post("/api/providers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getValidAddress() )))
                .andExpect( status().isUnauthorized() );

        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void addOrUpdatePublicAddress_whenExistingValidAddressIsSupplied_addressIsSavedAndStatusIsOk() throws Exception {
        Address addressToSave = addressRepository.save( getValidAddress() );

        mockMvc.perform( post("/api/providers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( addressToSave )))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.id",
                        equalTo( addressToSave.getId().intValue() )));

        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void deletePublicAddress_whenExistingAddressIsDeleted_StatusIsOk() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( addressToDelete );
        serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( delete( "/api/providers/address/{id}", addressToDelete.getId() ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void deletePublicAddress_whenExistingAddressIsDeleted_addressIsDeleted() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( addressToDelete );
        serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( delete( "/api/providers/address/{id}", addressToDelete.getId() ));

        savedServiceProvider = serviceProviderRepository
                .findByUsername( savedServiceProvider.getUsername() )
                .orElse( null );
        Assertions.assertNotNull( savedServiceProvider );
        Assertions.assertNull( savedServiceProvider.getPublicAddress() );
        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void deletePublicAddress_whenNonExistingAddressIsDeleted_statusIsBadRequest() throws Exception {

        mockMvc.perform( delete( "/api/providers/address/{id}", 1L ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    void deletePublicAddress_whenExistingAddressIsDeletedWithoutAuthorization_statusIsUnauthorized() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( addressToDelete );
        savedServiceProvider = serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( delete( "/api/providers/address/{id}", addressToDelete.getId() ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    void deletePublicAddress_whenExistingAddressIsDeletedWithoutAuthorization_addressIsNotDeleted() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( addressToDelete );
        savedServiceProvider = serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( delete( "/api/providers/address/{id}", addressToDelete.getId() ));

        Assertions.assertNotNull( savedServiceProvider.getPublicAddress() );
        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getPublicAddress_whenAddressIsRequestedByAuthorizedProvider_statusIsOK() throws Exception {
        Address address = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( address );
        savedServiceProvider = serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( get("/api/providers/address/"))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER", "ROLE_SERVICE_PROVIDER" })
    void getPublicAddress_whenAddressIsRequestedByAuthorizedProvider_correctAddressIsReturnedAsJson() throws Exception {
        Address address = addressRepository.save( getValidAddress() );
        savedServiceProvider.setPublicAddress( address );
        savedServiceProvider = serviceProviderRepository.save( savedServiceProvider );

        mockMvc.perform( get("/api/providers/address/"))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$.id", equalTo( address.getId().intValue() )));
    }

    @Test
    void getPublicAddress_whenAddressIsRequestedByUnauthorizedUser_statusIsUnauthorized() throws Exception {

        mockMvc.perform( get("/api/providers/address/"))
                .andExpect( status().isUnauthorized() );
    }
}
