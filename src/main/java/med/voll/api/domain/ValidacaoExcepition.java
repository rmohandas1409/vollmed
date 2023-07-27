package med.voll.api.domain;

public class ValidacaoExcepition extends RuntimeException {
    public ValidacaoExcepition(String mensagem) {
        super(mensagem);
    }
}
