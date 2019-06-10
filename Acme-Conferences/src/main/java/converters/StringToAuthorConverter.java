package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.AuthorRepository;

import domain.Author;

public class StringToAuthorConverter implements Converter<String, Author> {

	@Autowired
	AuthorRepository authorRepository;

	@Override
	public Author convert(final String text) {
		Author result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.authorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}