package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class QuoletToStringConverter implements Converter<Quolet, String> {

	@Override
	public String convert(final Quolet quolet) {
		String result;

		if (quolet == null)
			result = null;
		else
			result = String.valueOf(quolet.getId());
		return result;
	}
}
