package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Administrator;
import forms.ActorForm;

@Transactional
@Service
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Administrator create() {
		Administrator res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Administrator();

		auth.setAuthority(Authority.ADMIN);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);

		return res;
	}

	public Administrator save(Administrator administrator) {
		return this.administratorRepository.save(administrator);
	}

	public void delete(Administrator administrator) {
		this.administratorRepository.delete(administrator);
	}

	public Administrator reconstruct(ActorForm form, BindingResult binding) {
		Administrator res = this.create();

		if (form.getId() != 0) {
			res.setId(form.getId());
			res.setVersion(form.getVersion());
		}

		try {
			if (form.getEmail() != null) {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"ADMIN"));
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
