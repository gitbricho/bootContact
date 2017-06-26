package bootcontact.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcontact.model.Mail;

public interface MailRepository extends JpaRepository<Mail, Long> {
	
}
