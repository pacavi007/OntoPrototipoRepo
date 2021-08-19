package Conceptos;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
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


public class Riesgo 
{
    static String NS = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    OntModel modelo = null;
    Model model=null;
    
    public Riesgo(){
        
    }
    
    public String consultarRiesgos(Model model){
        this.model= model;
        return ejecutarConsulta(this.model);
    }
    
    public String crearRiesgos(String nom, String des, String otroscon) throws FileNotFoundException{
        cargarOntologia();
        int cont = contarIndividuos();
        OntClass incidentes = modelo.getOntClass(NS+"Riesgo");
        DatatypeProperty nombreRiesgo = modelo.getDatatypeProperty(NS+"nombreRiesgo");
        DatatypeProperty descripcionRiesgo = modelo.getDatatypeProperty(NS+"descripcionRiesgo");
        DatatypeProperty idRiesgo = modelo.getDatatypeProperty(NS+"idRiesgo");
        String[] proyectos = otroscon.split(",");
        Individual nuevoRiesgo = modelo.createIndividual(NS+"Ries00"+String.valueOf(cont),incidentes);
        nuevoRiesgo.setPropertyValue(nombreRiesgo, modelo.createTypedLiteral(nom,NS));
        nuevoRiesgo.setPropertyValue(descripcionRiesgo, modelo.createTypedLiteral(des,NS));
        nuevoRiesgo.setPropertyValue(idRiesgo, modelo.createTypedLiteral("Ries00"+String.valueOf(cont),NS));
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    String[] auxpro = proyectos[i].split("Proy");
                    String auxinc = (auxpro[1].split(":")[0]);
                    Individual incer = modelo.getIndividual(NS + "Ince" + auxinc);
                    ObjectProperty objp = modelo.getObjectProperty(NS + "Inc_puede_ser_RP");
                    incer.addProperty(objp, nuevoRiesgo);
                }
            }
        }
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Riesgo creado";
    }
    
    public String editarRiesgos(String id, String nom, String des, String otroscon) throws IOException{
        cargarOntologia();
        DatatypeProperty nombreRiesgo = modelo.getDatatypeProperty(NS+"nombreRiesgo");
        DatatypeProperty descripcionRiesgo = modelo.getDatatypeProperty(NS+"descripcionRiesgo");
        String[] proyectos = otroscon.split(",");
        Individual nuevoRiesgo = modelo.getIndividual(NS+id);
        nuevoRiesgo.setPropertyValue(nombreRiesgo, modelo.createTypedLiteral(nom,NS));
        nuevoRiesgo.setPropertyValue(descripcionRiesgo, modelo.createTypedLiteral(des,NS));
        ObjectProperty prop1 = modelo.getObjectProperty(NS+"Inc_puede_ser_RP");
        nuevoRiesgo.removeAll(prop1);
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                String[] auxpro = proyectos[i].split("Proy");
                String auxinc = (auxpro[1].split(":")[0]);
                Individual incer = modelo.getIndividual(NS + "Ince" + auxinc);                
                ObjectProperty objp = modelo.getObjectProperty(NS + "Inc_puede_ser_RP");
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    incer.addProperty(objp, modelo.getIndividual(NS + id));
                } else {
                    Property predicate = modelo.getObjectProperty(NS + "Inc_puede_ser_RP");
                    RDFNode rdf = modelo.getIndividual(NS + id);
                    modelo.remove(incer, predicate, rdf);
                }
            }
        }
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Riesgo editado";
    }
    
    public String eliminarRiesgos (String id) throws FileNotFoundException{
        cargarOntologia();
        Individual individuo = modelo.getIndividual(NS+id);
        individuo.remove();      
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Riesgo eliminado";
    }
    
    public String ejecutarConsulta(Model model){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf"  + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#"       + "> ");
        queryStr.append("PREFIX xsd"  + ": <" + "http://www.w3.org/2001/XMLSchema#"          + "> ");
        String queryRequesteaux="SELECT ?NombreRiesgo ?descriRiesgo ?idRiesgo\n" +
                                "WHERE \n" +
                                "{\n" +
                                "	?Riesgo OntoBLOGP:nombreRiesgo ?NombreRiesgo.\n" +
                                "       ?Riesgo OntoBLOGP:descripcionRiesgo ?descriRiesgo.\n" +
                                "       ?Riesgo OntoBLOGP:idRiesgo ?idRiesgo.\n" +
                                "}\n" +
                                "Orderby ?NombreRiesgo";
        queryStr.append(queryRequesteaux);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        String json = "";
        try 
        {
            ResultSet response  = qexec.execSelect();
            boolean flag= response.hasNext();
            while(response.hasNext())
            {
                QuerySolution soln = response.nextSolution();
                RDFNode NombreRiesgo = soln.get("?NombreRiesgo");
                RDFNode descripcionR = soln.get("?descriRiesgo");
                RDFNode idRiesgo = soln.get("?idRiesgo");
                if((NombreRiesgo!=null) && (idRiesgo!=null))
                {
                    json += "{\"Riesgo\":\""+eliminarPrefijos(NombreRiesgo.toString()) + "\"," +
                            "\"descripcionR\":\""+eliminarPrefijos(descripcionR.toString()) + "\"," +
                            "\"proyectos\":\""+mostrarProyectosPorRiesgo(eliminarPrefijos(idRiesgo.toString())) + "\"," +
                            "\"idRiesgo\":\""+eliminarPrefijos(idRiesgo.toString()) + "\"}";
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
            if(!flag)
            {
                json= "Riesgo:SIN RIESGOS";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        return "[" + json + "]";
    }
    
    public String mostrarProyectosPorRiesgo(String idriesgo){
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?proyecto \n" +
                                "WHERE \n" +
                                "{ \n" +
                                "   ?Incertidumbre OntoBLOGP:Inc_puede_ser_RP OntoBLOGP:"+idriesgo+".\n" +
                                "   ?Proyecto_De_TI OntoBLOGP:PDTI_presenta_Inc ?Incertidumbre.\n" +
                                "   ?Proyecto_De_TI OntoBLOGP:titulo ?proyecto.\n" +
                                "}\n" +
                                "Orderby asc (?proyecto)";
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        queryStr.append(queryRequesteaux); 
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        String json = "";
        try 
        {
            ResultSet response = qexec.execSelect();
            boolean flag= response.hasNext();
            while(response.hasNext())
            {
                QuerySolution soln = response.nextSolution();
                RDFNode proyecto = soln.get("?proyecto");
                if(proyecto != null)
                {
                    json += "Proyecto:"+eliminarPrefijos(proyecto.toString());
                    if (response.hasNext())
                    {
                        json += ",,";
                    }
                }
                else
                {
                    System.out.println("No data found!"); 
                }          
            }
            if(!flag)
            {
                json= "Proyecto:RIESGO NO ASIGNADO A PROYECTO";
            }      
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public String mostrarProyectos(Model modelo){
        model = modelo;
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf"  + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#"       + "> ");
        queryStr.append("PREFIX xsd"  + ": <" + "http://www.w3.org/2001/XMLSchema#"          + "> ");
        String queryRequesteaux="SELECT ?proyecto ?idpro \n" +
                                "WHERE \n" +
                                "{ \n" +
                                "   ?Proyecto_De_TI OntoBLOGP:titulo ?proyecto.\n" +
                                "   ?Proyecto_De_TI OntoBLOGP:idProyecto ?idpro.\n" +
                                "} \n" +
                                "Orderby asc (?proyecto)";
        queryStr.append(queryRequesteaux);
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        String json = "";
        try 
        {
            ResultSet response  = qexec.execSelect();
            boolean flag= response.hasNext();
            while(response.hasNext())
            {
                QuerySolution soln = response.nextSolution();
                RDFNode proyecto = soln.get("?proyecto");
                RDFNode idpro = soln.get("?idpro");
                if((proyecto!=null) && (idpro!=null))
                {
                    json += "Proyecto:"+eliminarPrefijos(proyecto.toString())+"&&";
                    json += "idProyecto:"+eliminarPrefijos(idpro.toString());
                    if (response.hasNext())
                    {
                        json += ",,";
                    }
                }
                else
                {
                    System.out.println("No data found!"); 
                }   
            }  
            if(!flag)
            {
                json= "Proyecto:SIN PROYECTOS";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public int contarIndividuos(){
        int cont = 0;
        OntClass supuestos = modelo.getOntClass(NS+"Riesgo");
        for(Iterator it = supuestos.listInstances(true);it.hasNext();){
            Individual ind = (Individual) it.next();
            cont++;
        }
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
