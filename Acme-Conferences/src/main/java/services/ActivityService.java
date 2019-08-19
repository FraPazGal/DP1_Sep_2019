package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActivityRepository;
import domain.Activity;
import domain.Conference;

@Transactional
@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ConferenceService conferenceService;

	// @Autowired
	// private UtilityService utilityService;

	public Collection<Activity> listAll() {
		return this.activityRepository.findAll();
	}

	public Activity create(Integer conferenceId) {
		Activity res = new Activity();
		Conference conference = this.conferenceService.findOne(conferenceId);
		Assert.notNull(conference);

		res.setConference(conference);

		return res;
	}

	public Activity save(Activity activity) {
		return this.activityRepository.save(activity);
	}

	public void delete(Activity activity) {
		this.activityRepository.delete(activity);
	}

	public Collection<Activity> getActivitiesOfConference(Integer conferenceId) {
		Collection<Activity> activities = this.activityRepository
				.getActivitiesOfConference(conferenceId);
		return (activities != null) ? activities : new ArrayList<Activity>();
	}

	public Activity findOne(Integer activityid) {
		return this.activityRepository.findOne(activityid);
	}

	public void flush() {
		this.activityRepository.flush();
	}
}
