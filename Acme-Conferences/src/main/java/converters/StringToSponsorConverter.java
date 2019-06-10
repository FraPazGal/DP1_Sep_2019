package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.SponsorRepository;
import domain.Sponsor;

public class StringToSponsorConverter implements Converter<String, Sponsor> {

	@Autowired
	SponsorRepository sponsorRepository;

	@Override
	public Sponsor convert(final String text) {
		Sponsor result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.sponsorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}