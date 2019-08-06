package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	// Managed repository ------------------------------------
	
	@Autowired
	private CreditCardRepository  creditCardRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private UtilityService utilityService;
	
	// CRUD Methods ------------------------------------------
	
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
		return this.creditCardRepository.findAll();
	}

	public CreditCard findOne(final int creditCardId) {
		return this.creditCardRepository.findOne(creditCardId);
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
		
	// Other business methods -------------------------------
	
	public boolean checkIfExpired(Integer expirationMonth,
			Integer expirationYear) throws ParseException {

		boolean res = false;
		
		String expiration = ((expirationMonth.toString().length() == 1) ? "0"
				+ expirationMonth.toString() : expirationMonth.toString())
				+ ((expirationYear.toString().length() == 1) ? "0"
						+ expirationYear.toString() : expirationYear.toString());

		DateFormat date = new SimpleDateFormat("MMyy");

		Date expiryDate = date.parse(expiration);
		
		Calendar expDate = Calendar.getInstance();
		expDate.setTime(expiryDate);
		expDate.add(Calendar.MONTH, 1);
		expDate.add(Calendar.DAY_OF_MONTH, -1);

		Date currentDate = new Date();

		if (currentDate.after(expDate.getTime()))
			res = true;

		return res;
	}
}
