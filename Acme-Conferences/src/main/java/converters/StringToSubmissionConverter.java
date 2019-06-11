package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.SubmissionRepository;
import domain.Submission;

public class StringToSubmissionConverter implements
		Converter<String, Submission> {

	@Autowired
	SubmissionRepository submissionRepository;

	@Override
	public Submission convert(final String text) {
		Submission result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.submissionRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}