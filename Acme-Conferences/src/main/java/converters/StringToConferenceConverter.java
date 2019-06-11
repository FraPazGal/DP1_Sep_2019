package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.ConferenceRepository;

import domain.Conference;

public class StringToConferenceConverter implements
		Converter<String, Conference> {

	@Autowired
	ConferenceRepository conferenceRepository;

	@Override
	public Conference convert(final String text) {
		Conference result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.conferenceRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}