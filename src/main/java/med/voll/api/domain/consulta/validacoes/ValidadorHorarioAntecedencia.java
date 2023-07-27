package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoExcepition;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();

        //Pega ao hora real
        var agora = LocalDateTime.now();

        //pega a duração em minutos.
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if(diferencaEmMinutos < 30){
            throw new ValidacaoExcepition("Consulta deve ser agendada com antecedência minima de 30 minutos!");
        }

    }

}
