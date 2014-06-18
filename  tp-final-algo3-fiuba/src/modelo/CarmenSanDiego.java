package modelo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public  class CarmenSanDiego {
	private static String nombreArchivoPolicias = "RegistroPolicias.xml";
	private static String nombreArchivoLadrones = "RegistroLadrones.xml";
	public static List<Policia> ListadoPolicias = new ArrayList<Policia>();
	public static List<Ladron> ListadoLadrones = new ArrayList<Ladron>();
	public static int CantidadDePolicias = 0;
	public static int CantidadDeLadrones = 0;
	
	public void LevantarPoliciasDelXML() throws ParserConfigurationException, SAXException, IOException{
		File archivo = new File(nombreArchivoPolicias);
		if (archivo.exists()){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			doc = dBuilder.parse(archivo);
			doc.getDocumentElement().normalize();
			for (int i = 0; i <CantidadDePolicias;i++){
				Policia policia = Policia.Hidratar(doc,i);
				ListadoPolicias.add(policia);
			}
		}
	
	}
	public void LevantarLadronesDelXML() throws ParserConfigurationException, SAXException, IOException{
		File archivo = new File(nombreArchivoLadrones);
		if (archivo.exists()){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			doc = dBuilder.parse(archivo);
			doc.getDocumentElement().normalize();
			for (int i = 0;i < CantidadDeLadrones;i++){
				Ladron ladron = Ladron.ladronHidratar(doc,i);
				ListadoLadrones.add(ladron);
			}
		}
	}
	private Document crearDoc() throws ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		return doc;
	}
	private void TransformarYEscribirADisco(String nombreArchivo, Document doc) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		File archivoDestino = new File(nombreArchivo);
		StreamResult result = new StreamResult(archivoDestino);
		transformer.transform(source, result);
	}
	public void BajarPoliciasAXML() throws ParserConfigurationException, TransformerException{
		Document doc = crearDoc();
		for (Policia policia:ListadoPolicias){
			Element policiaSerializado = policia.Serializar(doc);
			if (doc.hasChildNodes()==false){//Creo la root
				doc.appendChild(policiaSerializado);
			}
			else{//Le mando frutilla a la raiz
			doc.getFirstChild().appendChild(policiaSerializado);
			}
			}
		TransformarYEscribirADisco(nombreArchivoPolicias,doc);
		}
	public void BajarLadronesAXML() throws ParserConfigurationException, TransformerException{
		Document doc = crearDoc();
		for (Ladron ladron:ListadoLadrones){
			Element ladronSerializado = ladron.serializarLadron(doc);
			if (doc.hasChildNodes()==false){
				doc.appendChild(ladronSerializado);
			}
			else{
				doc.getFirstChild().appendChild(ladronSerializado);
			}
		TransformarYEscribirADisco(nombreArchivoLadrones,doc);
		}
	}

	public void AgregarPolicia(Policia unPolicia){
		ListadoPolicias.add(unPolicia);
		CantidadDePolicias++;
	}
	public void AgregarLadron(Ladron unLadron){
		ListadoLadrones.add(unLadron);
		CantidadDeLadrones++;
	}
	public Boolean LadronEstaEnJuego(Ladron unLadron){
		return ListadoLadrones.contains(unLadron);
	}
	public Boolean PoliciaEstaEnJuego(Policia unPolicia){
		return ListadoPolicias.contains(unPolicia);
	}
	private Boolean ArchivoExiste(String nombreArchivo){
		File archivo = new File(nombreArchivo);
		if (archivo.exists()){
			return true;
		}
		return false;
	}
	public Boolean ArchivoPoliciasExiste(){
		return ArchivoExiste(nombreArchivoPolicias);
	}
	public Boolean ArchivoLadronesExiste(){
		return ArchivoExiste(nombreArchivoLadrones);
	}
	
	}
