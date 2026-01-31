package com.mysplast.springboot.backend.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mysplast.springboot.backend.model.entity.ConsultaLotes;
import com.mysplast.springboot.backend.model.entity.Itemtransaccion;

public interface ItemtransaccionDao extends CrudRepository<Itemtransaccion, Long> {
	
	@Query(value = "EXEC USP_ConsultaLotesExistentesxFecha @fecha1 = :fecha1, @fecha2 = :fecha2", nativeQuery = true)
	public List<ConsultaLotes> consultarLotesporFechas(@Param("fecha1") String fecha1, @Param("fecha2") String fecha2);

}
