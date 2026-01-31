package com.mysplast.springboot.backend.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysplast.springboot.backend.model.dao.InventarioFisicoDao;
import com.mysplast.springboot.backend.model.entity.InventarioFisico;

@Service
@Transactional
public class InventarioFisicoService {
	
	@Autowired
	private InventarioFisicoDao inventariofisicorepo;
	
	public List<InventarioFisico> listarInventariosFisicos(){
		return (List<InventarioFisico>) inventariofisicorepo.findAll();
	}
	
	public List<InventarioFisico> listarTop50InventariosFisicos(){
		return inventariofisicorepo.listarTop50InventarioFisico();
	}
	
	public List<InventarioFisico> listarInventariosPendientes(){
		return inventariofisicorepo.inventariosFisicosPendientes();
	}
	
	public List<InventarioFisico> filtrarInventariosFisicos(String sector, String fecha1, String fecha2){
		return inventariofisicorepo.filtroInventarioFisico(sector, fecha1, fecha2);
	}
	
	public InventarioFisico buscarInventarioFisicoxID(String id) {
		return inventariofisicorepo.findById(id).get();
	}
	
	public List<InventarioFisico> buscarInventarioFisicoxFecha (String fecha, String sector) {
		return inventariofisicorepo.buscarInventarioFisicoxFecha(fecha, sector);
	}
	
	public InventarioFisico grabarInventarioFisico(InventarioFisico inventariofisico) {
		return inventariofisicorepo.save(inventariofisico);
	}
	
	public void eliminarInventarioFisicoxID(String id) {
		inventariofisicorepo.deleteById(id);
	}

}
