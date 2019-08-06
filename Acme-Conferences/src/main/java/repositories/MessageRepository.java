package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Mensaje;

@Repository
public interface MessageRepository extends JpaRepository<Mensaje, Integer> {

	@Query("select m from Mensaje m where m.sender.id = ?1")
	Collection<Mensaje> findByPrincipal(Integer id);

}
