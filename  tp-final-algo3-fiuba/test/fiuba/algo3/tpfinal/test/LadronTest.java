package fiuba.algo3.tpfinal.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import modelo.Caracteristicas;
import modelo.Ladron;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LadronTest {
	private String nombreArchivo = "pruebaLadron.xml";
	private String[] Sexo = {"Masculino","Femenino"};
	private String[] Hobbie = {"Tennis","Musica","Alpinismo","Paracaidismo","Natacion","Croquet"};
	private String[] Cabello = {"Castanio","Rubio","Rojo","Negro"};
	private String[] Senia = {"Cojera","Anillo","Tatuaje","Cicatriz","Joyas"};
	private String[] Vehiculo = {"Descapotable","Limusina","Deportivo","Moto"};
	
	@After
	public void after() {
		File archivo = new File(this.nombreArchivo);
		if (archivo.exists()) {
			archivo.delete();
		}
	}
	
	@Test
	public void LadronNombreEsCorrecto() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertEquals(ladron.getNombre(),"Menem");	
	}
	
	@Test
	public void LadronNombreNoEsCorrecto() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertNotEquals(ladron.getNombre(),"Carlitos Menem");	
	}
	
	@Test
	public void LadronCaracteristicasSonCorrectas() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertEquals(ladron.Sexo(),Sexo[1]);
		Assert.assertEquals(ladron.Hobby(), Hobbie[0]);
		Assert.assertEquals(ladron.Cabello(),Cabello[3]);
		Assert.assertEquals(ladron.Senia(),Senia[1]);
		Assert.assertEquals(ladron.Vehiculo(),Vehiculo[0]);
	}

	@Test
	public void LadronCaracteristicasNoSonCorrectas() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertNotEquals(ladron.Sexo(),Sexo[0]);
		Assert.assertNotEquals(ladron.Hobby(), Hobbie[1]);
		Assert.assertNotEquals(ladron.Cabello(),Cabello[0]);
		Assert.assertNotEquals(ladron.Senia(),Senia[2]);
		Assert.assertNotEquals(ladron.Vehiculo(),Vehiculo[1]);
	}
	
	@Test
	public void LadronCompararCaracteristicasEsTrue() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertTrue(ladron.CompararCaracteristicas(CaracteristicasDelLadron));
	}
	
	@Test
	public void LadronCompararCaracteristicasEsFalse() {
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Caracteristicas OtrasCaracteristicasDelLadron = new Caracteristicas(Sexo[0],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		Assert.assertFalse(ladron.CompararCaracteristicas(OtrasCaracteristicasDelLadron));
	}
 	
	@Test
	public void TestOrdenDeArresto(){
		Caracteristicas CaracteristicasDelLadron = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Ladron ladron = new Ladron("Menem",CaracteristicasDelLadron);
		
		Assert.assertFalse(ladron.TieneOrdenDeArresto());
		
		ladron.EmitirOrdenDeArresto();
		
		Assert.assertTrue(ladron.TieneOrdenDeArresto());
	}
	
	
	
	
	@Test
	public void PersistenciaFuncionaCon2Ladrones() throws ParserConfigurationException, TransformerException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Caracteristicas caracteristicasLadronUno = new Caracteristicas(Sexo[1],Hobbie[0],Cabello[3],Senia[1],Vehiculo[0]);
		Caracteristicas caracteristicasLadronDos = new Caracteristicas (Sexo[0],Hobbie[2],Cabello[1],Senia[3],Vehiculo[2]);
		Ladron unLadron = new Ladron("Tomy",caracteristicasLadronUno);
		Ladron otroLadron = new Ladron("Facu",caracteristicasLadronDos);
		Element LadronSerializado = unLadron.Serializar(doc);
		Element otroLadronSerializado = otroLadron.Serializar(doc);
		Assert.assertNotNull(LadronSerializado);
		doc.appendChild(LadronSerializado);
		doc.getFirstChild().appendChild(otroLadronSerializado);
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
		NodeList nodosLadron = doc.getElementsByTagName("Ladron");
		List <Ladron> listado= new ArrayList<Ladron>();
		for  (int i=0;i<nodosLadron.getLength();i++){
			Node nodoHijo = nodosLadron.item(i);
			Ladron ladronCargado = Ladron.Hidratar(nodoHijo);
			listado.add(ladronCargado);
		}
		
		Assert.assertNotNull(listado.get(0));
		Assert.assertEquals(listado.get(0).getNombre(),unLadron.getNombre());
		Assert.assertTrue(listado.get(0).CompararCaracteristicas(caracteristicasLadronUno) );
	
		Assert.assertNotNull(listado.get(1));
		Assert.assertEquals(listado.get(1).getNombre(),otroLadron.getNombre());
		Assert.assertTrue(listado.get(1).CompararCaracteristicas(caracteristicasLadronDos) );
	}

}
