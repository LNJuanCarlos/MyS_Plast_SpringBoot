package com.mysplast.springboot.backend.controller;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysplast.springboot.backend.model.entity.InventarioFisico;
import com.mysplast.springboot.backend.model.service.InventarioFisicoService;

@CrossOrigin(origins= {"http://localhost:4200"})
@Controller
@RequestMapping("/inventariofisico")
public class InventarioFisicoController {
	
	@Autowired
	private InventarioFisicoService inventariofisicoservice;
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/listar")
	public ResponseEntity<?> listadoInventarioFisico(){
		
		List<InventarioFisico> inventariosfisicos = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			inventariosfisicos = inventariofisicoservice.listarInventariosFisicos();
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<InventarioFisico>>(inventariosfisicos, HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/listartop")
	public ResponseEntity<?> listadoTop50InventarioFisico(){
		
		List<InventarioFisico> inventariosfisicos = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			inventariosfisicos = inventariofisicoservice.listarTop50InventariosFisicos();
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<InventarioFisico>>(inventariosfisicos, HttpStatus.OK);
	}
	
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/buscar/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable String id){
		
		InventarioFisico inventariofisico = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			inventariofisico = inventariofisicoservice.buscarInventarioFisicoxID(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(inventariofisico == null) {
			response.put("mensaje", "El Almacen con el ID: ".concat(id.toString().concat("no existe!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<InventarioFisico>(inventariofisico, HttpStatus.OK);
		
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminarPorId(@PathVariable String id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			inventariofisicoservice.eliminarInventarioFisicoxID(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha eliminado el Almacén correctamente!");
		return new ResponseEntity<Map<String, Object> >(response, HttpStatus.OK);
		
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@PutMapping("/aprobar/{id}")
	public ResponseEntity<?> aprobarInventarioFisico(@RequestBody InventarioFisico inventarioFisico,
			@PathVariable String id) {
		InventarioFisico inventariofisicoActual = inventariofisicoservice.buscarInventarioFisicoxID(id);
		InventarioFisico inventarioFisicoActualizado = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();

		if (inventariofisicoActual == null) {
			response.put("mensaje", "El ordencompra con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (inventariofisicoActual.getESTADO().equals("A")) {
			response.put("mensaje", "El Inventario Físico con el ID:" + id.toString()
					+ "se encuentra aprobado, comuníquese con el encargado de almacén!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			inventariofisicoActual.setESTADO("A");
			inventariofisicoActual.setMOD_USER(authentication.getName());
			inventariofisicoActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			inventarioFisicoActualizado = inventariofisicoservice.grabarInventarioFisico(inventariofisicoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Inventario Físico!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El Inventario Físico ha sido actualizado con éxito!");
		response.put("inventariofisico", inventarioFisicoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	
	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@PutMapping("/anular/{id}")
	public ResponseEntity<?> anularInventarioFisico(@RequestBody InventarioFisico inventarioFisico,
			@PathVariable String id) {
		InventarioFisico inventariofisicoActual = inventariofisicoservice.buscarInventarioFisicoxID(id);
		InventarioFisico inventarioFisicoActualizado = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();

		if (inventariofisicoActual == null) {
			response.put("mensaje", "El ordencompra con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (inventariofisicoActual.getESTADO().equals("N")) {
			response.put("mensaje", "El Inventario Físico con el ID:" + id.toString()
					+ "se encuentra anulado, comuníquese con el encargado de almacén!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			inventariofisicoActual.setESTADO("N");
			inventariofisicoActual.setMOD_USER(authentication.getName());
			inventariofisicoActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			inventarioFisicoActualizado = inventariofisicoservice.grabarInventarioFisico(inventariofisicoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Inventario Físico!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El Inventario Físico ha sido actualizado con éxito!");
		response.put("inventariofisico", inventarioFisicoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@PutMapping("/regularizar/{id}")
	public ResponseEntity<?> regularizarInventarioFisico(@RequestBody InventarioFisico inventarioFisico,
			@PathVariable String id) {
		InventarioFisico inventariofisicoActual = inventariofisicoservice.buscarInventarioFisicoxID(id);
		InventarioFisico inventarioFisicoActualizado = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();

		if (inventariofisicoActual == null) {
			response.put("mensaje", "El ordencompra con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (inventariofisicoActual.getESTADO().equals("N")) {
			response.put("mensaje", "El Inventario Físico con el ID:" + id.toString()
					+ "se encuentra anulado, comuníquese con el encargado de almacén!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			inventariofisicoActual.setESTADO("R");
			inventariofisicoActual.setMOD_USER(authentication.getName());
			inventariofisicoActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			inventarioFisicoActualizado = inventariofisicoservice.grabarInventarioFisico(inventariofisicoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Inventario Físico!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El Inventario Físico ha sido actualizado con éxito!");
		response.put("inventariofisico", inventarioFisicoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@PostMapping("/crear")
	public ResponseEntity<?> crearAlmacen(@RequestBody InventarioFisico inventariofisico){
		
		List<InventarioFisico> inventarioObtenidoxFecha = null;
		InventarioFisico nuevoinventariofisico = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();
		inventarioObtenidoxFecha = inventariofisicoservice.buscarInventarioFisicoxFecha(inventariofisico.getFECHA(), inventariofisico.getId_SECTOR().getID_SECTOR());
	

		
		
		try {

			if(inventarioObtenidoxFecha.size() == 0) {
				inventariofisico.setESTADO("P");
				inventariofisico.setREG_USER(authentication.getName());
				inventariofisico.setFECH_REG_USER(ZonedDateTime.now().toLocalDate().toString());
				nuevoinventariofisico =	inventariofisicoservice.grabarInventarioFisico(inventariofisico);
			} else {
				response.put("mensaje", "La fecha con el cual quiere ingresar el inventario, ya se encuentra ingresada, verificar!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
			}

		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha grabado correctamente el Inventario Físico!");
		response.put("inventariofisico", nuevoinventariofisico);
		return new ResponseEntity<Map<String, Object> >(response, HttpStatus.OK);
		
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN", "ROLE_JEFE", "ROLE_LOGISTICA" })
	@GetMapping("/filtro")
	public ResponseEntity<?> filtroInventarioFisico(@RequestParam(value = "sector", required = false) String sector,
			@RequestParam(value = "fecha1", required = false) String fecha1,
			@RequestParam(value = "fecha2", required = false) String fecha2) {

		List<InventarioFisico> inventariosFisicosFiltrados = null;

		Map<String, Object> response = new HashMap<>();

		if (sector.equals("")  || fecha1.equals("") || fecha2.equals("")) {
			response.put("mensaje", "Todos los campos para filtrar son obligatorios!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		
		if (sector.equals("")) {
			sector = null;
		}
		if (fecha1.equals("")) {
			fecha1 = null;
		}
		if (fecha2.equals("")) {
			fecha2 = null;
		}

		try {
			inventariosFisicosFiltrados = inventariofisicoservice.filtrarInventariosFisicos(sector, fecha1, fecha2);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<InventarioFisico>>(inventariosFisicosFiltrados, HttpStatus.OK);

	}

}
