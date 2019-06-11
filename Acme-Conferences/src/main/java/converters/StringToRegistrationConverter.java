package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.RegistrationRepository;
import domain.Registration;

public class StringToRegistrationConverter implements Converter<String, Registration> {

	@Autowired
	RegistrationRepository registrationRepository;

	@Override
	public Registration convert(final String text) {
		Registration result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.registrationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}