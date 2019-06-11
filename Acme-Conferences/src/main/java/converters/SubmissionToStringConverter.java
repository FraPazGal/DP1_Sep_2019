package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Submission;

@Component
@Transactional
public class SubmissionToStringConverter implements
		Converter<Submission, String> {

	@Override
	public String convert(final Submission submission) {
		String result;

		if (submission == null)
			result = null;
		else
			result = String.valueOf(submission.getId());
		return result;
	}
}
