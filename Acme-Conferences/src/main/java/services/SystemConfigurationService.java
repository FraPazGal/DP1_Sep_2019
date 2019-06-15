package services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	@Autowired
	private SystemConfigurationRepository systemConfigurationRepository;

	@Autowired
	private Validator validator;

	public SystemConfiguration findMySystemConfiguration() {
		return this.systemConfigurationRepository.getMySystemConfiguration();
	}

	public SystemConfiguration save(SystemConfiguration systemConfiguration) {
		return this.systemConfigurationRepository.save(systemConfiguration);
	}

	public SystemConfiguration reconstruct(
			SystemConfiguration systemConfiguration, String welcomeES,
			String welcomeEN, String voidES, String voidEN, String topicES,
			String topicEN, BindingResult binding) {
		SystemConfiguration res;

		try {
			Assert.notNull(welcomeES);
			Assert.notNull(welcomeEN);
			Assert.isTrue(welcomeEN.trim().length() < 0);
			Assert.isTrue(welcomeES.trim().length() < 0);
		} catch (Throwable oops) {
			binding.rejectValue("welcomeMessage", "welcome.error");
		}

		this.validator.validate(systemConfiguration, binding);

		if (binding.hasErrors()) {
			res = systemConfiguration;
		} else {
			res = new SystemConfiguration();
			res.setSystemName(systemConfiguration.getSystemName());
			res.setBanner(systemConfiguration.getBanner());
			res.setCountryCode(systemConfiguration.getCountryCode());
			res.setTimeResultsCached(systemConfiguration.getTimeResultsCached());
			res.setMaxResults(systemConfiguration.getMaxResults());
			res.setWelcomeMessage(this.reconstruct(welcomeES, welcomeEN));
			res.setVoidWords(this.reconstruct(voidES, voidEN));
			res.setVoidWords(this.reconstruct(topicES, topicEN));
		}
		return res;
	}

	public Map<String, String> reconstruct(String stringES, String stringEN) {
		Map<String, String> res = new HashMap<>();

		res.put("Español", stringES);
		res.put("English", stringEN);

		return res;
	}
}
