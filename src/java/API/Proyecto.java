package API;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import javax.ws.rs.core.Context; 
import javax.ws.rs.core.UriInfo; 
import javax.ws.rs.Produces; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.core.MediaType; 
import javax.ws.rs.QueryParam; 
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution; 
import com.hp.hpl.jena.query.QueryExecutionFactory; 
import com.hp.hpl.jena.query.QueryFactory; 
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.*; 
import com.hp.hpl.jena.util.FileManager; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream;
import java.util.Iterator;
import javax.ws.rs.DefaultValue;

public class Proyecto 
{
    static String NS = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    OntModel modelo = null;
    Model model=null;
    String tituloPDTI;
    String objetivoPDTI;
    String descripcionPDTI;
    String fechaInicio;
    String fechaFin;
    String presupuestoPDTI;
    String queryRequest = "SELECT ?Titulo ?Objetivo ?Descripcion ?FechaInicio ?FechaFin ?Presupuesto ?IdProyecto \n" +
                "WHERE { \n" +
                "?Proyecto_De_TI OntoBLOGP:titulo ?Titulo. \n" +
                "?Proyecto_De_TI OntoBLOGP:objetivo ?Objetivo. \n" +
                "?Proyecto_De_TI OntoBLOGP:descripcionPDTI ?Descripcion. \n" +
                "?Proyecto_De_TI OntoBLOGP:fechaInicio ?FechaInicio. \n" +
                //"?Proyecto_De_TI OntoBLOGP:fechaFin ?FechaFin.}"
                "?Proyecto_De_TI OntoBLOGP:fechaFin ?FechaFin. \n" +
                "?Proyecto_De_TI OntoBLOGP:presupuesto ?Presupuesto. \n" +
                "?Proyecto_De_TI OntoBLOGP:idProyecto ?IdProyecto.} \n" +
                "Orderby ?Titulo";
    
    public Proyecto(String tit, String obj, String des,String fei, String fef,String pre){
        tituloPDTI= tit;
        objetivoPDTI=obj;
        descripcionPDTI= des;
        fechaInicio= fei;
        fechaFin= fef;
        presupuestoPDTI= pre;
    }
    
    public Proyecto(){
        
    }
    
    public String consultarProyectos(Model model){
        this.model= model;
        return ejecutarConsulta(this.model);
    }
    
    public String crearProyectos(String tit, String obj, String des, String fechai, String fechaf, String pre) throws FileNotFoundException{
        cargarOntologia();
        int cont = contarIndividuos();
        OntClass proyectos = modelo.getOntClass(NS+"Proyecto_De_TI");
        DatatypeProperty titulo = modelo.getDatatypeProperty(NS+"titulo");
        DatatypeProperty objetivo = modelo.getDatatypeProperty(NS+"objetivo");
        DatatypeProperty descripcion = modelo.getDatatypeProperty(NS+"descripcionPDTI");
        DatatypeProperty fechainicio = modelo.getDatatypeProperty(NS+"fechaInicio");
        DatatypeProperty fechafin = modelo.getDatatypeProperty(NS+"fechaFin");
        DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"presupuesto");
        DatatypeProperty idproyecto = modelo.getDatatypeProperty(NS+"idProyecto");
        //DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"descripcionPDTI");
        //ObjectProperty objp = modelo.getObjectProperty(NS+"is_Enrrolled");

        Individual nuevoProyecto = modelo.createIndividual(NS+"Proy00"+String.valueOf(cont),proyectos);
        nuevoProyecto.setPropertyValue(titulo, modelo.createTypedLiteral(tit,NS));
        nuevoProyecto.setPropertyValue(objetivo, modelo.createTypedLiteral(obj,NS));
        nuevoProyecto.setPropertyValue(descripcion, modelo.createTypedLiteral(des,NS));
        nuevoProyecto.setPropertyValue(fechainicio, modelo.createTypedLiteral(fechai,NS));
        nuevoProyecto.setPropertyValue(fechafin, modelo.createTypedLiteral(fechaf,NS));
        nuevoProyecto.setPropertyValue(presupuesto, modelo.createTypedLiteral(pre,NS));
        nuevoProyecto.setPropertyValue(idproyecto, modelo.createTypedLiteral("Proy00"+String.valueOf(cont),NS));

        //newEstudiante.setPropertyValue(objp, modelo.getIndividual(NS+"Grp0001"));

        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        
        modelo.write(out, "RDF/XML");
        /*System.out.println("Proyecto agregado "+cont);
        System.out.println("titulo "+tit);
        System.out.println("objetivo "+obj);
        System.out.println("descripcion "+des);
        System.out.println("inicio "+fechai);
        System.out.println("fin "+fechaf);
        */
        return "Proyecto creado";
    }
    
    public String editarProyectos(String id, String tit, String obj, String des, String fechai, String fechaf, String pre) throws IOException{
        cargarOntologia();
        //DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"First_Name");
        DatatypeProperty titulo = modelo.getDatatypeProperty(NS+"titulo");
        DatatypeProperty objetivo = modelo.getDatatypeProperty(NS+"objetivo");
        DatatypeProperty descripcion = modelo.getDatatypeProperty(NS+"descripcionPDTI");
        DatatypeProperty fechainicio = modelo.getDatatypeProperty(NS+"fechaInicio");
        DatatypeProperty fechafin = modelo.getDatatypeProperty(NS+"fechaFin");
        DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"presupuesto");
        //DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"fechaFin");
        
        //Individual newEstudiante = modelo.getIndividual(NS+"Std0014");
        Individual nuevoProyecto = modelo.getIndividual(NS+id);
        //newEstudiante.setPropertyValue(nombre, modelo.createTypedLiteral("Pedro",NS));
        nuevoProyecto.setPropertyValue(titulo, modelo.createTypedLiteral(tit,NS));
        nuevoProyecto.setPropertyValue(objetivo, modelo.createTypedLiteral(obj,NS));
        nuevoProyecto.setPropertyValue(descripcion, modelo.createTypedLiteral(des,NS));
        nuevoProyecto.setPropertyValue(fechainicio, modelo.createTypedLiteral(fechai,NS));
        nuevoProyecto.setPropertyValue(fechafin, modelo.createTypedLiteral(fechaf,NS));
        nuevoProyecto.setPropertyValue(presupuesto, modelo.createTypedLiteral(pre,NS));
        System.out.println("Proyecto editado");
        System.out.println("titulo "+tit);
        System.out.println("objetivo "+obj);
        System.out.println("descripcion "+des);
        System.out.println("inicio "+fechai);
        System.out.println("fin "+fechaf);
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Proyecto editado";
    }
    
    public String eliminarProyectos (String id) throws FileNotFoundException{
        cargarOntologia();
        System.out.println("Eliminando individuo ");System.out.println(" "+id);
        //Individual individuo = modelo.getIndividual(NS+"Std0014");
        Individual individuo = modelo.getIndividual(NS+id);
        individuo.remove();      
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        //System.out.println("\n---- RDF/XML ----");
        modelo.write(out, "RDF/XML");
       //inFoafInstance.close();
       return "Proyecto eliminado";
}
    
    public String ejecutarConsulta(Model model){
        
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        queryStr.append(queryRequest); 
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        String json = "";int conta=0;
        try 
        {
            ResultSet response = qexec.execSelect();
            System.out.println("Starting searchita");
            int cont=0;
            while(response.hasNext())
            {
                conta++;
                QuerySolution soln = response.nextSolution();
                RDFNode titulo = soln.get("?Titulo");
                RDFNode objetivo = soln.get("?Objetivo");
                RDFNode descripcion = soln.get("?Descripcion"); 
                RDFNode fechainicio = soln.get("?FechaInicio");
                RDFNode fechafin = soln.get("?FechaFin");
                RDFNode presupuesto = soln.get("?Presupuesto");
                RDFNode idproyecto = soln.get("?IdProyecto");
                //Individual individuo = soln.getLiteral();
                //modelo.getIndividual(NS+"Proy00"+String.valueOf(cont));
                //cont++;
                
                //System.out.println("Id individuo: "+soln);
                //String idindividuo = "";
                if( (titulo != null) && (objetivo != null) && (descripcion != null) && (fechainicio != null) && (fechafin != null))
                {
                    json += "{\"titulo\":\""+eliminarPrefijos(titulo.toString())  +"\"," + 
                            "\"objetivo\":\""+eliminarPrefijos(objetivo.toString())  +"\"," + 
                            "\"descripcion\":\""+eliminarPrefijos(descripcion.toString())  +"\"," +
                            "\"fechainicio\":\""+eliminarPrefijos(fechainicio.toString())  +"\"," +
                            "\"fechafin\":\""+eliminarPrefijos(fechafin.toString()) +"\"," + 
                            "\"presupuesto\":\""+eliminarPrefijos(presupuesto.toString()) +"\"," +
                            "\"idproyecto\":\""+ eliminarPrefijos(idproyecto.toString()) +"\"}" ;
                            //"\"fechafin\":\""+eliminarPrefijos(fechafin.toString()) +"\"}" ;
                    if (response.hasNext())
                    {
                        json += ",";
                    }
                }
                else
                {
                    System.out.println("No data found!"); 
                }
                        
            }
                
        }
        finally 
        {
            qexec.close(); 
        }
        
        return "[" + json + "]";
    }

    public  int contarIndividuos( ){
       int cont = 0;
       OntClass proyectos = modelo.getOntClass(NS+"Proyecto_De_TI");
       for(Iterator it = proyectos.listInstances(true);it.hasNext();){
           Individual ind = (Individual) it.next();
             //System.out.println("  "+ind.getLocalName()+"  ");
             cont++;
         }
         //System.out.println("");
         //System.out.println("el contador es  " + cont);
         return cont+1;
    }
    
    public void cargarOntologia(){
        modelo = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream inFoafInstance = FileManager.get().open("Onto/OntoBLOGP1.0.owl");
        modelo.read(inFoafInstance,NS);
    }
    
    public String  eliminarPrefijos (String linea){
        linea=linea.replace("^^http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#","");
        return linea;   
    }
    
}
