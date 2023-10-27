package br.com.ibm.cadeiabatch.ws;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.repository.UserRepository;
import br.com.ibm.cadeiabatch.service.UserService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService serv;

    @Autowired
    private UserRepository rep;

    @PostMapping("/edituser")
    @ResponseStatus(code= HttpStatus.OK)
    public String editUsuario(@RequestBody String request) {

        String[] dadosEnviados = request.split("-");

        String usuarioAux = dadosEnviados[0];
        String lastPassword = dadosEnviados[1];
        String newPassword = dadosEnviados[2];
        String newPasswordConfirm = dadosEnviados[3];
        String nome = dadosEnviados[4];
        String email = dadosEnviados[5];

        Usuario usuario = serv.getUsuarioLogado();

        //compara senha anterior com a do banco
        if(!newPassword.equals(newPasswordConfirm))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova Senha não coincidem");

        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setUsuario(usuarioAux);

        rep.save(usuario);

        return "home";
    }

}
