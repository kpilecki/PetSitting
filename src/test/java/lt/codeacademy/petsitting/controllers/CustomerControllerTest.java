package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.request.CustomerUpdateRequest;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
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

import static lt.codeacademy.petsitting.controllers.Utils.*;
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
                        .content( serializeObjectToJSON( invalidCustomer ) ))
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

    @Test
    void loadCustomer_whenUserIsNotLoggedIn_statusIsUnauthorized() throws Exception {
        mockMvc.perform( get( "/api/customers/get" ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidUsernameIsSupplied_usernameIsUpdated() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setUsername( "newUsername" );

        mockMvc.perform( post( "/api/customers")
                .contentType( MediaType.APPLICATION_JSON )
                .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, customerRepository.count());
        Assertions.assertTrue( customerRepository.getCustomersByUsername( request.getUsername()).isPresent() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidUsernameIsSupplied_onlyUsernameIsUpdated() throws Exception {
        Customer customer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setUsername( "newUsername" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Customer updatedCustomer = customerRepository.getCustomersByUsername( request.getUsername() ).orElse( null );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( 1, customerRepository.count());

        Assertions.assertEquals( customer.getLastName(), updatedCustomer.getLastName() );
        Assertions.assertEquals( customer.getFirstName(), updatedCustomer.getFirstName() );
        Assertions.assertEquals( customer.getId(), updatedCustomer.getId() );
        Assertions.assertEquals( customer.getEmail(), updatedCustomer.getEmail() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenInValidUsernameIsSupplied_statusIsBadRequest() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setUsername( "" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isBadRequest() );
    }

    @Test
    void updateCustomer_whenNotAuthorizedUserUpdatesUsername_statusIsUnauthorized() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setUsername( "newUsername" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidFirstNameIsSupplied_firstNameIsUpdated() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setFirstName( "newName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, customerRepository.count());
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( request.getFirstName(), updatedCustomer.getFirstName() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidFirstNameIsSupplied_onlyFirstNameIsUpdated() throws Exception {
        Customer customer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setFirstName( "newName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( 1, customerRepository.count());

        Assertions.assertEquals( customer.getLastName(), updatedCustomer.getLastName() );
        Assertions.assertEquals( customer.getUsername(), updatedCustomer.getUsername() );
        Assertions.assertEquals( customer.getId(), updatedCustomer.getId() );
        Assertions.assertEquals( customer.getEmail(), updatedCustomer.getEmail() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenInValidFirstNameIsSupplied_fistNameIsNotUpdated() throws Exception {
        Customer savedCustomer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setFirstName( "" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Assertions.assertEquals( 1, customerRepository.count() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertEquals( savedCustomer.getFirstName(), updatedCustomer.getFirstName() );
    }

    @Test
    void updateCustomer_whenNotAuthorizedUserUpdatesFirstName_statusIsUnauthorized() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setFirstName( "newName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidLastNameIsSupplied_lastNameIsUpdated() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setLastName( "newLastName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, customerRepository.count() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( request.getLastName(), updatedCustomer.getLastName() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidLastNameIsSupplied_onlyLastNameIsUpdated() throws Exception {
        Customer customer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setLastName( "newLastName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( 1, customerRepository.count());

        Assertions.assertEquals( customer.getFirstName(), updatedCustomer.getFirstName() );
        Assertions.assertEquals( customer.getUsername(), updatedCustomer.getUsername() );
        Assertions.assertEquals( customer.getId(), updatedCustomer.getId() );
        Assertions.assertEquals( customer.getEmail(), updatedCustomer.getEmail() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenInValidLastNameIsSupplied_lastNameIsNotUpdated() throws Exception {
        Customer savedCustomer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setLastName( "" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Assertions.assertEquals( 1, customerRepository.count() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertEquals( savedCustomer.getLastName(), updatedCustomer.getLastName() );
    }

    @Test
    void updateCustomer_whenNotAuthorizedUserUpdatesLastName_statusIsUnauthorized() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setLastName( "newLastName" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidEmailIsSupplied_emailIsUpdated() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail( "newEmail@email.com" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, customerRepository.count() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( request.getEmail(), updatedCustomer.getEmail() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenValidEmailIsSupplied_onlyEmailIsUpdated() throws Exception {
        Customer customer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail( "newEmail@email.com" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertNotNull( updatedCustomer );
        Assertions.assertEquals( 1, customerRepository.count());

        Assertions.assertEquals( customer.getFirstName(), updatedCustomer.getFirstName() );
        Assertions.assertEquals( customer.getUsername(), updatedCustomer.getUsername() );
        Assertions.assertEquals( customer.getId(), updatedCustomer.getId() );
        Assertions.assertEquals( customer.getLastName(), updatedCustomer.getLastName() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void updateCustomer_whenInValidEmailIsSupplied_emailIsNotUpdated() throws Exception {
        Customer savedCustomer = customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail( "" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
        Assertions.assertEquals( 1, customerRepository.count() );
        Customer updatedCustomer = customerRepository.findAll().get( 0 );
        Assertions.assertEquals( savedCustomer.getEmail(), updatedCustomer.getEmail() );
    }

    @Test
    void updateCustomer_whenNotAuthorizedUserUpdatesEmail_statusIsUnauthorized() throws Exception {
        customerRepository.save( getValidCustomer() );
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail( "newEmail@email.com" );

        mockMvc.perform( post( "/api/customers")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( request )))
                .andExpect( status().isUnauthorized() );
    }

    //TODO Add CustomerController tests for image upload and retrieve
}
