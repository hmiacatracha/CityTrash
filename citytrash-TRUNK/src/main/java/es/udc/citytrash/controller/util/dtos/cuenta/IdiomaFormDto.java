package es.udc.citytrash.controller.util.dtos.cuenta;

import es.udc.citytrash.util.enums.Idioma;

public class IdiomaFormDto {
	
	public IdiomaFormDto(){
		
	}
	
	public IdiomaFormDto(Idioma idioma){
		this.idioma = idioma;
	}
	
	private Idioma idioma;

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	@Override
	public String toString() {
		return "IdiomaForm [lang=" + idioma + "]";
	}

}
