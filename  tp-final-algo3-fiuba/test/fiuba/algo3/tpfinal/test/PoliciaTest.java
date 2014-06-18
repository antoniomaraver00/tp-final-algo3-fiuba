package fiuba.algo3.tpfinal.test;

import modelo.Policia;
import modelo.RangoDetective;
import modelo.RangoInvestigador;
import modelo.RangoNovato;
import modelo.RangoSargento;
import modelo.Tiempo;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
public class PoliciaTest {
	private String nombreArchivo;
	@Before
	public void before() {
		this.nombreArchivo = java.util.UUID.randomUUID().toString() + ".xml";
	}
	
	@After
	public void after() {
		File archivo = new File(this.nombreArchivo);
		if (archivo.exists()) {
			archivo.delete();
		}
	}

	
	public void SumarArrestos(int Cantidad,Policia Jugador){
		for (int i = 0; i<Cantidad; i++){
			Jugador.AddArresto();
		}
	}

	@Test
	public void PoliciaNuevoEsNovato() {
		
		Policia Jugador = new Policia("Pepe",0);
		Assert.assertTrue(Jugador.getRango() instanceof RangoNovato);
	}
	
	@Test
	public void PoliciaCambiadeRangoAlSumarArrestos(){
		Policia Jugador = new Policia("Pepe",0);
		
		this.SumarArrestos(5,Jugador);
		Assert.assertTrue(Jugador.getRango() instanceof RangoDetective);

		this.SumarArrestos(5,Jugador);
		Assert.assertTrue(Jugador.getRango() instanceof RangoInvestigador);
		
		this.SumarArrestos(10,Jugador);
		Assert.assertTrue(Jugador.getRango() instanceof RangoSargento);
	}
	
	@Test
	public void PoliciaDormir(){
		Policia UnPolicia = new Policia("Pedraza",0);
		Tiempo UnTiempo = new Tiempo(200);
		UnPolicia.setTiempo(UnTiempo);
		int TiempoAntesDeDormir = UnTiempo.Horas();
		int HorasDormir = 8;
		
		UnPolicia.Dormir();
		
		Assert.assertEquals(TiempoAntesDeDormir - HorasDormir , UnTiempo.Horas());
	
	}
	
	@Test
	public void PoliciaNoCambiadeRangoConCuatroArrestos(){
		Policia Jugador = new Policia("Pepe",0);
		this.SumarArrestos(4,Jugador);
		Assert.assertFalse(Jugador.getRango() instanceof RangoDetective);
	}
	
	@Test 
	public void PoliciaVijaMasRapidoSiCambiaDeRango(){
		Policia Jugador = new Policia("Pepe",0);
		//Novato Viaja a 900 Km/h
		int KilometrosAViajar = 5000;
		
		int HorasDeViajeEsperadasNovato = 5000/900;
		int HorasDeViajeNovato = Jugador.CostoDeViaje(KilometrosAViajar);
		Assert.assertEquals(HorasDeViajeNovato, HorasDeViajeEsperadasNovato);
		
		//RangoDetective viaja a 1100
		this.SumarArrestos(5,Jugador);
		int HorasDeViajeEsperadasDetective = 5000/1100;
		int HorasDeViajeDetective = Jugador.CostoDeViaje(KilometrosAViajar);
		Assert.assertTrue(HorasDeViajeDetective < HorasDeViajeNovato);
		Assert.assertEquals(HorasDeViajeDetective, HorasDeViajeEsperadasDetective);
	}
	@Test
	public void PersistenciaFuncionaCon2Policias() throws ParserConfigurationException, TransformerException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		
		Policia unPolicia = new Policia("Juan",15);
		Policia otroPolicia = new Policia("Pepe",35);
		Element otroPoliciaSerializado = otroPolicia.Serializar(doc);
		Element policiaSerializado = unPolicia.Serializar(doc);
		Assert.assertNotNull(policiaSerializado);
		doc.appendChild(policiaSerializado);
		doc.getFirstChild().appendChild(otroPoliciaSerializado);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		File archivoDestino = new File(this.nombreArchivo);
		StreamResult result = new StreamResult(archivoDestino);
		transformer.transform(source, result);
		
		File archivo = new File(this.nombreArchivo);
		Assert.assertTrue(archivo.exists());


		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(archivo);
		doc.getDocumentElement().normalize();
		
		Policia policiaCargado = Policia.Hidratar(doc,1);
		Assert.assertNotNull(policiaCargado);
		Assert.assertEquals(policiaCargado.getNombre(),otroPolicia.getNombre());
		Assert.assertTrue(policiaCargado.getRango() instanceof RangoSargento);
		Policia otroPoliciaCargado = Policia.Hidratar(doc,0);
		Assert.assertNotNull(otroPoliciaCargado);
		Assert.assertEquals(otroPoliciaCargado.getNombre(),unPolicia.getNombre());
		Assert.assertTrue(otroPoliciaCargado.getRango() instanceof RangoInvestigador);
	}

	
}
