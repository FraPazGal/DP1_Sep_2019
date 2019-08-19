package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Mensaje;

@Component
@Transactional
public class MessageToStringConverter implements Converter<Mensaje, String> {

	@Override
	public String convert(final Mensaje message) {
		String result;

		if (message == null)
			result = null;
		else
			result = String.valueOf(message.getId());
		return result;
	}
}
