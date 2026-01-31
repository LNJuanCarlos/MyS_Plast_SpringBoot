package com.mysplast.springboot.backend.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mysplast.springboot.backend.model.entity.InventarioFisico;

public interface InventarioFisicoDao extends CrudRepository<InventarioFisico, String> {
	
	@Query(value="SELECT TOP(50)* FROM INVENTARIOFISICO ORDER BY FECHA DESC", nativeQuery = true)
	public List<InventarioFisico> listarTop50InventarioFisico();
	
	@Query(value="SELECT * FROM INVENTARIOFISICO WHERE ESTADO = 'P'", nativeQuery = true)
	public List<InventarioFisico> inventariosFisicosPendientes();
	
	@Query(value="EXEC usp_consultainventariofisicoxfecha @fecha = :fecha, @SECTOR = :sector", nativeQuery = true)
	public List<InventarioFisico> buscarInventarioFisicoxFecha(@Param("fecha") String fecha, @Param("sector")String sector);
	
	@Query(value="EXEC usp_consultainventariofisico @sector = :sector, @fecha1 = :fecha1, @fecha2 = :fecha2", nativeQuery = true)
	public List<InventarioFisico> filtroInventarioFisico(@Param("sector") String sector, @Param("fecha1")String fecha1,@Param("fecha2")String fecha2);
	

}
