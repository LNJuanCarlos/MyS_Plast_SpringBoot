package com.mysplast.springboot.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysplast.springboot.backend.model.entity.ConsultaGatosMes;
import com.mysplast.springboot.backend.model.entity.InventarioFisico;
import com.mysplast.springboot.backend.model.entity.Ordencompra;
import com.mysplast.springboot.backend.model.entity.Ordenprod;
import com.mysplast.springboot.backend.model.entity.TopProductosKardex;
import com.mysplast.springboot.backend.model.entity.TopProductosProduccion;
import com.mysplast.springboot.backend.model.service.InventarioFisicoService;
import com.mysplast.springboot.backend.model.service.KardexService;
import com.mysplast.springboot.backend.model.service.OrdencompraService;
import com.mysplast.springboot.backend.model.service.OrdenprodService;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private OrdencompraService ordenservice;
	
	@Autowired
	private OrdenprodService ordenprodservice;
	
	@Autowired
	private InventarioFisicoService  inventariofisicoservice;
	
	@Autowired
	private KardexService kardexservice;
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/topproductosproduccion")
	public ResponseEntity<?> topProductosProduccion(){
		
		List<TopProductosProduccion> topproductosProduccion = null;

		Map<String, Object> response = new HashMap<>();

		try {
			topproductosProduccion = ordenprodservice.topProductosProduccion();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<TopProductosProduccion>>(topproductosProduccion, HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/topproductosegresos")
	public ResponseEntity<?> topProductosEgresos(){
		
		List<TopProductosKardex> topegresos = null;

		Map<String, Object> response = new HashMap<>();

		try {
			topegresos = kardexservice.topProductosEgresos();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<TopProductosKardex>>(topegresos, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/topproductosingresos")
	public ResponseEntity<?> TopProductosIngresos(){
		
		List<TopProductosKardex> topingresos = null;

		Map<String, Object> response = new HashMap<>();

		try {
			topingresos = kardexservice.topProductosIngresos();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<TopProductosKardex>>(topingresos, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/gastosmeslocal")
	public ResponseEntity<?> gatosMesLocal(){
		
		List<ConsultaGatosMes> gastoslocal = null;

		Map<String, Object> response = new HashMap<>();

		try {
			gastoslocal = ordenservice.gastosMesLocal();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ConsultaGatosMes>>(gastoslocal, HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/gastosmesextranjera")
	public ResponseEntity<?> gatosMesExtranjera(){
		
		List<ConsultaGatosMes> gastoslocal = null;

		Map<String, Object> response = new HashMap<>();

		try {
			gastoslocal = ordenservice.gastosMesExtranjera();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ConsultaGatosMes>>(gastoslocal, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/ordencompra/pendiente")
	public ResponseEntity<?> ordencomprasPendientes(){
		
		List<Ordencompra> ordenespendientes = null;

		Map<String, Object> response = new HashMap<>();

		try {
			ordenespendientes = ordenservice.listarOrdenesPendientes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Ordencompra>>(ordenespendientes, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("	")
	public ResponseEntity<?> ordenprodPendientes(){
		
		List<Ordenprod> ordenprodpendientes = null;

		Map<String, Object> response = new HashMap<>();

		try {
			ordenprodpendientes = ordenprodservice.listarOrdenprodPendientes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Ordenprod>>(ordenprodpendientes, HttpStatus.OK);
	}
	
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" , "ROLE_JEFE", "ROLE_LOGISTICA"})
	@GetMapping("/inventariofisico/pendientes")
	public ResponseEntity<?> inventriosFisicosPendientes(){
		
		List<InventarioFisico> inventarioPendiente = null;

		Map<String, Object> response = new HashMap<>();

		try {
			inventarioPendiente = inventariofisicoservice.listarInventariosPendientes();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<InventarioFisico>>(inventarioPendiente, HttpStatus.OK);
	}
	

}
