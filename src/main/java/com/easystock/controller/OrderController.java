package com.easystock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.model.Order;
import com.easystock.model.OrderItem;
import com.easystock.model.Product;
import com.easystock.service.exception.ProductNotFound; // Importa a exceção
import com.easystock.service.interfaces.OrderService;
import com.easystock.service.interfaces.ProductService;

@Controller
@RequestMapping("/pedidos")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	/**
	 * Endpoint da API REST para criação de um novo pedido.
	 *
	 * <p>Este método recebe um objeto {@link Order} no corpo da requisição,
	 * esperado no formato JSON. Ao receber uma requisição HTTP POST para
	 * "/api/orders", o pedido é validado e, se válido, persistido na base de dados
	 * através do serviço {@link OrderService}.</p>
	 *
	 * <p>Em caso de sucesso na criação, retorna-se um {@link ResponseEntity}
	 * com status HTTP 201 (CREATED) e o objeto {@link Order} recém-criado no corpo.</p>
	 *
	 * <p>Caso ocorra uma {@link InsufficientQuantityException} durante a criação
	 * do pedido (indicando que não há estoque suficiente para algum item),
	 * retorna-se um {@link ResponseEntity} com status HTTP 400 (BAD_REQUEST).
	 * Futuramente, pode-se refinar a resposta para incluir detalhes sobre a falha.</p>
	 *
	 * @param order O objeto {@link Order} a ser criado, enviado no corpo da requisição.
	 * @return {@link ResponseEntity} contendo o pedido criado e o status HTTP adequado.
	 */
	@PostMapping("/api/orders")
	public ResponseEntity<Order> createApi(@RequestBody Order order) {
		try {
			Order createdOrder = orderService.create(order);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
		} catch (InsufficientQuantityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	/**
	 * Endpoint da API REST para leitura de um pedido específico.
	 *
	 * <p>Este método recebe o ID do pedido através da variável de caminho "{id}".
	 * Ao receber uma requisição HTTP GET para "/api/orders/{id}", o serviço
	 * {@link OrderService} é invocado para buscar o pedido correspondente.</p>
	 *
	 * <p>Se o pedido for encontrado, retorna-se um {@link ResponseEntity}
	 * com status HTTP 200 (OK) e o objeto {@link Order} no corpo.</p>
	 *
	 * <p>Se o pedido não for encontrado, o serviço pode lançar uma exceção,
	 * que deve ser tratada para retornar um status HTTP 404 (NOT FOUND).</p>
	 *
	 * @param id O ID do pedido a ser recuperado.
	 * @return {@link ResponseEntity} contendo o pedido encontrado e o status HTTP adequado.
	 */
	@GetMapping("/api/orders/{id}")
	public ResponseEntity<Order> readApi(@PathVariable Long id) {
		Order order = orderService.read(id);
		return ResponseEntity.ok(order);
	}

	/**
	 * Endpoint da API REST para atualização de um pedido existente.
	 *
	 * <p>Este método recebe o ID do pedido através da variável de caminho "{id}"
	 * e um objeto {@link Order} com os dados atualizados no corpo da requisição.
	 * Ao receber uma requisição HTTP PUT para "/api/orders/{id}", o serviço
	 * {@link OrderService} é invocado para atualizar o pedido na base de dados.</p>
	 *
	 * <p>Em caso de sucesso na atualização, retorna-se um {@link ResponseEntity}
	 * com status HTTP 200 (OK) e o objeto {@link Order} atualizado no corpo.</p>
	 *
	 * <p>Caso ocorra uma {@link InsufficientQuantityException} ou outra exceção
	 * (por exemplo, pedido não encontrado), a resposta HTTP deve ser adequada
	 * (e.g., 400 BAD_REQUEST, 404 NOT_FOUND).</p>
	 *
	 * @param id O ID do pedido a ser atualizado.
	 * @param order O objeto {@link Order} com os dados atualizados, enviado no corpo da requisição.
	 * @return {@link ResponseEntity} contendo o pedido atualizado e o status HTTP adequado.
	 */
	@PutMapping("/api/orders/{id}")
	public ResponseEntity<Order> updateApi(@PathVariable Long id, @RequestBody Order order) {
		try {
			Order updatedOrder = orderService.update(id, order);
			return ResponseEntity.ok(updatedOrder);
		} catch (InsufficientQuantityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	/**
	 * Endpoint da API REST para exclusão de um pedido.
	 *
	 * <p>Este método recebe o ID do pedido através da variável de caminho "{id}".
	 * Ao receber uma requisição HTTP DELETE para "/api/orders/{id}", o serviço
	 * {@link OrderService} é invocado para remover o pedido da base de dados.</p>
	 *
	 * <p>Em caso de sucesso na exclusão, retorna-se um {@link ResponseEntity}
	 * com status HTTP 204 (NO_CONTENT), indicando que a operação foi bem-sucedida
	 * e não há conteúdo para retornar.</p>
	 *
	 * @param id O ID do pedido a ser excluído.
	 * @return {@link ResponseEntity} com o status HTTP 204 (NO_CONTENT).
	 */
	@DeleteMapping("/api/orders/{id}")
	public ResponseEntity<Void> deleteApi(@PathVariable Long id) {
		orderService.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Exibe a lista de todos os pedidos.
	 *
	 * @param model O modelo para passar dados para a view.
	 * @return O nome da view para listar os pedidos.
	 */
	@GetMapping
	public String listarPedidos(Model model) {
		List<Order> pedidos = orderService.findAll();
		model.addAttribute("pedidos", pedidos);
		return "lista-pedidos"; // Assegure-se que o template se chama lista-pedidos.html
	}

	/**
	 * Exibe o formulário para criar um novo pedido.
	 *
	 * @param model O modelo para passar dados para a view.
	 * @return O nome da view para o formulário de cadastro de pedido.
	 */
	@GetMapping("/novo")
	public String exibirFormularioCadastro(Model model) {
		model.addAttribute("order", new Order());
		List<Product> products = productService.findAll(); // Carrega todos os produtos disponíveis
		model.addAttribute("products", products);
		// Adiciona um item vazio para começar, se quiser ter pelo menos uma linha no formulário
		// new Order().addItem(new OrderItem(new Product(), 1, ""));
		// No template, você precisará iterar sobre order.items ou usar JavaScript para adicionar dinamicamente.
		return "cadastro-pedido";
	}

	/**
	 * Processa o envio do formulário para criar um novo pedido.
	 *
	 * @param order O objeto Order preenchido pelo formulário (será usado para o status e dados gerais).
	 * @param productIds Os IDs dos produtos selecionados.
	 * @param quantities As quantidades dos produtos selecionados.
	 * @param observations As observações para cada item.
	 * @param redirectAttributes Usado para adicionar mensagens flash após o redirecionamento.
	 * @param model O modelo para passar dados de erro de volta para a view.
	 * @return Redireciona para a lista de pedidos ou volta ao formulário em caso de erro.
	 */
	@PostMapping("/salvar")
	public String salvarPedido(@ModelAttribute Order order,
	                           @RequestParam(value = "productId", required = false) List<Long> productIds,
	                           @RequestParam(value = "quantity", required = false) List<Integer> quantities,
	                           @RequestParam(value = "observation", required = false) List<String> observations,
	                           RedirectAttributes redirectAttributes,
	                           Model model) {
	    try {
	        order.setItems(new java.util.ArrayList<>());

	        // Antes de tudo, verifique se há *qualquer* ID de produto enviado.
	        // Se productIds for nulo ou vazio, significa que nenhum item foi preenchido.
	        if (productIds == null || productIds.isEmpty()) {
	            throw new IllegalArgumentException("O pedido deve conter pelo menos um item válido.");
	        }

	        boolean hasAnyValidItem = false; // Flag para verificar se pelo menos um item válido foi processado

	        for (int i = 0; i < productIds.size(); i++) {
	            Long productId = productIds.get(i);
	            Integer quantity = quantities.get(i);
	            String observation = (observations != null && observations.size() > i) ? observations.get(i) : "";

	            // A validação para cada item deve ser mais rigorosa:
	            // - productId não pode ser nulo E precisa ser um ID válido (não a string vazia convertida para null)
	            // - quantity não pode ser nula E precisa ser maior que 0
	            if (productId != null && quantity != null && quantity > 0) {
	                try {
	                    Product product = productService.read(productId);
	                    OrderItem item = new OrderItem(product, quantity, observation);
	                    order.addItem(item);
	                    hasAnyValidItem = true; // Um item válido foi adicionado
	                } catch (ProductNotFound e) {
	                    // Trata o caso onde o produto não é encontrado para um productId específico
	                    // Você pode optar por pular este item ou lançar uma exceção mais específica aqui
	                    System.err.println("Produto com ID " + productId + " não encontrado. Ignorando item.");
	                    // Não lança exceção para não interromper a submissão de outros itens válidos
	                }
	            } else {
	                // Opcional: logar ou ignorar itens com productId nulo/vazio ou quantidade inválida
	                System.out.println("Item inválido detectado (produto: " + productId + ", quantidade: " + quantity + "). Ignorando.");
	            }
	        }

	        // Após o loop, verifica se *nenhum* item válido foi adicionado.
	        // Se hasAnyValidItem ainda for false, significa que todos os itens eram inválidos.
	        if (!hasAnyValidItem) {
	            throw new IllegalArgumentException("Nenhum item válido foi adicionado ao pedido. Por favor, selecione um produto e informe uma quantidade válida.");
	        }

	        orderService.create(order);
	        redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido cadastrado com sucesso!");
	        return "redirect:/pedidos";
	    } catch (ProductNotFound e) { // Esta exceção agora é mais específica para um item
	        model.addAttribute("erro", "Erro ao processar um dos produtos: " + e.getMessage());
	        // ... (resto do tratamento de erro)
	        return "cadastro-pedido";
	    } catch (InsufficientQuantityException e) {
	        model.addAttribute("erro", e.getMessage());
	        // ... (resto do tratamento de erro)
	        return "cadastro-pedido";
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("erro", e.getMessage());
	        model.addAttribute("order", order);
	        model.addAttribute("products", productService.findAll());
	        return "cadastro-pedido";
	    } catch (Exception e) {
	        model.addAttribute("erro", "Erro inesperado ao salvar pedido: " + e.getMessage());
	        model.addAttribute("order", order);
	        model.addAttribute("products", productService.findAll());
	        return "cadastro-pedido";
	    }
	}

	/**
	 * Exibe os detalhes de um pedido específico.
	 *
	 * @param id O ID do pedido.
	 * @param model O modelo para passar dados para a view.
	 * @param redirectAttributes Usado para adicionar mensagens flash após o redirecionamento.
	 * @return O nome da view para exibir os detalhes do pedido ou redireciona para a lista em caso de erro.
	 */
	@GetMapping("/{id}")
	public String exibirDetalhesPedido(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Order pedido = orderService.read(id);
			model.addAttribute("pedido", pedido);
			return "detalhes-pedidos";
		} catch (ProductNotFound | com.easystock.exception.OrderNotFoundException e) { // Captura ambas as exceções
			redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado: " + e.getMessage());
			return "redirect:/pedidos";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar detalhes do pedido: " + e.getMessage());
			return "redirect:/pedidos";
		}
	}

	/**
	 * Exibe o formulário para editar um pedido existente.
	 *
	 * @param id O ID do pedido a ser editado.
	 * @param model O modelo para passar dados para a view.
	 * @param redirectAttributes Usado para adicionar mensagens flash após o redirecionamento.
	 * @return O nome da view para o formulário de edição de pedido ou redireciona para a lista em caso de erro.
	 */
	@GetMapping("/editar/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.read(id);
			model.addAttribute("order", order);
			List<Product> products = productService.findAll();
			model.addAttribute("products", products);
			return "editar-pedido"; // Assumindo que você terá um template editar-pedido.html
		} catch (ProductNotFound | com.easystock.exception.OrderNotFoundException e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado ou erro ao carregar para edição.");
			return "redirect:/pedidos";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Ocorreu um erro ao carregar o pedido para edição: " + e.getMessage());
			return "redirect:/pedidos";
		}
	}

	/**
	 * Processa o envio do formulário para atualizar um pedido existente.
	 *
	 * @param id O ID do pedido a ser atualizado.
	 * @param order O objeto Order com os dados atualizados do formulário.
	 * @param redirectAttributes Usado para adicionar mensagens flash após o redirecionamento.
	 * @return Redireciona para a lista de pedidos ou volta ao formulário em caso de erro.
	 */
	@PostMapping("/editar/{id}")
	public String atualizarPedido(@PathVariable Long id, @ModelAttribute Order order,
								  @RequestParam(value = "productId", required = false) List<Long> productIds,
								  @RequestParam(value = "quantity", required = false) List<Integer> quantities,
								  @RequestParam(value = "observation", required = false) List<String> observations,
								  RedirectAttributes redirectAttributes, Model model) {
		try {
			Order existingOrder = orderService.read(id); // Carrega o pedido existente
			existingOrder.setStatus(order.getStatus()); // Atualiza o status

			// Limpa os itens existentes e adiciona os novos/atualizados
			existingOrder.getItems().clear(); // Importante limpar para evitar duplicatas ou órfãos
			double newTotalValue = 0.0;

			if (productIds == null || productIds.isEmpty() || productIds.get(0) == null) {
				throw new IllegalArgumentException("O pedido deve conter pelo menos um item válido.");
			}

			for (int i = 0; i < productIds.size(); i++) {
				Long productId = productIds.get(i);
				Integer quantity = quantities.get(i);
				String observation = (observations != null && observations.size() > i) ? observations.get(i) : "";

				if (productId == null || quantity == null || quantity <= 0) {
					continue; // Pula itens inválidos
				}

				Product product = productService.read(productId);
				OrderItem item = new OrderItem(product, quantity, observation);
				item.setOrder(existingOrder); // Associa o item ao pedido existente
				existingOrder.addItem(item); // Adiciona ao pedido existente
				newTotalValue += item.getSubTotal().doubleValue();
			}
			existingOrder.setTotalValue(newTotalValue); // Atualiza o total manualmente após adicionar itens

			orderService.update(id, existingOrder); // Passa o pedido existente atualizado
			redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido atualizado com sucesso!");
			return "redirect:/pedidos";
		} catch (ProductNotFound | com.easystock.exception.OrderNotFoundException e) {
			model.addAttribute("erro", "Erro de recurso: " + e.getMessage());
			model.addAttribute("order", order); // Mantém os dados no formulário
			model.addAttribute("products", productService.findAll());
			return "editar-pedido"; // Volta para a página de edição
		} catch (InsufficientQuantityException | IllegalArgumentException e) {
			model.addAttribute("erro", e.getMessage());
			model.addAttribute("order", order);
			model.addAttribute("products", productService.findAll());
			return "editar-pedido";
		} catch (Exception e) {
			model.addAttribute("erro", "Erro ao atualizar pedido: " + e.getMessage());
			model.addAttribute("order", order);
			model.addAttribute("products", productService.findAll());
			return "editar-pedido";
		}
	}

	/**
	 * Exclui um pedido.
	 *
	 * @param id O ID do pedido a ser excluído.
	 * @param redirectAttributes Usado para adicionar mensagens flash após o redirecionamento.
	 * @return Redireciona para a lista de pedidos.
	 */
	@GetMapping("/excluir/{id}")
	public String excluirPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			orderService.delete(id);
			redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido excluído com sucesso!");
		} catch (com.easystock.exception.OrderNotFoundException e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Pedido não encontrado: " + e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir pedido: " + e.getMessage());
		}
		return "redirect:/pedidos";
	}
}