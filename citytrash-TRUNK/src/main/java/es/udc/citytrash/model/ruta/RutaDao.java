package es.udc.citytrash.model.ruta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface RutaDao extends GenericDAO<Ruta, Integer> {

	/**
	 * 
	 * @param pageable
	 * @param tiposDeBasura
	 * @param trabajadores
	 * @param contenedores
	 * @param camiones
	 * @param mostrarSoloContenedoresDeAlta
	 * @return
	 */
	/*
	 * Page<Ruta> buscarRutas(Pageable pageable, List<TipoDeBasura>
	 * tiposDeBasura, List<Trabajador> trabajadores, List<Contenedor>
	 * contenedores, List<Camion> camiones, boolean
	 * mostrarSoloContenedoresDeAlta);
	 */

	/**
	 * 
	 * @param pageable
	 * @param tiposDeBasura
	 * @param trabajadores
	 * @param contenedores
	 * @param camiones
	 * @param mostrarSoloRutasActivas
	 * @return
	 */
	Page<Ruta> buscarRutas(Pageable pageable, List<Integer> tiposDeBasura, List<Long> trabajadores,
			List<Long> contenedores, List<Long> camiones, boolean mostrarSoloRutasActivas);
}