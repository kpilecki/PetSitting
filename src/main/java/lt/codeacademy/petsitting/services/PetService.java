package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Pet;
import lt.codeacademy.petsitting.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save( Pet pet ) {
        return petRepository.save( pet );
    }

    public Optional<Pet> findById(Long id ) {
        return petRepository.findById( id );
    }
}
