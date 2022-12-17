package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.AddressRepository;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
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
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerAddressTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PasswordEncoder encoder;

    private Customer savedCustomer;

    @BeforeEach
    void setUp(){
        Customer customerToSave = getValidCustomer();
        customerToSave.setPassword(encoder.encode( customerToSave.getPassword() ));
        savedCustomer = customerRepository.save( customerToSave );
    }

    @AfterEach
    void cleanUp(){
        customerRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void addOrUpdateAddress_whenNewValidAddressIsSupplied_addressIsSavedAndStatusIsOk() throws Exception {
        Address addressToSave = getValidAddress();

        mockMvc.perform( post("/api/customers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( addressToSave )))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.street",
                        equalTo( addressToSave.getStreet() )));

        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void addOrUpdateAddress_whenINValidAddressIsSupplied_statusIsBadRequest() throws Exception {

        mockMvc.perform( post("/api/customers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getInValidAddress() )))
                .andExpect( status().isBadRequest() );

        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    void addOrUpdateAddress_whenUnauthorizedUserSuppliesAddress_statusIsUnauthorized() throws Exception {

        mockMvc.perform( post("/api/customers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( getInValidAddress() )))
                .andExpect( status().isUnauthorized() );

        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void addOrUpdateAddress_whenExistingValidAddressIsSupplied_addressIsSavedAndStatusIsOk() throws Exception {
        Address addressToSave = addressRepository.save( getValidAddress() );

        mockMvc.perform( post("/api/customers/address/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( addressToSave )))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath( "$.id",
                        equalTo( addressToSave.getId().intValue() )));

        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER" })
    void deleteAddress_whenExistingAddressIsDeleted_StatusIsOk() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( addressToDelete );
        customerRepository.save( savedCustomer );

        mockMvc.perform( delete( "/api/customers/address/{id}", addressToDelete.getId() ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER" })
    void deleteAddress_whenExistingAddressIsDeleted_addressIsDeleted() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( addressToDelete );
        customerRepository.save( savedCustomer );

        mockMvc.perform( delete( "/api/customers/address/{id}", addressToDelete.getId() ));

        savedCustomer = customerRepository
                .getCustomersByUsername( savedCustomer.getUsername() )
                .orElse( null );
        Assertions.assertNotNull( savedCustomer );
        Assertions.assertNull( savedCustomer.getAddress() );
        Assertions.assertEquals( 0, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER" })
    void deleteAddress_whenNonExistingAddressIsDeleted_statusIsBadRequest() throws Exception {

        mockMvc.perform( delete( "/api/customers/address/{id}", 1L ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    void deleteAddress_whenExistingAddressIsDeletedWithoutAuthorization_statusIsUnauthorized() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( addressToDelete );
        savedCustomer = customerRepository.save( savedCustomer );

        mockMvc.perform( delete( "/api/customers/address/{id}", addressToDelete.getId() ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    void deleteAddress_whenExistingAddressIsDeletedWithoutAuthorization_addressIsNotDeleted() throws Exception {
        Address addressToDelete = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( addressToDelete );
        savedCustomer = customerRepository.save( savedCustomer );

        mockMvc.perform( delete( "/api/customers/address/{id}", addressToDelete.getId() ));

        Assertions.assertNotNull( savedCustomer.getAddress() );
        Assertions.assertEquals( 1, addressRepository.count() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER" })
    void getAddress_whenAddressIsRequestedByAuthorizedCustomer_statusIsOK() throws Exception {
        Address address = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( address );
        savedCustomer = customerRepository.save( savedCustomer );

        mockMvc.perform( get("/api/customers/address/"))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser( username = "username", authorities = { "ROLE_CUSTOMER" })
    void getAddress_whenAddressIsRequestedByAuthorizedCustomer_correctAddressIsReturnedAsJson() throws Exception {
        Address address = addressRepository.save( getValidAddress() );
        savedCustomer.setAddress( address );
        savedCustomer = customerRepository.save( savedCustomer );

        mockMvc.perform( get("/api/customers/address/"))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$.id", equalTo( address.getId().intValue() )));
    }

    @Test
    void getAddress_whenAddressIsRequestedByUnauthorizedUser_statusIsUnauthorized() throws Exception {

        mockMvc.perform( get("/api/customers/address/"))
                .andExpect( status().isUnauthorized() );
    }
}
