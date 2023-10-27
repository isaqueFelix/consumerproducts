
package br.com.ibm.cadeiabatch.ws;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.repository.UserRepository;
import br.com.ibm.cadeiabatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Controller
public class Control {

	@Autowired
	private UserService serv;

	@Autowired
	private UserRepository rep;


	private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private final DateFormat dateHourFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	@RequestMapping(value = { "/", "/home" })
	public String home() {
		return "home.html";
	}

	@RequestMapping("/add")
	public String addInc() {
		return "incluir-chamado";
	}

	@RequestMapping("/incluir")
	public String incluir() {
		return "incluir.html";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute String loginObject, Locale locale, Model model) {
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginget(@ModelAttribute String loginObject, Locale locale, Model model) {
		return "login";
	}

	@RequestMapping(value = "/listarProdutos", method = RequestMethod.POST)
	public String listarProdutos(@ModelAttribute String loginObject, Locale locale, Model model) {
		return "listaProdutos";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrationDo(HttpServletRequest request) {
		Usuario user = new Usuario();
		//Empresa empresa = new Empresa();

		user.setArea(request.getParameter("area").toString());
		user.setNome(request.getParameter("nome").toString());
		user.setUsuario(request.getParameter("username").toString());
		user.setEmail(request.getParameter("email").toString());

		//if("1".equals(request.getParameter("empresa").toString()))
		//	user.setArea("");

		// consulta Empresa
		//empresa.setId(Long.parseLong(request.getParameter("empresa").toString()));
		//empresa.setNome_empresa(Empresas.getName(Integer.parseInt(request.getParameter("empresa").toString())));

		//user.setEmpresa(Arrays.asList(empresa));

		rep.save(user);

		return "redirect:login";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.GET)
	public String registrar(Model model, String error) {
		if (error != null)
			model.addAttribute("error", "Usuario ja existe.");

		return "registration";
	}

}
