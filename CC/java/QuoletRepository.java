package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Quolet;

@Repository
public interface QuoletRepository extends JpaRepository<Quolet, Integer> {

	@Query("select case when (count(q) = 0) then true else false end from Quolet q where q.ticker = ?1")
	boolean checkIfUniqueTicker(String ticker);

	@Query("select q from Quolet q where q.conference.id = ?1")
	Collection<Quolet> findConferenceQuolets(Integer conferenceid);

}
