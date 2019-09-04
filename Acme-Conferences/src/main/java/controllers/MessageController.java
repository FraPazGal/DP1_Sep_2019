package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import services.RegistrationService;
import services.SubmissionService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;
import domain.Author;
import domain.Mensaje;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private UtilityService utilityService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(required = false, value = "topic") String topic,
			@RequestParam(required = false, value = "sender") String sender,
			@RequestParam(required = false, value = "receiver") String receiver) {
		ModelAndView res;
		Collection<Mensaje> messages;

		try {
			messages = this.messageService.findByPrincipal();
			if (topic != null) {
				messages = this.messageService.findByTopic(messages, topic);
			} else if (sender != null) {
				messages = this.messageService.findBySender(messages, sender);
			} else if (receiver != null) {
				messages = this.messageService.findByReceiver(messages,
						receiver);
			}

			res = new ModelAndView("message/list");
			res.addObject("mensajes", messages);
			res.addObject("topics", this.systemConfigurationService
					.findMySystemConfiguration().getTopics());
			res.addObject("principal", this.utilityService.findByPrincipal());

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Mensaje newMessage;

		try {
			// newMessage = this.messageService.create();
			newMessage = new Mensaje();

			res = new ModelAndView("message/edit");
			res.addObject("mensaje", newMessage);
			res.addObject("topics", this.systemConfigurationService
					.findMySystemConfiguration().getTopics());

			Collection<Actor> actors = this.actorService.findAll();
			actors.remove((Actor) this.utilityService.findByPrincipal());
			actors.remove((Actor) this.utilityService
					.findByUsername("[Anonymous]"));

			if (actors.isEmpty())
				res.addObject("found", false);
			else
				res.addObject("actors", actors);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "save")
	public ModelAndView send(Mensaje message, BindingResult binding) {
		ModelAndView res;

		try {
			this.messageService.validate(message, binding);
			if (binding.hasErrors()) {

				res = new ModelAndView("message/edit");

				res.addObject("mensaje", message);
				res.addObject("topics", this.systemConfigurationService
						.findMySystemConfiguration().getTopics());

				Collection<Actor> actors = this.actorService.findAll();
				actors.remove((Actor) this.utilityService.findByPrincipal());
				actors.remove((Actor) this.utilityService
						.findByUsername("[Anonymous]"));

				res.addObject("actors", actors);
				res.addObject("binding", binding);
			} else {
				this.messageService.save(message);

				res = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/createbroadcast", method = RequestMethod.GET)
	public ModelAndView createBroadcast(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "id", required = false) Integer id) {
		ModelAndView res;
		String messageError = null;
		Mensaje newMessage;
		Collection<Actor> actors = new ArrayList<>();

		try {
			Assert.notNull(type);
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));
			newMessage = this.messageService.create();

			res = new ModelAndView("message/broadcast");

			switch (type) {
			case "sub":
				actors = this.submissionService.findActorsWithSubmitions(id);
				newMessage.setReciever(actors);
				messageError = (actors.isEmpty()) ? "reciever.error" : null;
				break;
			case "reg":
				actors = this.registrationService.findActorsRegisteredTo(id);
				newMessage.setReciever(actors);
				messageError = (actors.isEmpty()) ? "reciever.error" : null;
				break;
			case "aut":
				actors = this.actorService.findAll();
				Collection<Actor> authors = new ArrayList<>();
				for (Actor a : actors) {
					if (a instanceof Author) {
						authors.add(a);
					}
				}
				messageError = (authors.isEmpty()) ? "reciever.error" : null;
				newMessage.setReciever(authors);
				break;
			case "all":
				actors = this.actorService.findAll();
				actors.remove((Actor) this.utilityService.findByPrincipal());
				actors.remove((Actor) this.utilityService
						.findByUsername("[Anonymous]"));
				newMessage.setReciever(actors);
				messageError = (actors.isEmpty()) ? "reciever.error" : null;
				break;
			}

			res.addObject("mensaje", newMessage);
			res.addObject("topics", this.systemConfigurationService
					.findMySystemConfiguration().getTopics());
			res.addObject("messageError", messageError);
			res.addObject("broadcast", false);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView sendBroadcast(Mensaje message, BindingResult binding) {
		ModelAndView res;

		try {
			this.messageService.validate(message, binding);
			if (binding.hasErrors()) {
				res = new ModelAndView("message/broadcast");
				res.addObject("mensaje", message);
				res.addObject("topics", this.systemConfigurationService
						.findMySystemConfiguration().getTopics());
				res.addObject("binding", binding);
			} else {
				this.messageService.save(message);

				res = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(
			@RequestParam(value = "messageid", required = false) Integer messageid) {
		ModelAndView res;
		Actor principal;
		Mensaje toDelete;

		try {
			Assert.notNull(messageid);
			principal = this.utilityService.findByPrincipal();
			toDelete = this.messageService.findOne(messageid);

			Assert.isTrue(toDelete.getSender().getId() == principal.getId());

			this.messageService.delete(toDelete);

			res = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}

		return res;
	}
}
