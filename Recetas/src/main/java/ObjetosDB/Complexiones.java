package ObjetosDB;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="Complexion")
public class Complexiones implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idComplexion")
	private int idComplexion;
	
	@NotNull
	@Size(min=1, max=30)
	@Column(name="complexion")
	private String complexion;
	
	public Complexiones(int id, String complexion){
		setIdComplexion(id);
		setComplexion(complexion);
	}
	public int getIdComplexion() {
		return idComplexion;
	}
	public void setIdComplexion(int idComplexion) {
		this.idComplexion = idComplexion;
	}
	public String getComplexion() {
		return complexion;
	}
	public void setComplexion(String complexion) {
		this.complexion = complexion;
	}
}
