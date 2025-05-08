package br.com.fiap.fin_money_api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.com.fiap.fin_money_api.model.User;
import br.com.fiap.fin_money_api.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categories") // como todos os métodos usavam o mesmo caminho
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CategoryController {

    // private Logger log = LoggerFactory.getLogger(getClass()); // objeto para log no terminal --> @Slf4j

    // private List<Category> repository = new ArrayList<>(); // banco de dados em memória
    
    @Autowired // Injeção de Dependência
    private CategoryRepository repository;

    // @RequestMapping(produces = "application/json", path = "/categories", method =
    // {RequestMethod.GET})
    // mapeia requisições para este método

    @GetMapping // mapeia requisições do tipo GET, path é um atributo padrão, por isso omitimos
    @Operation(summary = "Listar categorias", description = "Retorna um array com todas as categorias")
    @Cacheable("categories")
    public List<Category> index(@AuthenticationPrincipal User user) {
        return repository.findByUser(user);
    }

    @PostMapping
    @CacheEvict(value = "categories", allEntries = true)
    @Operation(responses = @ApiResponse(responseCode = "400", description = "Validação falhou"))
    @ResponseStatus(code = HttpStatus.CREATED) // se o método foi criado com sucesso, a mensagem será "created"
    public Category create(@RequestBody @Valid Category category, @AuthenticationPrincipal User user) { // mostrando que a categoria estará no corpo da requisição
        log.info("Cadastrando categoria " + category.getName());
        category.setUser(user);
        return repository.save(category); // inserindo uma categoria no bd ||  add -> save
    }

    @GetMapping("{id}")
    // ResponseEntity - eu controlo o que vai ser enviado na resposta
    public ResponseEntity<Category> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.info("Buscando categoria " + id);
        return ResponseEntity.ok(getCategory(id, user));

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
    public ResponseEntity<Object> destroy(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.info("Apagando categoria " + id);
        repository.delete(getCategory(id, user)); // usando direto porque não precisa de uma variável que só vai usar 1 vez
        return ResponseEntity.noContent().build(); // o retorno de sucesso é 204
    }

    // Editar
    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid Category category, @AuthenticationPrincipal User user) {
        log.info("Atualizando categoria + " + id + " " + category);

        // repository.remove(getCategory(id)); // tirando os dados antigos
        var oldCategory = getCategory(id, user); // evitando que o id seja atualizado
        
        // category.setId(id); // setando o id
        // category.setUser(user); // garantindo que o usuário esteja logado
        BeanUtils.copyProperties(category, oldCategory, "id", "user"); // objeto original do BD, substitui pelo que estamos adicionando, campos que quero ignorar
        
        repository.save(oldCategory); // adicionando os novos dados
        return ResponseEntity.ok(oldCategory);
    }

    private Category getCategory(Long id, User user) {
        var category = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        
        if(!category.getUser().equals(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return category;
        
        /*
                / stream - fluxo de dados, pega uma coleção e retorna um fluxo que pode ser
        / consumido por uma outra função
        return repository.stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        / pega o primeiro ou, senão, lança uma exceção
         */
    }
}
