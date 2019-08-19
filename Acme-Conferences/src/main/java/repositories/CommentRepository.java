package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comentario;

@Repository
public interface CommentRepository extends JpaRepository<Comentario, Integer> {

	@Query("select c from Comentario c where c.conference.id = ?1")
	Collection<Comentario> getCommentsOfConference(Integer conferenceid);

	@Query("select c from Comentario c where c.activity.id = ?1")
	Collection<Comentario> getCommentsOfActivity(Integer activityid);
}
