/*-
 * ========================LICENSE_START=================================
 * TFG BYTEPIT-APP
 * %%
 * Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */
package es.udc.tfg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tf.controller.wrapers.CityWraper;
import es.udc.tf.controller.wrapers.DumpsterResource;
import es.udc.tfg.model.dumpster.Dumpster;
import es.udc.tfg.service.DumpsterService;
import es.udc.tfg.util.exceptions.InstanceNotFoundException;

/**
 * Rest controller default root
 * 
 * @author hmia
 *
 */
@RestController
@RequestMapping("/v1/contenedores")
public class ContenedoresController {
	private static final Logger logger = LoggerFactory.getLogger(ContenedoresController.class);

	@Inject
	DumpsterService dService;

	// https://github.com/spring-projects/spring-boot/blob/master/spring-boot-samples/spring-boot-sample-hateoas/src/main/java/sample/hateoas/web/CustomerController.java
	// https://opencredo.com/hal-hypermedia-api-spring-hateoas/

	@RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Resources<DumpsterResource>> findDumpsters() throws InstanceNotFoundException {
		List<Dumpster> dumpsters = dService.findDumpsters();
		List<DumpsterResource> dumpsterResource = new ArrayList<DumpsterResource>(dumpsters.size());

		for (Dumpster d : dumpsters) {
			CityWraper cw = new CityWraper(d.getTownhall().getName(), d.getTownhall().getLatitude(),
					d.getTownhall().getLongitude());
			Link ldetailCity = linkTo(MyRestController.class).slash("/").slash(d.getTownhall().getId()).withSelfRel();
			cw.add(ldetailCity);
			DumpsterResource resource = new DumpsterResource(d.getDumpsterId(), cw, d.getLatitude(), d.getLongitude(),
					d.getDumspterType().getName());
			Link ldetailController = linkTo(MyRestController.class).slash(d.getDumpsterId()).withSelfRel();
			resource.add(ldetailController);
			dumpsterResource.add(resource);
		}
		Resources<DumpsterResource> wrapped = new Resources<DumpsterResource>(dumpsterResource,
				linkTo(ContenedoresController.class).withSelfRel());
		return new ResponseEntity<Resources<DumpsterResource>>(wrapped, HttpStatus.OK);
	}

	@RequestMapping(value = "/paged", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<PagedResources<DumpsterResource>> findDumpsterPag() throws InstanceNotFoundException {
		List<Dumpster> dumpsters = dService.findDumpsters();
		List<DumpsterResource> dumpsterResource = new ArrayList<DumpsterResource>(dumpsters.size());

		for (Dumpster d : dumpsters) {
			CityWraper cw = new CityWraper(d.getTownhall().getName(), d.getTownhall().getLatitude(),
					d.getTownhall().getLongitude());
			Link ldetailCity = linkTo(ContenedoresController.class).slash(cw.getId()).withRel("city");
			cw.add(ldetailCity);
			DumpsterResource resource = new DumpsterResource(d.getDumpsterId(), cw, d.getLatitude(), d.getLongitude(),
					d.getDumspterType().getName());
			Link ldetailController = linkTo(ContenedoresController.class).slash(d.getDumpsterId()).withSelfRel();
			resource.add(ldetailController);
			dumpsterResource.add(resource);
		}
		PageMetadata pageMetadata = new PageMetadata(5, 1, dumpsterResource.size(), dumpsterResource.size() / 5);
		// Resources<DumpsterResource> wrapped = new
		// Resources<DumpsterResource>(dumpsterResource,
		// linkTo(ContenedoresController.class).withSelfRel());
		PagedResources<DumpsterResource> pagedResources = new PagedResources<DumpsterResource>(dumpsterResource,
				pageMetadata);
		return new ResponseEntity<PagedResources<DumpsterResource>>(pagedResources, HttpStatus.OK);
	}

	@RequestMapping(value = "{/contenedor/{dumpsterid}/ciudad/{towerId}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Dumpster> findDumpster(@PathVariable long towerId, @PathVariable String dumpsterId)
			throws InstanceNotFoundException {
		Dumpster d = null;
		d = dService.findDumpster(dumpsterId, towerId);

		return new ResponseEntity<Dumpster>(d, HttpStatus.OK);
	}

	@ExceptionHandler({ InstanceNotFoundException.class })
	public ResponseEntity<String> gestionarNoExistentes(Exception e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ResponseBody
	public Dumpster findDumpster() {
		return null;
	}

}
