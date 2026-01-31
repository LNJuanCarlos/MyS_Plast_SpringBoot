package com.mysplast.springboot.backend.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysplast.springboot.backend.model.entity.Categoriatransaccion;
import com.mysplast.springboot.backend.model.entity.Egreso;
import com.mysplast.springboot.backend.model.entity.Ingreso;
import com.mysplast.springboot.backend.model.entity.Itemtransaccion;
import com.mysplast.springboot.backend.model.entity.Kardex;
import com.mysplast.springboot.backend.model.entity.Ordenprod;
import com.mysplast.springboot.backend.model.entity.Producto;
import com.mysplast.springboot.backend.model.entity.Recetaprod;
import com.mysplast.springboot.backend.model.entity.Sector;
import com.mysplast.springboot.backend.model.entity.Stock;
import com.mysplast.springboot.backend.model.entity.Tipotransaccion;
import com.mysplast.springboot.backend.model.service.CategoriatransaccionService;
import com.mysplast.springboot.backend.model.service.EgresoService;
import com.mysplast.springboot.backend.model.service.IngresoService;
import com.mysplast.springboot.backend.model.service.KardexService;
import com.mysplast.springboot.backend.model.service.OrdenprodService;
import com.mysplast.springboot.backend.model.service.RecetaprodService;
import com.mysplast.springboot.backend.model.service.StockService;
import com.mysplast.springboot.backend.model.service.Tipotransaccionservice;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/ordenprod")
public class OrdenprodController {

	@Autowired
	private OrdenprodService ordenprodservice;

	@Autowired
	private IngresoService ingresoservice;

	@Autowired
	private EgresoService egresoservice;

	@Autowired
	private RecetaprodService recetaprodservice;

	@Autowired
	private StockService productostockservice;

	@Autowired
	private Tipotransaccionservice tipotransaccionservice;

	@Autowired
	private KardexService kardexservice;

	@Autowired
	private CategoriatransaccionService categoriatransaccionservice;

	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN", "ROLE_JEFE", "ROLE_LOGISTICA" })
	@GetMapping("/listartop")
	public ResponseEntity<?> listarTop50() {

		List<Ordenprod> ordenprods = null;

		Map<String, Object> response = new HashMap<>();

		try {
			ordenprods = ordenprodservice.listarOrdenprod();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Ordenprod>>(ordenprods, HttpStatus.OK);
	}

	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN", "ROLE_JEFE", "ROLE_LOGISTICA" })
	@GetMapping("/buscar/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable String id) {

		Ordenprod ordenprod = null;
		Map<String, Object> response = new HashMap<>();

		try {

			ordenprod = ordenprodservice.buscarOrdenprodXId(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ordenprod == null) {
			response.put("mensaje", "La Orden de Produccción con el ID:".concat(id.toString().concat("no existe!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Ordenprod>(ordenprod, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminarPorId(@PathVariable String id) {

		Map<String, Object> response = new HashMap<>();

		try {
			ordenprodservice.eliminarOrdenprodxID(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminación en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Orden de Produccción ha sido eliminada con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@PostMapping("/ordenprod")
	public ResponseEntity<?> crear(@RequestBody Ordenprod ordenprod) {

		Ordenprod nuevoordenprod = null;

		Map<String, Object> response = new HashMap<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		try {
			ordenprod.setESTADO("P");
			ordenprod.setREG_USER(authentication.getName());
			ordenprod.setFECH_REG_USER(ZonedDateTime.now().toLocalDate().toString());
			nuevoordenprod = ordenprodservice.grabarOrdenprod(ordenprod);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el registro en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Orden de Produccción ha sido registrada con éxito!");
		response.put("ordenprod", nuevoordenprod);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_LOGISTICA" })
	@PutMapping("/estado/{id}")
	public ResponseEntity<?> actualizarEstadoOrdenprod(@RequestBody Ordenprod ordenprod, @PathVariable String id) {
		Ordenprod ordenprodActual = ordenprodservice.buscarOrdenprodXId(id);
		Ordenprod ordenprodActualizada = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();

		if (ordenprodActual == null) {
			response.put("mensaje", "La Orden de Produccción con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("C")) {
			response.put("mensaje", "La Orden de Produccción con el ID:" + id.toString()
					+ "se encuentra confirmada, comuníquese con el encargado de produccción!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("N")) {
			response.put("mensaje", "La Orden de Produccción con el ID:" + id.toString() + "ya está anulada!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			ordenprodActual.setESTADO(ordenprod.getESTADO());
			ordenprodActual.setMOD_USER(authentication.getName());
			ordenprodActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			ordenprodActualizada = ordenprodservice.grabarOrdenprod(ordenprodActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la orden de producción!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Orden de Produccción ha sido actualizada con éxito!");
		response.put("ordenprod", ordenprodActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" })
	@PutMapping("/inventariar/{id}")
	public ResponseEntity<?> inventariarEstadoOrdenprod(@RequestBody Ordenprod ordenprod,
			@PathVariable String id) {
		Ordenprod ordenprodActual = ordenprodservice.buscarOrdenprodXId(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Ordenprod ordenprodActualizada = null;
		Map<String, Object> response = new HashMap<>();

		if (ordenprodActual == null) {
			response.put("mensaje", "La Orden de Producción con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("I")) {
			response.put("mensaje", "La Orden de Producción con el ID:" + id.toString() + "ya se encuentra inventariada!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("N")) {
			response.put("mensaje",
					"La Orden de Producción con el ID:" + id.toString() + "se encuentra anulada, no se puede inventariar!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			
			
			boolean StockNegativo = false;
			boolean StockNulo = false;
			Categoriatransaccion categoriaIngreso = categoriatransaccionservice.buscarCaterogiraTransaccion(Long.parseLong("2"));
			Categoriatransaccion categoriaEgreso = categoriatransaccionservice.buscarCaterogiraTransaccion(Long.parseLong("7"));
			Tipotransaccion nuevotipotransaccion = tipotransaccionservice.buscarTipotransaccionId(Long.parseLong("1"));
			Tipotransaccion nuevotipotransaccionEgreso = tipotransaccionservice.buscarTipotransaccionId(Long.parseLong("2"));
			
			//Creando Nuevo Egreso a Partir de Datos de la Orden de Producción
			Egreso nuevoegreso = new Egreso();	
			nuevoegreso.setId_PERSONA(ordenprod.getId_PERSONA());
			nuevoegreso.setId_SECTOR(ordenprod.getId_SECTORINSUMOS());
			nuevoegreso.setNRO_ORDEN(ordenprod.getNRO_ORDENPROD());
			nuevoegreso.setCategoriatransaccion(categoriaEgreso);
			
			//Agregando Items al Detalle del Egreso a partir de los Insumos de los Items de la Orden de Producción
			List<Itemtransaccion> itemtransaccionAcumuladoEgreso = new ArrayList<Itemtransaccion>();
			for (int i = 0; i < ordenprod.getItems().size(); i++) {
				Recetaprod recetaObtenida = recetaprodservice.buscarRecetaxProducto(ordenprod.getItems().get(i).getId_PRODUCTO().getID_PRODUCTO());
				if (recetaObtenida == null) {
					response.put("mensaje",
							"No se han encontrado recetas para los productos o se encuentran inactivas, por favor verificar!");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
				
				int line = 0;
					for(int x = 0; x < recetaObtenida.getItems().size(); x++) {
						Itemtransaccion itemtransaccionEgreso = new Itemtransaccion();
						itemtransaccionEgreso.setCANTIDAD((ordenprod.getItems().get(i).getCANTIDAD()) * (recetaObtenida.getItems().get(x).getCANTIDAD()));
						itemtransaccionEgreso.setId_PRODUCTO(recetaObtenida.getItems().get(x).getId_PRODUCTO());
						itemtransaccionEgreso.setLINEA(line + 1);
						itemtransaccionAcumuladoEgreso.add(itemtransaccionEgreso);
					}
			}
			
			nuevoegreso.setItems(itemtransaccionAcumuladoEgreso);
			
			List<Itemtransaccion> whtransaccionitemActualEgreso = nuevoegreso.getItems();


			for (int i = 0; i < whtransaccionitemActualEgreso.size(); i++) {

				String sector = nuevoegreso.getId_SECTOR().getID_SECTOR();
				String producto = whtransaccionitemActualEgreso.get(i).getId_PRODUCTO().getID_PRODUCTO();
				Stock productostockactual = productostockservice.buscarPorAlmacen(sector, producto);

				if (productostockactual.equals(null)) {
					StockNulo = true;
					break;
				} else {
					double cantidad = whtransaccionitemActualEgreso.get(i).getCANTIDAD();
					double cantidadactualizada = productostockactual.getCANTIDAD() - cantidad;
					if (cantidadactualizada < 0) {
						StockNegativo = true;
						break;
					}
				}
			}
			
			
			//Creando Nuevo Ingreso a Partir de Datos de la Orden de Producción
			Ingreso nuevoingreso = new Ingreso();	
			nuevoingreso.setId_PERSONA(ordenprod.getId_PERSONA());
			nuevoingreso.setId_SECTOR(ordenprod.getId_SECTOR());
			nuevoingreso.setNRO_ORDEN(ordenprod.getNRO_ORDENPROD());
			nuevoingreso.setCategoriatransaccion(categoriaIngreso);
			
			String Lote = "";
			int día = ZonedDateTime.now().toLocalDate().getDayOfMonth();
			int mes = ZonedDateTime.now().toLocalDate().getMonthValue();
			int año = ZonedDateTime.now().toLocalDate().getYear();
			
			//Agregando Items al Detalle del Ingreso a partir de los Items de la Orden de Producción
			List<Itemtransaccion> itemtransaccionAcumulado = new ArrayList<Itemtransaccion>();
			for (int i = 0; i < ordenprod.getItems().size(); i++) {
			String codigoproductolote = ordenprod.getItems().get(i).getId_PRODUCTO().getCODEXTERNO().substring(0, 4);
			Lote = (codigoproductolote +String.valueOf(día) + String.valueOf(mes) + String.valueOf(año));
			Itemtransaccion itemtransaccion = new Itemtransaccion();
			itemtransaccion.setCANTIDAD(ordenprod.getItems().get(i).getCANTIDAD());
			itemtransaccion.setId_PRODUCTO(ordenprod.getItems().get(i).getId_PRODUCTO());
			itemtransaccion.setLINEA(ordenprod.getItems().get(i).getLINE());
			itemtransaccion.setLOTE(Lote);
			itemtransaccionAcumulado.add(itemtransaccion);
			}
			nuevoingreso.setItems(itemtransaccionAcumulado);
			
			List<Itemtransaccion> whtransaccionitemActual = nuevoingreso.getItems();
				
				
				// Inicio de Grabación de Egreso de Mercadería de Insumos
				
				if (StockNulo == true) {
					response.put("mensaje", "No se cuenta con stock de el/los productos indicados, verificar!");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
				} else {
					if (StockNegativo == true) {
						response.put("mensaje", "El stock del producto no puede quedar en negativo, verificar!");
						return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
					} else {
						
						if(nuevoegreso.getNRO_ORDEN()!="") {
							nuevoegreso.setESTADO("I");
							nuevoegreso.setFECHATRAN(ZonedDateTime.now().toLocalDate().toString());
						} else {
							nuevoegreso.setESTADO("A");
						}
						nuevoegreso.setREG_USER(authentication.getName());
						nuevoegreso.setId_TIPOTRANSACCION(nuevotipotransaccionEgreso);
						nuevoegreso.setFECH_REG_USER(ZonedDateTime.now().toLocalDate().toString());
						egresoservice.grabarEgreso(nuevoegreso);

						for (int i = 0; i < whtransaccionitemActualEgreso.size(); i++) {

							String sectorEgreso = nuevoegreso.getId_SECTOR().getID_SECTOR();
							String productoEgreso = whtransaccionitemActualEgreso.get(i).getId_PRODUCTO().getID_PRODUCTO();
							Producto productoItemEgreso = whtransaccionitemActualEgreso.get(i).getId_PRODUCTO();
							Sector subalmItemEgreso = nuevoegreso.getId_SECTOR();
							Stock productostockactualEgreso = productostockservice.buscarPorAlmacen(sectorEgreso, productoEgreso);
							double cantidadEgreso = whtransaccionitemActualEgreso.get(i).getCANTIDAD();
							double cantidadactualizada = productostockactualEgreso.getCANTIDAD() - cantidadEgreso;
							productostockactualEgreso.setCANTIDAD(cantidadactualizada);
							productostockservice.grabarProductoStock(productostockactualEgreso);
							Kardex kardexactual = kardexservice.buscarPorAlmacen(sectorEgreso, productoEgreso);

							Kardex nuevoKardexEgreso = new Kardex();
							nuevoKardexEgreso.setId_TRAN(nuevoegreso);
							nuevoKardexEgreso.setId_PRODUCTO(productoItemEgreso);
							nuevoKardexEgreso.setId_SECTOR(subalmItemEgreso);
							nuevoKardexEgreso.setFECHA(ZonedDateTime.now().toLocalDate().toString());
							nuevoKardexEgreso.setOPERACION("R");
							nuevoKardexEgreso.setCONDICION("Salida de Mercadería");
							nuevoKardexEgreso.setCANTIDAD(cantidadEgreso);

							if (kardexactual == null) {
								nuevoKardexEgreso.setSTOCKFECHA(cantidadEgreso);

							} else {
								double stockactual = kardexactual.getSTOCKFECHA() - cantidadEgreso;
								nuevoKardexEgreso.setSTOCKFECHA(stockactual);
							}

							kardexservice.grabarKardex(nuevoKardexEgreso);

						}

					}
				}
				
				

				// Inicio de Grabación de Ingreso de Mercadería
				
				if(nuevoingreso.getNRO_ORDEN()!="") {
					nuevoingreso.setESTADO("I");
					nuevoingreso.setFECHATRAN(ZonedDateTime.now().toLocalDate().toString());
				} else {
					nuevoingreso.setESTADO("A");
				}
				nuevoingreso.setREG_USER(authentication.getName());
				nuevoingreso.setId_TIPOTRANSACCION(nuevotipotransaccion);
				nuevoingreso.setFECH_REG_USER(ZonedDateTime.now().toLocalDate().toString());
				ingresoservice.grabarIngreso(nuevoingreso);
				
				for(int i=0;i<whtransaccionitemActual.size();i++) {
					double cantidad = whtransaccionitemActual.get(i).getCANTIDAD();
					String subalmacen = nuevoingreso.getId_SECTOR().getID_SECTOR();
					String producto = whtransaccionitemActual.get(i).getId_PRODUCTO().getID_PRODUCTO();
					Producto productoItem = whtransaccionitemActual.get(i).getId_PRODUCTO();
					Sector subalmItem = nuevoingreso.getId_SECTOR();
					Stock productostockactual = productostockservice.buscarPorAlmacen(subalmacen, producto);
					Kardex kardexactual = kardexservice.buscarPorAlmacen(subalmacen, producto);
					
					if(productostockactual==null) {
						
						Stock nuevoProductoStock = new Stock();
						nuevoProductoStock.setCANTIDAD(cantidad);
						nuevoProductoStock.setId_PRODUCTO(productoItem);
						nuevoProductoStock.setId_SECTOR(subalmItem);
						productostockservice.grabarProductoStock(nuevoProductoStock);
						
					} else {
						
						double cantidadactualizada =  productostockactual.getCANTIDAD() + cantidad;
						productostockactual.setCANTIDAD(cantidadactualizada);
						productostockservice.grabarProductoStock(productostockactual);
					}
					
					Kardex nuevoKardex = new Kardex();
					nuevoKardex.setId_TRAN(nuevoingreso);
					nuevoKardex.setId_PRODUCTO(productoItem);
					nuevoKardex.setId_SECTOR(subalmItem);
					nuevoKardex.setFECHA(ZonedDateTime.now().toLocalDate().toString());
					nuevoKardex.setOPERACION("S");
					nuevoKardex.setCONDICION("Ingreso de Mercadería");
					nuevoKardex.setCANTIDAD(cantidad);
					
					if(kardexactual==null) {
						nuevoKardex.setSTOCKFECHA(cantidad);

					} else {
						double stockactual = kardexactual.getSTOCKFECHA()+cantidad;
						nuevoKardex.setSTOCKFECHA(stockactual);
					}
					
					kardexservice.grabarKardex(nuevoKardex);
				}
				
				//FIN DE INGRESO DE MERCADERÍA
			
			ordenprodActual.setESTADO("I");
			ordenprodActual.setMOD_USER(authentication.getName());
			ordenprodActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			ordenprodActualizada = ordenprodservice.grabarOrdenprod(ordenprodActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la orden de compra!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Orden de Producción ha sido actualizada con éxito!");
		response.put("ordenprod", ordenprodActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN" })
	@PutMapping("/aprobar/{id}")
	public ResponseEntity<?> aprobarEstadoOrdenprod(@RequestBody Ordenprod ordenprod,
			@PathVariable String id) {
		Ordenprod ordenprodActual = ordenprodservice.buscarOrdenprodXId(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Ordenprod ordenprodActualizada = null;
		Map<String, Object> response = new HashMap<>();

		if (ordenprodActual == null) {
			response.put("mensaje", "La Orden de Producción con el ID:" + id.toString() + "no existe!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("A")) {
			response.put("mensaje", "La Orden de Producción con el ID:" + id.toString() + "ya se encuentra aprobada!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (ordenprodActual.getESTADO().equals("N")) {
			response.put("mensaje",
					"La Orden de Producción con el ID:" + id.toString() + "se encuentra anulada, no se puede aprobar!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			ordenprodActual.setESTADO("A");
			ordenprodActual.setMOD_USER(authentication.getName());
			ordenprodActual.setFECH_MOD_USER(ZonedDateTime.now().toLocalDate().toString());
			ordenprodActualizada = ordenprodservice.grabarOrdenprod(ordenprodActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la orden de compra!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Orden de Producción ha sido actualizada con éxito!");
		response.put("ordenprod", ordenprodActualizada);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Secured({ "ROLE_ALMACEN", "ROLE_ADMIN", "ROLE_JEFE", "ROLE_LOGISTICA" })
	@GetMapping("/filtro")
	public ResponseEntity<?> filtroOrdenprods(@RequestParam(value = "subalmacen", required = false) String subalmacen,
			@RequestParam(value = "almacen", required = false) String almacen,
			@RequestParam(value = "fecha1", required = false) String fecha1,
			@RequestParam(value = "fecha2", required = false) String fecha2,
			@RequestParam(value = "estado", required = false) String estado) {

		List<Ordenprod> filtroordenprods = null;

		Map<String, Object> response = new HashMap<>();

		if (subalmacen.equals("") && almacen.equals("") && fecha1.equals("") && fecha2.equals("")
				&& estado.equals("")) {
			response.put("mensaje", "Tiene que ingresar al menos un dato!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (fecha1 != "" && fecha2.equals("") || fecha1.equals("") && fecha2 != "") {
			response.put("mensaje", "Si va a filtrar por fechas debe escoger un rango de fechas!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		if (subalmacen.equals("")) {
			subalmacen = null;
		}
		if (almacen.equals("")) {
			almacen = null;
		}
		if (fecha1.equals("")) {
			fecha1 = null;
		}
		if (fecha2.equals("")) {
			fecha2 = null;
		}

		if (estado.equals("")) {
			estado = null;
		}

		try {
			filtroordenprods = ordenprodservice.filtroOrdenprod(subalmacen, almacen, fecha1, fecha2, estado);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Ordenprod>>(filtroordenprods, HttpStatus.OK);

	}

}
