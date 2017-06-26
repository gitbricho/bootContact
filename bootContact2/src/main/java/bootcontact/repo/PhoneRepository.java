package bootcontact.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcontact.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	
}
