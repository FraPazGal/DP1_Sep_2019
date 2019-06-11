package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.FinderRepository;
import domain.Finder;

public class StringToFinderConverter implements Converter<String, Finder> {

	@Autowired
	FinderRepository finderRepository;

	@Override
	public Finder convert(final String text) {
		Finder result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.finderRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}