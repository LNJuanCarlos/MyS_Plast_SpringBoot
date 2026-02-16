package com.mysplast.springboot.backend.dto;

import java.util.Date;

public interface KardexConsultaDTO {

    Date getFecha();
    String getDocumento();
    String getSector();
    String getProducto();
    String getTipo_Transaccion();
    String getCondicion();
    Double getIngreso();
    Double getSalida();
    Double getStock();
}
