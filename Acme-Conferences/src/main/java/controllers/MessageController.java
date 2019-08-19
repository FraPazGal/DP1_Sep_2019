package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

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
public class MessageController {

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
	public ModelAndView list() {
		ModelAndView res;
		Collection<Mensaje> messages;

		try {
			messages = this.messageService.findByPrincipal();

			res = new ModelAndView("message/list");
			res.addObject("mensajes", messages);

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Mensaje newMessage;

		try {
			newMessage = this.messageService.create();

			res = new ModelAndView("message/edit");
			res.addObject("mensaje", newMessage);
			res.addObject("topics", this.systemConfigurationService
					.findMySystemConfiguration().getTopics());

			Collection<Actor> actors = this.actorService.findAll();
			actors.remove((Actor) this.utilityService.findByPrincipal());

			res.addObject("actors", actors);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
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

				res.addObject("actors", actors);
			} else {
				this.messageService.save(message);

				res = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/createbroadcast", method = RequestMethod.GET)
	public ModelAndView createBroadcast(
			@RequestParam(value = "type") String type,
			@RequestParam(value = "id", required = false) Integer id) {
		ModelAndView res;
		Mensaje newMessage;
		Collection<Actor> actors;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));
			newMessage = this.messageService.create();

			res = new ModelAndView("message/broadcast");

			switch (type) {
			case "sub":
				actors = this.submissionService.findActorsWithSubmitions(id);
				newMessage.setReciever(actors);
				break;
			case "reg":
				actors = this.registrationService.findActorsRegisteredTo(id);
				newMessage.setReciever(actors);
				break;
			case "aut":
				actors = this.actorService.findAll();
				Collection<Actor> authors = new ArrayList<>();
				for (Actor a : actors) {
					if (a instanceof Author) {
						authors.add(a);
					}
				}
				newMessage.setReciever(authors);
				break;
			case "all":
				actors = this.actorService.findAll();
				newMessage.setReciever(actors);
				break;
			}

			res.addObject("mensaje", newMessage);
			res.addObject("topics", this.systemConfigurationService
					.findMySystemConfiguration().getTopics());
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView sendBroadcast(@Valid Mensaje message,
			BindingResult binding) {
		ModelAndView res;

		try {
			if (binding.hasErrors()) {
				res = new ModelAndView("message/broadcast");
				res.addObject("mensaje", message);
				res.addObject("topics", this.systemConfigurationService
						.findMySystemConfiguration().getTopics());
			} else {
				this.messageService.save(message);

				res = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}
}
