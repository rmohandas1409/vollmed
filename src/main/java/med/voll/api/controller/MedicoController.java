package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    //A classe UriComponentsBuilder fica responsavel para criar o endereço a aplicação quando tem um retorno
    //sendo assim ela monta a url da aplicação para não precisar passar manualmente tornando mais agil a manutenção do codigo.
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {

        //Recebe os paramentros que veio do json
        //para os metodos construtores que estão na Entities Medico e Endereco  e salva no banco.
        var medico = new Medico(dados);
        repository.save(medico);

        //uriBuilder.path o complemento do metodo fica responsavel por criar o resto do complemento da url pois o  uriBuilder
        //somente monta o http://localhost e com o complemnto path podemos passar o restante da url sendo assim http://localhost/medicos/id.
        //buildAndExpand(medico.getId()) pega o id do medico que foi retornada na variavel medico e subititui pela variavel {id},
        // logo na sequencia vem o toUri() para montar o abjeto uri.
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        //No retorno passamos o metodo de retorno created(uri) passondo o objeto criado montando o corpo do retorno com
        //metodo body().
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();

        //O metodo build monta o retorno confome o metodo acima
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    //Metodo de usuario autenticado por perfil.
    //@Secured("ROLE_ADMIN")
    // O metodo @PathVariable pega a variavel que foi passada na url lembrando que temos que passar no json
    // a nomeclatura igual foi especificada na função.
    public ResponseEntity detalhar(@PathVariable Long id) {

        //Metodo que retorna um resutado do banco de dados.
        var medico = repository.getReferenceById(id);

        //ResponseEntity.ok retorna um codigo 200 com o DTO de retorno.
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }


}
