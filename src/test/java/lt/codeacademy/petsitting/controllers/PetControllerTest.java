package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Pet;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import lt.codeacademy.petsitting.repositories.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class PetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    private Customer customer;

    @BeforeEach
    void setUp(){
        customer = customerRepository.save( getValidCustomer() );
    }


    @AfterEach
    void cleanUp(){
        customerRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void getPets_whenRequestedPetsExist_statusIsOk() throws Exception {
        Pet pet = petRepository.save( getValidPet() );
        customer.setPets( List.of( pet ) );
        customerRepository.save( customer );

        mockMvc.perform( get( "/api/pets" ) )
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void getPets_whenRequestedPetsExist_petsAreReturned() throws Exception {
        Pet pet = petRepository.save( getValidPet() );
        customer.setPets( List.of( pet ) );
        customerRepository.save( customer );

        mockMvc.perform( get( "/api/pets" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.pets[0].id", equalTo( pet.getId().intValue() )));
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void getPets_whenRequestedPetsDontExist_statusIsOk() throws Exception {

        mockMvc.perform( get( "/api/pets" ) )
                .andExpect( status().isOk() );
    }

    @Test
    public void getPets_whenUnauthorizedUserSendsRequest_statusIsUnauthorized() throws Exception {

        mockMvc.perform( get( "/api/pets" ) )
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void getPetById_whenRequestedPetsExist_petIsReturned() throws Exception {
        Pet pet = petRepository.save( getValidPet() );
        customer.setPets( List.of( pet ) );
        customerRepository.save( customer );

        mockMvc.perform( get( "/api/pets/{id}", pet.getId() ))
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.pets[0].id", equalTo( pet.getId().intValue() )));
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void getPetById_whenRequestedPetsDoesNotExist_statusBadRequest() throws Exception {

        mockMvc.perform( get( "/api/pets/{id}", 1L ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    public void getPetById_whenUnauthorizedRequestIsMade_statusIsUnauthorized() throws Exception {

        mockMvc.perform( get( "/api/pets/{id}", 1L ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void savePet_whenValidPetIsSupplied_statusIsOk() throws Exception {

        mockMvc.perform( post( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidPetAsJson() ))
                .andExpect( status().isOk() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void savePet_whenValidPetIsSupplied_petIsSaved() throws Exception {

        mockMvc.perform( post( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidPetAsJson() ))
                .andExpect( status().isOk() );

        customer = customerRepository.findAll().get( 0 );

        Assertions.assertEquals( 1, petRepository.count() );
        Assertions.assertEquals( 1, customer.getPets().size() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void savePet_whenInValidPetIsSupplied_statusIsBadRequest() throws Exception {

        mockMvc.perform( post( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getInValidPetAsJson() ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    public void savePet_whenUnauthorizedUserSavesPet_statusUnauthorized() throws Exception {

        mockMvc.perform( post( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidPetAsJson() ))
                .andExpect( status().isUnauthorized() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void updatePet_whenExistingPetIsUpdated_petIsUpdated() throws Exception {
        Pet savedPet = petRepository.save( getValidPet() );
        customer.setPets( List.of( savedPet ) );
        customerRepository.save( customer );
        savedPet.setName( "NewName" );

        mockMvc.perform( put( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( savedPet ) ))
                .andExpect( status().isOk() );

        List<Pet> pets = customerRepository.findAll().get( 0 ).getPets();

        Assertions.assertEquals( 1, petRepository.count() );
        Assertions.assertEquals( 1, pets.size() );
        Assertions.assertEquals( savedPet.getName(), pets.get( 0 ).getName() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void updatePet_whenExistingPetIsUpdatedWithInvalidData_statusIsBadRequest() throws Exception {
        Pet savedPet = petRepository.save( getValidPet() );
        customer.setPets( List.of( savedPet ) );
        customerRepository.save( customer );
        savedPet.setName( "" );

        mockMvc.perform( put( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( savedPet ) ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    public void updatePet_whenNotExistingPetIsUpdated_statusIsBadRequest() throws Exception {
        Pet savedPet = petRepository.save( getValidPet() );

        mockMvc.perform( put( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( serializeObjectToJSON( savedPet ) ))
                .andExpect( status().isBadRequest() );
    }

    @Test
    public void updatePet_whenUnauthorizedUserUpdatesPet_statusIsUnauthorized() throws Exception {

        mockMvc.perform( put( "/api/pets" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( getValidPetAsJson() ))
                .andExpect( status().isUnauthorized() );
    }
}
