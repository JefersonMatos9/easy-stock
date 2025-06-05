package com.easystock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easystock.model.Product;
import com.easystock.service.interfaces.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * Endpoint da API REST para criar um novo produto.
	 *
	 * <p>Este método recebe um objeto {@link Product} no corpo da requisição,
	 * esperado no formato JSON. Ao receber uma requisição HTTP POST para
	 * "/api/products", o produto é persistido na base de dados através do
	 * serviço {@link ProductService}.</p>
	 *
	 * <p>Em caso de sucesso, retorna-se um {@link ResponseEntity} com status
	 * HTTP 201 (CREATED) e o objeto {@link Product} recém-criado no corpo.</p>
	 *
	 * @param product O objeto {@link Product} a ser criado, enviado no corpo da requisição.
	 * @return {@link ResponseEntity} contendo o produto criado e o status HTTP adequado.
	 */
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product) {
		Product createdProduct = productService.createProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	}

	/**
	 * Endpoint da API REST para buscar um produto específico pelo seu ID.
	 *
	 * <p>Este método recebe o ID do produto através da variável de caminho "{id}".
	 * Ao receber uma requisição HTTP GET para "/api/products/{id}", o serviço
	 * {@link ProductService} é invocado para buscar o produto correspondente.</p>
	 *
	 * <p>Se o produto for encontrado, retorna-se um {@link ResponseEntity}
	 * com status HTTP 200 (OK) e o objeto {@link Product} no corpo.</p>
	 *
	 * <p>Considerar a implementação de tratamento para o caso em que o produto
	 * não seja encontrado, retornando um status HTTP 404 (NOT FOUND).</p>
	 *
	 * @param id O identificador único do produto a ser buscado.
	 * @return {@link ResponseEntity} contendo o produto encontrado e o status HTTP 200.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> read(@PathVariable Long id) {
		Product product = productService.read(id);
		return ResponseEntity.ok(product);
	}

	/**
	 * Endpoint da API REST para atualizar um produto existente.
	 *
	 * <p>Este método recebe o ID do produto a ser atualizado através da variável
	 * de caminho "{id}" e um objeto {@link Product} com os dados atualizados
	 * no corpo da requisição (formato JSON). Ao receber uma requisição HTTP PUT
	 * para "/api/products/{id}", o serviço {@link ProductService} é utilizado
	 * para realizar a atualização na base de dados.</p>
	 *
	 * <p>Em caso de sucesso, retorna-se um {@link ResponseEntity} com status
	 * HTTP 200 (OK) e o objeto {@link Product} atualizado no corpo.</p>
	 *
	 * <p>É importante garantir que o produto com o ID fornecido realmente exista
	 * antes de tentar a atualização. Uma validação e tratamento para o caso
	 * de produto não encontrado (retornando 404 NOT FOUND) seria recomendável.</p>
	 *
	 * @param id    O identificador único do produto a ser atualizado.
	 * @param product O objeto {@link Product} com os novos dados.
	 * @return {@link ResponseEntity} contendo o produto atualizado e o status HTTP 200.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
		Product updateProduct = productService.update(id, product);
		return ResponseEntity.ok(updateProduct);
	}

	/**
	 * Endpoint da API REST para excluir um produto pelo seu ID.
	 *
	 * <p>Este método recebe o ID do produto a ser excluído através da variável
	 * de caminho "{id}". Ao receber uma requisição HTTP DELETE para
	 * "/api/products/{id}", o serviço {@link ProductService} é invocado para
	 * remover o produto da base de dados.</p>
	 *
	 * <p>Em caso de sucesso na exclusão, retorna-se um {@link ResponseEntity}
	 * com status HTTP 200 (OK). Considerar a possibilidade de retornar
	 * status HTTP 204 (NO_CONTENT) para indicar sucesso sem conteúdo no corpo.</p>
	 *
	 * @param id O identificador único do produto a ser excluído.
	 * @return {@link ResponseEntity} com status HTTP 200.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id) {
		Product deleteProduct = productService.delete(id);
		return ResponseEntity.ok(deleteProduct);
	}
}