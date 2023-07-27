package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoExcepition;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados){

        var dataConsulta = dados.data();

        //getDayOfWeek pegar o dia da semana.
        //equals verifica se é igual.
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);

        //retorna para vareavel se o horario e menos que 07:00.
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;

        //retorna para vareavel se o horario e maior que 18:00.
        var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;

        if(domingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica){
            throw new ValidacaoExcepition("Consulta fora do horário de funcionamento da cliníca");
        }

    }
}
