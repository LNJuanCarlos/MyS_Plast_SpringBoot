package com.mysplast.springboot.backend.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysplast.springboot.backend.model.dao.ItemtransaccionDao;
import com.mysplast.springboot.backend.model.entity.ConsultaLotes;
import com.mysplast.springboot.backend.model.entity.Itemtransaccion;

@Service
@Transactional
public class ItemtransaccionService {
	
	@Autowired
	private ItemtransaccionDao itemtransaccionrepo;
	
	public Itemtransaccion grabarItemtransaccion(Itemtransaccion itemtransaccion) {
		return itemtransaccionrepo.save(itemtransaccion);
	}
	
	public List<ConsultaLotes> consultarStockLotesxFecha(String fecha1, String fecha2){
		return itemtransaccionrepo.consultarLotesporFechas(fecha1, fecha2);
	}

}
