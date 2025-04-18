package com.easystock.exception.productException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



//trata todas as excessoes do pacote product
@RestControllerAdvice
public class ProductExceptionHandler {

	//Produto não encontrado -erro 404  
	@ExceptionHandler(RegisteredProductException.class)
	public ResponseEntity<String> handleRegisteredProductException(RegisteredProductException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	
	//nome produto invalido -erro 400
	@ExceptionHandler(InvalidNameProductException.class)
	public ResponseEntity<String> handleInvalidNameProductException(InvalidNameProductException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	//produto insuficiente -erro 400
	@ExceptionHandler(InsufficientQuantityException.class)
	public ResponseEntity<String> handleInsufficientQuantityException(InsufficientQuantityException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	// estoque insuficiente - erro 409
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException e){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	//preço invalido - erro 400
	@ExceptionHandler(InvalidPriceException.class)
	public ResponseEntity<String> handleInvalidPriceException(InvalidPriceException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	//produto invalido - erro 400
	@ExceptionHandler(ProductInvalidException.class)
	public ResponseEntity<String> handleProductInvalidException(ProductInvalidException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	//categoria do produto obrigatoria - erro 400
	@ExceptionHandler(CategoryRequiredException.class)
	public ResponseEntity<String> handleCategoryRequiredException(CategoryRequiredException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	//Produto ja cadastrado - erro 400
	@ExceptionHandler(ProductAlreadyRegisteredException.class)
	public ResponseEntity<String> handleProductAlreadyRegisteredException(ProductAlreadyRegisteredException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	//captura qualquer outra excessão não tratada - erro 500
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handlerGenericException(Exception e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no sistema: " + e.getMessage());
	}
	
	
}
