package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.ActivityRepository;
import domain.Activity;

public class StringToActivityConverter implements Converter<String, Activity> {

	@Autowired
	ActivityRepository activityRepository;

	@Override
	public Activity convert(final String text) {
		Activity result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.activityRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}