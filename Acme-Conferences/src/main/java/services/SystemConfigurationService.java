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

		systemConfiguration.setWelcomeMessage(this.reconstruct(welcomeES,
				welcomeEN));
		systemConfiguration.setVoidWords(this.reconstruct(voidES, voidEN));
		systemConfiguration.setTopics(this.reconstruct(topicES, topicEN));

		try {
			Assert.notNull(welcomeES);
			Assert.notNull(welcomeEN);
			Assert.isTrue(welcomeEN.trim().length() > 0);
			Assert.isTrue(welcomeES.trim().length() > 0);
		} catch (Throwable oops) {
			binding.rejectValue("welcomeMessage", "welcome.error");
		}

		try {
			Assert.notNull(voidES);
			Assert.notNull(voidEN);
			Assert.isTrue(voidEN.trim().length() > 0);
			Assert.isTrue(voidES.trim().length() > 0);
		} catch (Throwable oops) {
			binding.rejectValue("voidWords", "void.error");
		}

		try {
			Assert.notNull(topicES);
			Assert.notNull(topicEN);
			Assert.isTrue(topicEN.trim().length() > 0);
			Assert.isTrue(topicES.trim().length() > 0);
		} catch (Throwable oops) {
			binding.rejectValue("topics", "topics.error");
		}

		try {
			Integer maxResults = Integer.parseInt(systemConfiguration
					.getMaxResults());
			try {
				Assert.isTrue(maxResults > 0 && maxResults <= 10);
			} catch (Throwable oops) {
				binding.rejectValue("maxResults", "results.out.of.range");
			}
		} catch (Throwable oops) {
			binding.rejectValue("maxResults", "results.error");
		}

		try {
			Integer time = Integer.parseInt(systemConfiguration
					.getTimeResultsCached());
			try {
				Assert.isTrue(time > 0 && time <= 24);
			} catch (Throwable oops) {
				binding.rejectValue("timeResultsCached", "time.out.of.range");
			}
		} catch (Throwable oops) {
			binding.rejectValue("timeResultsCached", "time.error");
		}

		this.validator.validate(systemConfiguration, binding);

		return systemConfiguration;
	}

	public Map<String, String> reconstruct(String stringES, String stringEN) {
		Map<String, String> res = new HashMap<>();

		res.put("Español", stringES);
		res.put("English", stringEN);

		return res;
	}

	public String findMyBanner() {
		return this.systemConfigurationRepository.findMyBanner();
	}
}
