package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.*;
import lt.codeacademy.petsitting.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static lt.codeacademy.petsitting.controllers.Utils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    PetRepository petRepository;

    private String customerUsername;

    private String serviceProviderUsername;

    private Long serviceId;

    private Long petId;

    @BeforeEach
    void setUp(){
        Pet savedPet = petRepository.save( Utils.getValidPet() );
        petId = savedPet.getId();

        Customer customer = customerRepository.save( Utils.getValidCustomer() );
        customer.setPets( List.of( savedPet ) );
        customerUsername = customerRepository.save( customer ).getUsername();

        Service service = serviceRepository.save( Utils.getValidService() );
        serviceId = service.getId();

        ServiceProvider provider = serviceProviderRepository.save( Utils.getValidServiceProvider() );
        provider.setServices( List.of( service ) );
        serviceProviderUsername = serviceProviderRepository.save( provider ).getUsername();
    }

    @AfterEach
    void cleanUp(){
        serviceProviderRepository.deleteAll();
        serviceRepository.deleteAll();
        customerRepository.deleteAll();
        petRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "username", authorities = { "ROLE_CUSTOMER" })
    void createOrder_whenValidOrderRequestIsMade_orderIsCreated() throws Exception {
        Order request = new Order(
                null, serviceProviderUsername, customerUsername, serviceId, petId, LocalDate.now(), null);

        mockMvc.perform( post( "/api/orders/new/")
                .contentType( MediaType.APPLICATION_JSON )
                .content( serializeObjectToJSON( request )))
                .andExpect( status().isOk() );
    }
}
