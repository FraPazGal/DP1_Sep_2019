package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Conference;
import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select distinct c from Conference c where ( c.title like %?1% or c.acronym like %?1% or c.summary like %?1% or c.venue like %?1%) and  c.entryFee <= ?2 and c.startDate >= ?3 and c.endDate <= ?4 and c.isFinal = true")
	Collection<Conference> search(String keyWord, Double maximumFee,
			Date minimumDate, Date maximumDate);
	
	@Query("select distinct c from Conference c where ( c.title like %?1% or c.acronym like %?1% or c.summary like %?1% or c.venue like %?1%) and c.isFinal = true")
	Collection<Conference> searchAnon(String keyWord);

	@Query("select c from Conference c where c.isFinal = true")
	Collection<Conference> allConferencesFinal();
	
	@Query("select f from Finder f where f.actor.id = ?1")
	Finder findFinderByActorId(int actorId);

}
