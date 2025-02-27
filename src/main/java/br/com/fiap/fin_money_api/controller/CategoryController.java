package br.com.fiap.fin_money_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.fin_money_api.model.Category;

@RestController
public class CategoryController {

    private List<Category> repository = new ArrayList<>(); // banco de dados em memória

    // @RequestMapping(produces = "application/json", path = "/categories", method =
    // {RequestMethod.GET})
    // mapeia requisições para este método

    @GetMapping("/categories") // mapeia requisições do tipo GET, path é um atributo padrão, por isso omitimos
    public List<Category> index() {
        return repository;
    }

    @PostMapping("/categories")
    @ResponseStatus(code = HttpStatus.CREATED) // se o método foi criado com sucesso, a mensagem será "created"
    public Category create(@RequestBody Category category) { // mostrando que a categoria estará no corpo da requisição
        System.out.println("Cadastrando categoria " + category.getName());
        repository.add(category);
        return category;
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id) { // ResponseEntity - eu controlo o que vai ser enviado
                                                                 // na resposta
        System.out.println("Buscando categoria " + id);
        var category = repository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst(); // stream - fluxo de dados, pega uma coleção e retorna um fluxo que pode ser
                              // consumido por uma outra função

        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category.get());

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
}
