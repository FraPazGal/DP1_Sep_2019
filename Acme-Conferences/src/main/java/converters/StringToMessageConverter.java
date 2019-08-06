package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.MessageRepository;
import domain.Mensaje;

public class StringToMessageConverter implements Converter<String, Mensaje> {

	@Autowired
	MessageRepository messageRepository;

	@Override
	public Mensaje convert(final String text) {
		Mensaje result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.messageRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}