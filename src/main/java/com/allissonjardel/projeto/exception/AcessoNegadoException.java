package com.allissonjardel.projeto.exception;

@SuppressWarnings("serial")
public class AcessoNegadoException extends RuntimeException{
	
	public AcessoNegadoException(String message) {
		super(message);
	}

}
