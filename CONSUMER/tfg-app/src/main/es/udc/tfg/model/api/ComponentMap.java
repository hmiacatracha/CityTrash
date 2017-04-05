package es.udc.tfg.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentMap {
	
	private List<Component> component;
	
	public ComponentMap() {
		
	}

	public ComponentMap(List<Component> component) {		
		this.component = component;
	}

	public List<Component> getComponents() {
		return component;
	}

	public void setComponents(List<Component> component) {
		this.component = component;
	}

	@Override
	public String toString() {
		return " {components=" + component + "}";
	}
}
