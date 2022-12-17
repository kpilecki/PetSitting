package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address save( Address address ){
        return addressRepository.save( address );
    }

    public void deleteById( Long id ) {
        addressRepository.deleteById( id );
    }
}
