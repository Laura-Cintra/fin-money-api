package br.com.fiap.fin_money_api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.fin_money_api.model.Category;
import br.com.fiap.fin_money_api.repository.CategoryRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories") // como todos os métodos usavam o mesmo caminho
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private Logger log = LoggerFactory.getLogger(getClass()); // objeto para log no terminal

    // private List<Category> repository = new ArrayList<>(); // banco de dados em memória
    
    @Autowired // Injeção de Dependência
    private CategoryRepository repository;

    // @RequestMapping(produces = "application/json", path = "/categories", method =
    // {RequestMethod.GET})
    // mapeia requisições para este método

    @GetMapping() // mapeia requisições do tipo GET, path é um atributo padrão, por isso omitimos
    public List<Category> index() {
        return repository.findAll();
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED) // se o método foi criado com sucesso, a mensagem será "created"
    public Category create(@RequestBody @Valid Category category) { // mostrando que a categoria estará no corpo da requisição
        log.info("Cadastrando categoria " + category.getName());
        return repository.save(category); // inserindo uma categoria no bd ||  add -> save
    }

    @GetMapping("{id}")
    // ResponseEntity - eu controlo o que vai ser enviado na resposta
    public ResponseEntity<Category> get(@PathVariable Long id) {
        log.info("Buscando categoria " + id);
        return ResponseEntity.ok(getCategory(id));

        /*
         * // verificando se o optional tá vazio
         * if (category.isEmpty()) {
         * return ResponseEntity.status(404).build(); // build() - construindo uma
         * mensagem de resposta sem corpo
         * }
         * // body() constrói a mensagem com corpo
         * return ResponseEntity.status(200).body(category.get()); // "abrindo a caixa"
         * e pegando a categoria dentro dela,
         * // que pode ser
         */
    }

    // Apagar
    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Apagando categoria " + id);
        repository.delete(getCategory(id)); // usando direto porque não precisa de uma variável que só vai usar 1 vez
        return ResponseEntity.noContent().build(); // o retorno de sucesso é 204
    }

    // Editar
    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid Category category) {
        log.info("Atualizando categoria + " + id + " " + category);

        // repository.remove(getCategory(id)); // tirando os dados antigos
        getCategory(id); // evitando que o id seja atualizado
        category.setId(id); // setando o id
        repository.save(category); // adicionando os novos dados
        return ResponseEntity.ok(category);
    }

    private Category getCategory(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        
        /*
                / stream - fluxo de dados, pega uma coleção e retorna um fluxo que pode ser
        / consumido por uma outra função
        return repository.stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        / pega o primeiro ou, senão, lança uma exceção
         */
    }
}
