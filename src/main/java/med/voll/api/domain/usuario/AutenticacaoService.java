package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Classe do tipo service que tem toda logiaca de autenticação.
 * Para o spring reconhecer que esta classe utilizamos a anotation
 * @Service, para que o spring reconheça que esta classe e uma classe
 * de autenticação devenmos implementar uma classe do spring security
 * chamada UserDetailsService
 *
 * */
@Service
public class AutenticacaoService implements UserDetailsService {

    /**
     * É necessario injetar o repository da classe Usuario onde
     * com um medoto que retorna o resultado da consulta.
    **/
    @Autowired
    private UsuarioRepository repository;

    /*
    * Metodo que é chamado toda vez que fazemos login passando
    * o username,
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}
