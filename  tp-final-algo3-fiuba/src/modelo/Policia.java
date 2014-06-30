package modelo;

import modelo.rangos.Rango;
import modelo.rangos.RangoNovato;



import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Policia  {
	
	
	private String Nombre;
	private Rango Rango = new RangoNovato();
	private int CantidadDeArrestos;
	private Tiempo Tiempo;
	private Pais PaisActual;
	private int HorasSinDormir = 0;
	private Ladron Sospechoso;

	//Constantes
	private final int TiempoLimiteEnHoras = 154;
	private final int HorasADormir = 8;
	private final int LimiteHorasDespierto = 15;
	private final int HorasPorFiltracion = 3;
	private final int HorasHeridaCuchillo = 2;
	private final int HorasHeridaArma = 4;


	public Policia(String Nombre, int CantidadDeArrestos){
		this.Nombre = Nombre;
		this.CantidadDeArrestos = CantidadDeArrestos;
		this.Sospechoso = null;
		this.Rango = new RangoNovato();
		setTiempo();
		chequeoDeRango();
		
		
	}

	public String getNombre(){
		return this.Nombre;
	}
	public Rango getRango() {
		return this.Rango;
	}
	
	private void setTiempo(){
		this.Tiempo = new Tiempo(this.TiempoLimiteEnHoras);
	}
	public void setSospechoso(Ladron UnLadron){
		Sospechoso = UnLadron;
	}
	
	public void setPaisActual(Pais pais){
		this.PaisActual = pais;
	}
	
	public Pais getPais(){
		return this.PaisActual;
	}

	public void addArresto() {
		CantidadDeArrestos++;
		chequeoDeRango();
		
	}
	
	public boolean arrestarSospechoso(){
		if( Sospechoso == null ) return false;
		
		Sospechoso.arrestar();
		return true;
	}
	
	private void chequeoDeRango(){
		this.Rango = this.Rango.chequeoDeRango(CantidadDeArrestos);
	}
	public int costoDeViaje(int kilometrosAViajar) {
		return this.Rango.costoDeViaje(kilometrosAViajar);
	}

	private void reducirHoras(int horas){
		Tiempo.reducirHoras(horas);
		HorasSinDormir += horas;
		if (HorasSinDormir > LimiteHorasDespierto){
			dormir();
			HorasSinDormir = 0;
		}
	}
	
	private void dormir(){
		this.Tiempo.reducirHoras(HorasADormir);
	}
	
	public void reducirHorasPorFiltracion() {
		reducirHoras(HorasPorFiltracion);	
	}
	
	public void reducirHorasPorViaje(int CantidadHoras){
		reducirHoras(CantidadHoras);
	}
	
	public void reducirHorasalVisitar(int VecesVisitado){
		if (VecesVisitado == 1) reducirHoras(1);
		else if (VecesVisitado == 2) reducirHoras(2);
		else reducirHoras(3);
	}
	
	public void reducirHorasPorHeridaCuchillo(){
		reducirHoras(HorasHeridaCuchillo);
	}
	
	public void reducirHorasPorHeridaArmaDeFuego(){
		reducirHoras(HorasHeridaArma);
	}
	
	public boolean tiempoAgotado(){
		return Tiempo.tiempoAgotado();
	}
	public Element serializar(Document doc){
		Element elementoPolicia =doc.createElement("Policia");
		elementoPolicia.setAttribute("Nombre",this.Nombre);
		elementoPolicia.setAttribute("Arrestos",""+this.CantidadDeArrestos);
		return elementoPolicia;
	}
		
	public static Policia hidratar(Node nodo){
		//Como se va a guardar en orden acorde a la posicion dada
		//Le paso por parametro la posicion, para poder hidratar el correcto.
		Element elementoPolicia = (Element)nodo;
		Policia nuevoPolicia = new Policia(elementoPolicia.getAttribute("Nombre"),Integer.parseInt(elementoPolicia.getAttribute("Arrestos")));
		return nuevoPolicia;
		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Policia other = (Policia) obj;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		return true;
	}

	public int getTiempo() {
		return Tiempo.getHoras();
	}

	public String toStringRango() {
		return this.Rango.toString();
	}
	
}
