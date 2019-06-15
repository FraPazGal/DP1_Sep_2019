package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewerRepository;
import security.Authority;
import security.UserAccount;
import domain.Reviewer;
import forms.ActorForm;

@Transactional
@Service
public class ReviewerService {

	@Autowired
	private ReviewerRepository reviewerRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Reviewer create() {
		Reviewer res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Reviewer();

		auth.setAuthority(Authority.REVIEWER);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);

		return res;
	}

	public Reviewer save(Reviewer reviewer) {
		return this.reviewerRepository.save(reviewer);
	}

	public void delete(Reviewer reviewer) {
		this.reviewerRepository.delete(reviewer);
	}

	public Reviewer reconstruct(ActorForm form, BindingResult binding) {
		Reviewer res = this.create();

		if (form.getId() != 0) {
			res.setId(form.getId());
			res.setVersion(form.getVersion());
		}

		try {
			if (form.getEmail() != null) {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"REVIEWER"));
				res.setEmail(form.getEmail());
			}
		} catch (Throwable oops) {
			binding.rejectValue("email", "email.error");
		}

		res.setName(form.getName());
		res.setMiddleName(form.getMiddleName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		validator.validate(res, binding);

		return res;
	}
}
