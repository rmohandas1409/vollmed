package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
* Como se trata de uma classe de configuração colocamos a anotation @Configuration.
* @EnableWebSecurity está anotation informamos ao spring que vamos personalizar as configurações de segurança.
* */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //Metodo de configuração de autenticação e autorização.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable() desabilita defesa conta ataque Cross-Site Request Forgery
        //pois como estamos utilizando autenticação via token o mesmo ja realiza isto.
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                //Libera uma url da api sem precisar estar autenticado.
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                 // Metodo para liberar acesso a metodo com perfil de admin
                 //.antMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN")
                //.antMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN")
                //.anyRequest().authenticated() as demais url da api tem quem estar autenticado para executar.
                .anyRequest().authenticated()
                .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
