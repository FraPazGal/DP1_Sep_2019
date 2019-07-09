package services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Transactional
@Service
public class UtilityService {

	// Managed repository ------------------------------------
	
	@Autowired
	private ActorRepository actorRepository;
	
	// Other business methods -------------------------------

	public Boolean checkEmail(final String email, final String authority) {
		Boolean result;
		String emailLowerCase = email.toLowerCase();

		final Pattern pattern = Pattern
				.compile("(^(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})$)|(^((([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})>)$)");
		final Matcher matcher = pattern.matcher(emailLowerCase);
		if (authority.equals("ADMIN")) {
			final Pattern patternAdmin = Pattern
					.compile("(^((([a-z]|[0-9]){1,}[@])$)|(^(([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]>))$)");
			final Matcher matcherAdmin = patternAdmin.matcher(emailLowerCase);
			result = matcherAdmin.matches() ? true : false;
		} else
			result = matcher.matches() ? true : false;
		return result;
	}

	public Actor findByPrincipal() {
		Actor result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor);
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next()
				.getAuthority().equals(authority))
			result = true;
		return result;
	}

	public Boolean existsUsername(String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}

	public boolean isAdmin() {
		boolean isAdmin = false;

		try {
			isAdmin = this.checkAuthority(this.findByPrincipal(), "ADMIN");
		} catch (Throwable oops) {

		}

		return isAdmin;
	}
}
