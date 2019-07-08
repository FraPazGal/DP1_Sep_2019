package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;

@Transactional
@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UtilityService utilityService;

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

	public Collection<Message> findByPrincipal() {
		Collection<Message> res = new ArrayList<>();
		Actor principal = this.utilityService.findByPrincipal();
		Collection<Message> messages = this.messageRepository.findAll();

		for (Message m : messages) {
			if (m.getReciever().contains(principal)
					|| m.getSender().getId() == principal.getId()) {
				res.add(m);
			}
		}
		return res;
	}
}
