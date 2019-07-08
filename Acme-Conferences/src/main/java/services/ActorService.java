package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ActorRepository;
import domain.Actor;

@Transactional
@Service
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;

	/**
	 * Get all actors from database
	 * 
	 * @return Collection<Actor>
	 */
	public Collection<Actor> findAll() {
		Collection<Actor> result = null;
		result = this.actorRepository.findAll();
		return result;
	}

	/**
	 * Find an actor by id in the database
	 * 
	 * @param actorId
	 * @return actor
	 */
	public Actor findOne(final int actorId) {
		final Actor result = this.actorRepository.findOne(actorId);
		return result;
	}

	public boolean existsUsername(String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}
}
