package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.controllers.Utils;
import lt.codeacademy.petsitting.pojo.Pet;
import lt.codeacademy.petsitting.repositories.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class PetServiceTest {

    @Autowired
    PetService petService;

    @Autowired
    PetRepository petRepository;


    @AfterEach
    void cleanUp(){
        petRepository.deleteAll();
    }

    @Test
    void save_whenValidPetIsSupplied_petIsSaved(){
        Pet pet = Utils.getValidPet();

        petService.save( pet );

        Assertions.assertEquals( 1, petRepository.count() );
    }

    @Test
    void save_whenValidPetIsSupplied_idIsGenerated(){
        Pet pet = Utils.getValidPet();

        Pet savedPet = petService.save( pet );

        Assertions.assertNotNull( savedPet.getId() );
    }

    @Test
    void save_whenValidPetIsSupplied_samePetIsReturned(){
        Pet pet = Utils.getValidPet();

        Pet savedPet = petService.save( pet );

       Assertions.assertEquals( pet.getName(), savedPet.getName() );
    }

    @Test
    void findById_whenValidPetExists_petIsReturned(){
        Pet pet = petRepository.save( Utils.getValidPet() );

        Optional<Pet> returnedPet = petService.findById( pet.getId() );

        Assertions.assertTrue( returnedPet.isPresent() );
    }

    @Test
    void findById_whenValidPetExists_correctPetIsReturned(){
        Pet pet = petRepository.save( Utils.getValidPet() );

        Optional<Pet> returnedPet = petService.findById( pet.getId() );

        Assertions.assertTrue( returnedPet.isPresent() );
        Assertions.assertEquals( pet.getName(), returnedPet.get().getName() );
    }

    @Test
    void findById_whenNoValidPetExists_noPetIsReturned(){

        Optional<Pet> returnedPet = petService.findById( 1L );

        Assertions.assertTrue( returnedPet.isEmpty() );
    }

}
