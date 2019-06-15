package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import domain.Actor;
import domain.Category;
import domain.Conference;
import domain.Finder;

@Transactional
@Service
public class FinderService {

	// Managed repository ------------------------------
	@Autowired
	private FinderRepository finderRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UtilityService utilityService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Constructors
	public FinderService() {
		super();
	}

	public Finder create() {
		Finder result;
		Actor principal = this.utilityService.findByPrincipal();

		result = new Finder();
		result.setResults(new ArrayList<Conference>());
		result.setActor(principal);
		return result;
	}

	// /FINDONE
	public Finder findOne(final int finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);

		return result;
	}

	// FINDALL
	public Collection<Finder> findAll() {
		Collection<Finder> result;
		result = this.finderRepository.findAll();

		return result;

	}

	public Finder save(Finder finder) {
		Finder result;
		Actor principal;

		if (finder.getId() == 0) {
			result = this.finderRepository.save(finder);
		} else {
			principal = this.utilityService.findByPrincipal();
			
			Assert.isTrue(principal.equals(finder.getActor()), "not.allowed");
			Assert.notNull(finder, "not.allowed");

			finder.setSearchMoment(new Date(System.currentTimeMillis() - 1));
			result = this.finderRepository.save(finder);
			Assert.notNull(result, "not.null");
		}

		return result;
	}

	public void delete(Finder finder) {
		Actor principal;

		principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(principal.equals(finder.getActor()), "not.allowed");
		
		finder.setResults(null);
		finder.setKeyWord(null);
		finder.setMaximumFee(null);
		finder.setSearchMoment(null);
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);
		finder.setCategory(null);

		this.finderRepository.save(finder);
	}

	// Ancillary methods

	public void deleteExpiredFinder(Finder finder) {
		Date maxLivedMoment = new Date();
		int timeChachedFind;

		timeChachedFind = this.systemConfigurationService
				.findMySystemConfiguration().getTimeResultsCached();
		maxLivedMoment = DateUtils.addHours(new Date(System.currentTimeMillis() - 1), -timeChachedFind);
		if (finder.getSearchMoment().before(maxLivedMoment)) {

			finder.setResults(null);
			finder.setKeyWord(null);
			finder.setMaximumFee(null);
			finder.setSearchMoment(null);
			finder.setMinimumDate(null);
			finder.setMaximumDate(null);
			finder.setCategory(null);

			this.finderRepository.save(finder);
		}
	}

	public Collection<Conference> search(Finder finder) {

		Collection<Conference> results = new ArrayList<Conference>();
		String keyWord;
		Double maximumFee;
		Date minimumDate, maximumDate, searchMoment, aux;
		int nResults;

		Collection<Conference> resultsPageables = new ArrayList<Conference>();

		nResults = this.systemConfigurationService.findMySystemConfiguration()
				.getMaxResults();
		
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

			if(finder.getCategory() == null) {
				results = this.finderRepository.search(keyWord, maximumFee,
						minimumDate, maximumDate);
			} else {
				Category category = finder.getCategory();
				results = this.finderRepository.searchWithCat(keyWord, maximumFee,
						minimumDate, maximumDate, category.getId());
			}
//			List<Conference> r = new ArrayList<Conference>();
//			if (keyWord != "") {
//				r.addAll(this.finderRepository.searchV(keyWord,
//						maximumDuration, minimumRating, maximumRating));
//
//				for (Conference f : r) {
//					if (!results.contains(f)) {
//						results.add(f);
//					}
//				}
//			}
		}
		int count = 0;

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

	public Collection<Conference> allConferencesFinal() {
		return this.finderRepository.allConferencesFinal();
	}
	
	public Finder findFinderByActorId(int actorId) {
		Finder result;
		
		result = this.finderRepository.findFinderByActorId(actorId);
		
		return result;
	}
	
//	public Double RatioFindersEmpty() {
//		return this.finderRepository.RatioFindersEmpty();
//	}
//
//	public Double[] StatsFinder() {
//		return this.finderRepository.StatsFinder();
//	}
}