package bootcontact.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcontact.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
