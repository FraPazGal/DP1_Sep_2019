package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SectionRepository;
import domain.Activity;
import domain.Section;

@Transactional
@Service
public class SectionService {

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private ActivityService activityService;

	public Section create(Integer activityid) {
		Section res = new Section();
		Activity activity = this.activityService.findOne(activityid);
		Assert.notNull(activity);

		res.setActivity(activity);

		return res;
	}

	public Section save(Section section) {
		return this.sectionRepository.save(section);
	}

	public void delete(Section section) {
		this.sectionRepository.delete(section);
	}

	public Collection<Section> getSectionsOfActivity(int activityid) {
		return this.sectionRepository.getSectionsOfActivity(activityid);
	}

	public Section findOne(Integer sectionid) {
		return this.sectionRepository.findOne(sectionid);
	}

}
