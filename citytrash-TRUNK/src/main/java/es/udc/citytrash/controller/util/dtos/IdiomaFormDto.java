package es.udc.citytrash.controller.util.dtos;

import es.udc.citytrash.business.entity.idioma.Idioma;

public class IdiomaFormDto {
	
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
