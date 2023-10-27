package br.com.ibm.cadeiabatch.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.repository.UserRepository;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(!username.equals("isaque"))
            throw new UsernameNotFoundException("Usuário não existe");

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("Base"));

        //return new User(user.getUsuario(), user.getSenha(), grantedAuthorities);
        Usuario user = new Usuario();
        user.setUsuario("isaque");
        user.setNome("Isaque");
        user.setSenha("$2a$12$czNuBy3lNQyYUb1Xnh5XX.o7GFKnGnODO4C9S2Os0P8R.kTMY37Ve");

        return new MyUserPrincipal(user);
    }
}
