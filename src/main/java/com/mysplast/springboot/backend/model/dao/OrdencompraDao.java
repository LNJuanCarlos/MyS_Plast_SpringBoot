package com.mysplast.springboot.backend.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mysplast.springboot.backend.model.entity.ConsultaGatosMes;
import com.mysplast.springboot.backend.model.entity.Ordencompra;

public interface OrdencompraDao extends CrudRepository<Ordencompra, String> {
	
	@Query(value="EXEC usp_consultaordencompra @sector = :sector, @almacen = :almacen, @fecha1 = :fecha1, @fecha2 = :fecha2", nativeQuery = true)
	public List<Ordencompra> filtroOrdenCompra(@Param("sector") String sector, @Param("almacen") String almacen, @Param("fecha1")String fecha1,@Param("fecha2")String fecha2);

	@Query(value="EXEC usp_consultagastos @fecha1 = :fecha1, @fecha2 = :fecha2, @moneda = :moneda", nativeQuery = true)
	public List<Ordencompra> filtroGastos(@Param("fecha1")String fecha1,@Param("fecha2")String fecha2,@Param("moneda")String moneda);
	
	@Query("SELECT d FROM Ordencompra d WHERE d.NROORDENCOMPRA = ?1")
	public Ordencompra buscarPorNroOrden(String nroorden);
	
	@Query("SELECT d FROM Ordencompra d WHERE d.ESTADO = 'P'")
	public List<Ordencompra> ordenesPendientes();


	@Query(value="SELECT TOP(50)* FROM ORDENCOMPRA ORDER BY NROORDENCOMPRA DESC", nativeQuery = true)
	public List<Ordencompra> listar50OrdenesDeCompra();
	
	@Query(value="EXEC usp_gastospormeslocal", nativeQuery = true)
	public List<ConsultaGatosMes> gastosMesLocal();
	
	@Query(value="EXEC usp_gastospormesextranjera", nativeQuery = true)
	public List<ConsultaGatosMes> gastosMesExtranjera();
	
	@Query(value="SELECT * FROM ORDENCOMPRA WHERE ESTADO = 'P'", nativeQuery = true)
	public List<Ordencompra> listarOrdenesCompraPendientes();


}
