package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Mensaje;

@Transactional
@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Mensaje create() {
		Mensaje res = new Mensaje();

		res.setSender(this.utilityService.findByPrincipal());
		res.setSendMoment(LocalDate.now().toDate());

		return res;
	}

	public Mensaje save(Mensaje message) {
		return this.messageRepository.save(message);
	}

	public void delete(Mensaje message) {
		this.messageRepository.delete(message);
	}

	public Collection<Mensaje> findByPrincipal() {
		Collection<Mensaje> res = new ArrayList<>();
		Actor principal = this.utilityService.findByPrincipal();
		Collection<Mensaje> messages = this.messageRepository.findAll();

		for (Mensaje m : messages) {
			if (m.getReciever().contains(principal)
					|| m.getSender().getId() == principal.getId()) {
				res.add(m);
			}
		}
		return res;
	}

	public void validate(final Mensaje message, final BindingResult binding) {

		try {
			Assert.isTrue(!message.getBody().isEmpty()
					&& message.getBody() != null);
		} catch (Throwable oops) {
			binding.rejectValue("body", "message.body.not.blank");
		}

		try {
			Assert.isTrue(!message.getSubject().isEmpty()
					&& message.getSubject() != null);
		} catch (Throwable oops) {
			binding.rejectValue("subject", "message.subject.not.blank");
		}

		try {
			Assert.isTrue(!message.getTopic().isEmpty()
					&& message.getTopic() != null);
		} catch (Throwable oops) {
			binding.rejectValue("topic", "message.topic.not.blank");
		}

		try {
			Assert.notNull(message.getReciever());
			Assert.notEmpty(message.getReciever());
		} catch (Throwable oops) {
			binding.rejectValue("reciever", "message.reciever.not.blank");
		}

		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == message
					.getSender().getId());
		} catch (Throwable oops) {
			binding.rejectValue("sender", "message.sender.error");
		}

		this.validator.validate(message, binding);

	}
}
