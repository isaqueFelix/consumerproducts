package br.com.ibm.cadeiabatch.service;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public Usuario getUsuarioLogado(){

    return null;
    }
    
}
