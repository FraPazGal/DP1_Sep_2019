package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Review;

@Component
@Transactional
public class ReviewToStringConverter implements Converter<Review, String> {

	@Override
	public String convert(final Review report) {
		String result;

		if (report == null)
			result = null;
		else
			result = String.valueOf(report.getId());
		return result;
	}
}
