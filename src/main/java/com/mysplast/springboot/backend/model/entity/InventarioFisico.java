package com.mysplast.springboot.backend.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mysplast.springboot.backend.generator.StringPrefixedSequenceIdGenerator;

@Entity
@Table(name="INVENTARIOFISICO")
public class InventarioFisico implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator="InventarioSEQ"
			)
	@GenericGenerator(
			name = "InventarioSEQ",
			strategy = "com.mysplast.springboot.backend.generator.StringPrefixedSequenceIdGenerator",
			parameters = {
					@Parameter(name= StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
					@Parameter(name= StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "INVTR"),
					@Parameter(name= StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%010d")
				}
			)
	@Column(name="ID_INVENT", length=15, nullable=false)
	private String ID_INVENT;
	
	@Column(name="FECHA", nullable=false)
	@DateTimeFormat(pattern="YYYY-MM-dd")
	private String FECHA;
	
	@Column(name="ESTADO", length = 1, nullable = true)
	private String ESTADO;
	
	@Column(name="NROINVENTARIO", length=20, nullable=true)
	private String NROINVENTARIO;
	
	@Column(name="REG_USER", length=25, nullable=true)
	private String REG_USER;
	
	@Column(name="FECH_REG_USER", nullable=true)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private String FECH_REG_USER;
	
	@Column(name="MOD_USER", length=25, nullable=true)
	private String MOD_USER;
	
	@Column(name="FECH_MOD_USER", nullable=true)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private String FECH_MOD_USER;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RESPONSABLE", referencedColumnName ="ID_PERSONA")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Natural responsable;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_SECTOR", referencedColumnName ="ID_SECTOR")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Sector id_SECTOR;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="ID_INVENT")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private List<ItemInventarioFisico> items;

	public List<ItemInventarioFisico> getItems() {
		return items;
	}

	public void setItems(List<ItemInventarioFisico> items) {
		this.items = items;
	}

	public String getID_INVENT() {
		return ID_INVENT;
	}

	public void setID_INVENT(String iD_INVENT) {
		ID_INVENT = iD_INVENT;
	}

	public String getFECHA() {
		return FECHA;
	}

	public void setFECHA(String fECHA) {
		FECHA = fECHA;
	}

	public String getREG_USER() {
		return REG_USER;
	}

	public void setREG_USER(String rEG_USER) {
		REG_USER = rEG_USER;
	}

	public String getFECH_REG_USER() {
		return FECH_REG_USER;
	}

	public void setFECH_REG_USER(String fECH_REG_USER) {
		FECH_REG_USER = fECH_REG_USER;
	}
	
	public String getNROINVENTARIO() {
		return NROINVENTARIO;
	}

	public void setNROINVENTARIO(String nROINVENTARIO) {
		NROINVENTARIO = nROINVENTARIO;
	}

	public Natural getResponsable() {
		return responsable;
	}

	public void setResponsable(Natural responsable) {
		this.responsable = responsable;
	}

	public Sector getId_SECTOR() {
		return id_SECTOR;
	}

	public void setId_SECTOR(Sector id_SECTOR) {
		this.id_SECTOR = id_SECTOR;
	}

	public String getESTADO() {
		return ESTADO;
	}

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

	public String getMOD_USER() {
		return MOD_USER;
	}

	public void setMOD_USER(String mOD_USER) {
		MOD_USER = mOD_USER;
	}

	public String getFECH_MOD_USER() {
		return FECH_MOD_USER;
	}

	public void setFECH_MOD_USER(String fECH_MOD_USER) {
		FECH_MOD_USER = fECH_MOD_USER;
	}
	
}
