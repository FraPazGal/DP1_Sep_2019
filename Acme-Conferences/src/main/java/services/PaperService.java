package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PaperRepository;
import domain.Actor;
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
		Actor principal;
		Paper result;

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "AUTHOR"),
				"not.allowed");

		result = new Paper();

		return result;
	}

	public Collection<Paper> findAll() {
		return this.paperRepository.findAll();
	}

	public Paper findOne(final int paperId) {
		return this.paperRepository.findOne(paperId);
	}
	
	public Paper save(final Paper paper) {
		Actor principal;
		Paper result;

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "AUTHOR"), "not.allowed");
		
		Assert.notNull(paper.getTitle());
		Assert.notNull(paper.getAuthors());
		Assert.notNull(paper.getSummary());
		Assert.notNull(paper.getPaperDocument());
		
		result = this.paperRepository.save(paper);

		return result;
	}
	
	public void delete(final Paper paper) {
		Actor principal;

		Assert.notNull(paper);
		Assert.isTrue(paper.getId() != 0, "wrong.id");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "AUTHOR"),
				"not.allowed");

		this.paperRepository.delete(paper.getId());
	}
	
	// Other business methods -------------------------------
	
	public void flush() {
		this.paperRepository.flush();
	}
	
}
