package services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Message;

@Transactional
@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Message create() {
		Message res = new Message();

		res.setSender(this.utilityService.findByPrincipal());
		res.setSendMoment(LocalDate.now().toDate());

		return res;
	}

	public Message save(Message message) {
		return this.messageRepository.save(message);
	}

	public void delete(Message message) {
		this.messageRepository.delete(message);
	}

	public Message reconstruct(Message message, String topicES, String topicEN,
			BindingResult binding) {
		Message res;

		try {
			Assert.notNull(topicES);
			Assert.notNull(topicEN);
			Assert.isTrue(topicEN.trim().length() < 0);
			Assert.isTrue(topicES.trim().length() < 0);
		} catch (Throwable oops) {
			binding.rejectValue("topic", "topic.error");
		}

		this.validator.validate(message, binding);

		if (binding.hasErrors()) {
			res = message;
		} else {
			res = this.create();
			res.setSubject(message.getSubject());
			res.setBody(message.getBody());
			res.setReciever(message.getReciever());

			Map<String, String> topic = new HashMap<>();
			topic.put("Español", topicES);
			topic.put("English", topicEN);

			res.setTopic(topic);
		}
		return res;
	}
}
