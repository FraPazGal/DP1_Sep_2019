package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.CreditCardRepository;

import domain.CreditCard;

public class StringToCreditCardConverter implements
		Converter<String, CreditCard> {

	@Autowired
	CreditCardRepository creditCardRepository;

	@Override
	public CreditCard convert(final String text) {
		CreditCard result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.creditCardRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}