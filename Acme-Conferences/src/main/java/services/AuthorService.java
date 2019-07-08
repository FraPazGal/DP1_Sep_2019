package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuthorRepository;
import security.Authority;
import security.UserAccount;
import domain.Author;
import forms.ActorForm;

@Transactional
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Author create() {
		Author res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Author();

		auth.setAuthority(Authority.AUTHOR);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);

		return res;
	}

	public Author save(Author author) {
		return this.authorRepository.save(author);
	}

	public void delete(Author author) {
		this.authorRepository.delete(author);
	}

	public Author reconstruct(ActorForm form, BindingResult binding) {
		Author res = this.create();

		if (form.getId() != 0) {
			res.setId(form.getId());
			res.setVersion(form.getVersion());
		}

		try {
			if (form.getEmail() != null) {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"AUTHOR"));
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

	public List<Author> findAll() {
		return (List<Author>) this.authorRepository.findAll();
	}
}
