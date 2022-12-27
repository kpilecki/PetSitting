package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}