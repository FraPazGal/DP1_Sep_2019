package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Conference;
import domain.Finder;

@Transactional
@Service
public class FinderService {

	// Managed repository ------------------------------------
	@Autowired
	private FinderRepository finderRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Finder create() {
		Finder result = new Finder();
		Actor principal = this.utilityService.findByPrincipal();

		result.setResults(new ArrayList<Conference>());
		result.setActor(principal);
		return result;
	}

	public Finder create(Actor newActor) {
		Finder result;

		result = new Finder();
		result.setResults(new ArrayList<Conference>());
		result.setActor(newActor);
		return result;
	}

	public Finder findOne(final int finderId) {
		Finder result = this.finderRepository.findOne(finderId);
		Assert.notNull(result, "wrong.id");

		return result;
	}

	public Collection<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder save(Finder finder) {
		Finder result;

		if (finder.getId() == 0) {
			result = this.finderRepository.save(finder);
		} else {
			Actor principal = this.utilityService.findByPrincipal();

			Assert.isTrue(principal.equals(finder.getActor()), "not.allowed");
			Assert.notNull(finder, "not.allowed");

			finder.setSearchMoment(new Date(System.currentTimeMillis() - 1));
			result = this.finderRepository.save(finder);
			Assert.notNull(result, "not.null");
		}

		return result;
	}

	public void delete(Finder finder) {
		Assert.isTrue(finder.getId() != 0);

		Actor principal = this.utilityService.findByPrincipal();
		Finder aux = this.findOne(finder.getId());

		Assert.isTrue(principal.equals(aux.getActor()), "not.allowed");

		finder.setActor(aux.getActor());
		finder.setVersion(aux.getVersion());
		finder.setResults(new ArrayList<Conference>());
		finder.setKeyWord(null);
		finder.setMaximumFee(null);
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);
		finder.setCategory(null);

		this.save(finder);
	}

	// Ancillary methods

	public void deleteExpiredFinder(Finder finder) {
		Date maxLivedMoment = new Date();
		int timeChachedFind;

		timeChachedFind = Integer.parseInt(this.systemConfigurationService
				.findMySystemConfiguration().getTimeResultsCached());
		maxLivedMoment = DateUtils.addHours(new Date(
				System.currentTimeMillis() - 1), -timeChachedFind);
		if (finder.getSearchMoment().before(maxLivedMoment)) {

			finder.setResults(new ArrayList<Conference>());
			finder.setKeyWord(null);
			finder.setMaximumFee(null);
			finder.setSearchMoment(null);
			finder.setMinimumDate(null);
			finder.setMaximumDate(null);
			finder.setCategory(null);

			this.save(finder);
		}
	}

	public Collection<Conference> search(Finder finder) {
		Collection<Conference> results = new ArrayList<Conference>();
		String keyWord;
		Double maximumFee;
		Date minimumDate, maximumDate, aux;
		int nResults;
		int count = 0;

		Collection<Conference> resultsPageables = new ArrayList<Conference>();

		nResults = Integer.parseInt(this.systemConfigurationService
				.findMySystemConfiguration().getMaxResults());

		keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? ""
				: finder.getKeyWord();

		maximumFee = (finder.getMaximumFee() == null) ? 100000 : finder
				.getMaximumFee();

		aux = new GregorianCalendar(2000, 0, 1).getTime();
		minimumDate = (finder.getMinimumDate() == null) ? aux : finder
				.getMinimumDate();

		aux = new GregorianCalendar(2050, 0, 1).getTime();
		maximumDate = (finder.getMaximumDate() == null) ? aux : finder
				.getMaximumDate();

		if ((finder.getKeyWord() == null || finder.getKeyWord().isEmpty())
				&& finder.getMaximumFee() == null
				&& finder.getMinimumDate() == null
				&& finder.getMaximumDate() == null
				&& finder.getCategory() == null) {
			results = this.allConferencesFinal();
		} else {

			results = this.finderRepository.search(keyWord, maximumFee,
					minimumDate, maximumDate);

			if (finder.getCategory() != null) {
				results.retainAll(finder.getCategory().getConferences());
			}
		}

		for (Conference p : results) {
			resultsPageables.add(p);
			count++;
			if (count >= nResults) {
				break;
			}
		}
		finder.setResults(resultsPageables);

		this.save(finder);

		return resultsPageables;
	}

	public Collection<Conference> searchAnon(String keyword) {

		return this.finderRepository.searchAnon(keyword);
	}

	public Finder reconstruct(Finder finder, BindingResult binding) {
		Actor principal = this.utilityService.findByPrincipal();
		Finder aux = this.findOne(finder.getId());

		Assert.isTrue(aux.getActor().equals(principal), "not.allowed");

		finder.setVersion(aux.getVersion());
		finder.setActor(aux.getActor());
		finder.setResults(aux.getResults());
		finder.setSearchMoment(new Date(System.currentTimeMillis() - 1));

		this.validator.validate(finder, binding);

		return finder;
	}

	private Collection<Conference> allConferencesFinal() {
		return this.finderRepository.allConferencesFinal();
	}

	public Finder findFinderByActorId(int actorId) {
		return this.finderRepository.findFinderByActorId(actorId);
	}

	public Finder checkIfExpired() {
		Date maxLivedMoment = new Date();
		Actor principal = this.utilityService.findByPrincipal();
		Finder finder = this.findFinderByActorId(principal.getId());

		if (finder.getSearchMoment() != null) {
			final int timeChachedFind = Integer
					.parseInt(this.systemConfigurationService
							.findMySystemConfiguration().getTimeResultsCached());
			maxLivedMoment = DateUtils.addHours(maxLivedMoment,
					-timeChachedFind);

			if (finder.getSearchMoment().before(maxLivedMoment))
				this.deleteExpiredFinder(finder);
		}

		return finder;
	}

}