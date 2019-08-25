package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.ReviewRepository;
import domain.Review;

public class StringToReviewConverter implements Converter<String, Review> {

	@Autowired
	ReviewRepository reportRepository;

	@Override
	public Review convert(final String text) {
		Review result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.reportRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}