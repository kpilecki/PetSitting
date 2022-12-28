package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public ServiceProvider save( ServiceProvider serviceProvider ){
        return serviceProviderRepository.save( serviceProvider );
    }

    public ServiceProvider getByUsername( String username ) {
        return serviceProviderRepository.findByUsername( username ).orElse( null );
    }

    public ServiceProvider getAuthenticatedServiceProvider(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return getByUsername( auth.getName() );
        }
        return null;
    }
}
