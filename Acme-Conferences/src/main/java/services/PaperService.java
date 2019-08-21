package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PaperRepository;
import domain.Paper;

@Transactional
@Service
public class PaperService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private PaperRepository paperRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private UtilityService utilityService;
	
	// CRUD Methods ------------------------------------------
	
	public Paper create() {
		this.utilityService.assertPrincipal("AUTHOR");

		return new Paper();
	}

	public Collection<Paper> findAll() {
		return this.paperRepository.findAll();
	}

	public Paper findOne(final int paperId) {
		Paper result = this.paperRepository.findOne(paperId);
		Assert.notNull(result, "wrong.id");
		
		return result;
	}
	
	public Paper save(final Paper paper) {
		Assert.notNull(paper, "wrong.id");
		Paper result;

		this.utilityService.assertPrincipal("AUTHOR");
		
		Assert.notNull(paper.getTitle());
		Assert.notNull(paper.getAuthors());
		Assert.notNull(paper.getSummary());
		Assert.notNull(paper.getPaperDocument());
		
		result = this.paperRepository.save(paper);

		return result;
	}
	
	public void delete(final Paper paper) {
		Assert.notNull(paper);
		Assert.isTrue(paper.getId() != 0, "wrong.id");

		this.utilityService.assertPrincipal("AUTHOR");

		this.paperRepository.delete(paper.getId());
	}
	
	// Other business methods -------------------------------
	
	public void flush() {
		this.paperRepository.flush();
	}
	
}
