package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoExcepition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 *
 * Essas mensagens de erro não foram definidas na aplicação, pois são mensagens de erro padrão do próprio Bean Validation.
 * Entretanto, caso você queira, pode personalizar tais mensagens.
 *
 * Uma das maneiras de personalizar as mensagens de erro é adicionar o atributo message nas próprias anotações de validação:
 *
 * public record DadosCadastroMedico(
 *     @NotBlank(message = "Nome é obrigatório")
 *     String nome,
 *
 *     @NotBlank(message = "Email é obrigatório")
 *     @Email(message = "Formato do email é inválido")
 *     String email,
 *
 *     @NotBlank(message = "Telefone é obrigatório")
 *     String telefone,
 *
 *     @NotBlank(message = "CRM é obrigatório")
 *     @Pattern(regexp = "\\d{4,6}", message = "Formato do CRM é inválido")
 *     String crm,
 *
 *     @NotNull(message = "Especialidade é obrigatória")
 *     Especialidade especialidade,
 *
 *     @NotNull(message = "Dados do endereço são obrigatórios")
 *     @Valid DadosEndereco endereco) {}
 *
 *
 * Outra maneira é isolar as mensagens em um arquivo de propriedades, que deve possuir o nome
 * ValidationMessages.properties e ser criado no diretório src/main/resources:
 *
 * nome.obrigatorio=Nome é obrigatório
 * email.obrigatorio=Email é obrigatório
 * email.invalido=Formato do email é inválido
 * telefone.obrigatorio=Telefone é obrigatório
 * crm.obrigatorio=CRM é obrigatório
 * crm.invalido=Formato do CRM é inválido
 * especialidade.obrigatoria=Especialidade é obrigatória
 * endereco.obrigatorio=Dados do endereço são obrigatórios
 *
 *E, nas anotações, indicar a chave das propriedades pelo próprio atributo message, delimitando com os caracteres { e }:
 *
 * public record DadosCadastroMedico(
 *     @NotBlank(message = "{nome.obrigatorio}")
 *     String nome,
 *
 *     @NotBlank(message = "{email.obrigatorio}")
 *     @Email(message = "{email.invalido}")
 *     String email,
 *
 *     @NotBlank(message = "{telefone.obrigatorio}")
 *     String telefone,
 *
 *     @NotBlank(message = "{crm.obrigatorio}")
 *     @Pattern(regexp = "\\d{4,6}", message = "{crm.invalido}")
 *     String crm,
 *
 *     @NotNull(message = "{especialidade.obrigatoria}")
 *     Especialidade especialidade,
 *
 *     @NotNull(message = "{endereco.obrigatorio}")
 *     @Valid DadosEndereco endereco) {}
 *
 * **/

//@RestControllerAdvice esta anotation se referece ao spring boot quando e passada que
//está classe vai tratar erros.
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {

        //ex.getFieldErrors contem uma lista com todos os erro da exception.
        //os erros ficam armazenados na varialvel errors.
        var errors = ex.getFieldErrors();

        //Metodo map() do java converte uma lista para outra.
        //Metodo toList() converte o resultado em uma lista.
        return ResponseEntity.badRequest().body(errors.stream().map(DadosErroValidacao::new).toList());
        //return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity tratarErro400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }

    //Esta função se refere a um DTO interno onde são passado somente
    // dois dados que o campo e a mensagem.
    private record DadosErroValidacao(String campo, String mensagem) {
        //Construtor que recebe um objeto do tipo FieldError
        // this(error.getField(), error.getDefaultMessage()); chama o construtor padrão passando os campos especificos.
        public DadosErroValidacao(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(ValidacaoExcepition.class)
    public ResponseEntity tratarErroRegraDeNegocio(ValidacaoExcepition ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }



    }
