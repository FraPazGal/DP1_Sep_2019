package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.Actor;
import domain.CreditCard;

@Transactional
@Service
public class CreditCardService {

	@Autowired
	private CreditCardRepository  creditCardRepository;
	
	@Autowired
	private UtilityService utilityService;
	
	public CreditCard create() {
		Actor principal;
		CreditCard result;

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SPONSOR") || this.utilityService.checkAuthority(principal, "AUTHOR") ,
				"not.allowed");

		result = new CreditCard();

		return result;
	}

	public Collection<CreditCard> findAll() {
		Collection<CreditCard> result;
		result = this.creditCardRepository.findAll();

		return result;
	}

	public CreditCard findOne(final int creditCardId) {
		CreditCard result;
		result = this.creditCardRepository.findOne(creditCardId);

		return result;
	}
	
	public CreditCard save(final CreditCard creditCard) {
		Actor principal;
		CreditCard result;
		
		Assert.notNull(creditCard);
		Assert.notNull(creditCard.getCVV());
		Assert.notNull(creditCard.getExpirationMonth());
		Assert.notNull(creditCard.getExpirationYear());
		Assert.notNull(creditCard.getHolder());
		Assert.notNull(creditCard.getMake());
		Assert.notNull(creditCard.getNumber());
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SPONSOR") || this.utilityService.checkAuthority(principal, "AUTHOR") ,
				"not.allowed");
		
		result = this.creditCardRepository.save(creditCard);

		return result;
	}
	
	public void delete(final CreditCard creditCard) {
		Actor principal;

		Assert.notNull(creditCard);
		Assert.isTrue(creditCard.getId() != 0, "wrong.id");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SPONSOR") || this.utilityService.checkAuthority(principal, "AUTHOR") ,
				"not.allowed");
		
		this.creditCardRepository.delete(creditCard.getId());
	}
		
	/**
	 * 
	 * Checks if the date of the credit card is expired
	 * 
	 * @param expirationMonth
	 * @param expirationYear
	 * 
	 * @return boolean
	 * @throws ParseException
	 **/
	public boolean checkIfExpired(Integer expirationMonth,
			Integer expirationYear) throws ParseException {

		boolean res = false;

		String expiration = ((expirationMonth.toString().length() == 1) ? "0"
				+ expirationMonth.toString() : expirationMonth.toString())
				+ ((expirationYear.toString().length() == 1) ? "0"
						+ expirationYear.toString() : expirationYear.toString());

		DateFormat date = new SimpleDateFormat("MMyy");

		Date expiryDate = date.parse(expiration);
		Date currentDate = new Date();

		if (currentDate.after(expiryDate))
			res = true;

		return res;
	}

	/**
	 * 
	 * Checks if the string passed as parameter is a valid credit card number.
	 * This method was obtained by some research through the Internet. According
	 * to the Luhn algorithm, the last digit in the credit card number is a
	 * checksum.
	 * 
	 * @param mumber
	 * @return boolean
	 * 
	 **/
	public boolean checkCreditCardNumber(String number) {
		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
	}
	
	
	
/*	public void deleteCreditCardPerSponsorship(CreditCard c){
		this.creditCardRepository.delete(c);
	}*/
}
