package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Registration;

@Repository
public interface RegistrationRepository extends
		JpaRepository<Registration, Integer> {
	
	@Query("select r from Registration r where r.author.id = ?1")
	Collection<Registration> registrationsPerAuthor(int authorId);
	
	@Query("select case when count(r) > 0 then true else false end from Registration r where r.conference.id = ?1 and r.author.id = ?2")
	boolean isAlreadyRegistered(int conferenceId, int authorId);
}
