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


public class Incidente 
{
    static String NS = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    OntModel modelo = null;
    Model model=null;
    
    public Incidente(){
        
    }
    
    public String consultarIncidentes(Model model){
        this.model= model;
        return ejecutarConsulta(this.model);
    }
    
    public String crearIncidentes(String nom, String des, String otroscon) throws FileNotFoundException{
        cargarOntologia();
        int cont = contarIndividuos();
        OntClass incidentes = modelo.getOntClass(NS+"Registro_Incidente");
        DatatypeProperty nombreRI = modelo.getDatatypeProperty(NS+"nombreRI");
        DatatypeProperty descripcionRI = modelo.getDatatypeProperty(NS+"descripcionRI");
        DatatypeProperty idRI = modelo.getDatatypeProperty(NS+"idRI");
        String[] proyectos = otroscon.split(",");
        Individual nuevoIncidente = modelo.createIndividual(NS+"Rein00"+String.valueOf(cont),incidentes);
        nuevoIncidente.setPropertyValue(nombreRI, modelo.createTypedLiteral(nom,NS));
        nuevoIncidente.setPropertyValue(descripcionRI, modelo.createTypedLiteral(des,NS));
        nuevoIncidente.setPropertyValue(idRI, modelo.createTypedLiteral("Rein00"+String.valueOf(cont),NS));
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    String[] auxpro = proyectos[i].split("Proy");
                    String auxinc = (auxpro[1].split(":")[0]);
                    Individual incer = modelo.getIndividual(NS + "Ince" + auxinc);
                    ObjectProperty objp = modelo.getObjectProperty(NS + "Inc_puede_ser_RI");
                    incer.addProperty(objp, nuevoIncidente);
                }
            }
        }
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Incidente creado";
    }
    
    public String editarIncidentes(String id, String nom, String des, String otroscon) throws IOException{
        cargarOntologia();
        DatatypeProperty nombreRI = modelo.getDatatypeProperty(NS+"nombreRI");
        DatatypeProperty descripcionRI = modelo.getDatatypeProperty(NS+"descripcionRI");
        String[] proyectos = otroscon.split(",");
        Individual nuevoIncidente = modelo.getIndividual(NS+id);
        nuevoIncidente.setPropertyValue(nombreRI, modelo.createTypedLiteral(nom,NS));
        nuevoIncidente.setPropertyValue(descripcionRI, modelo.createTypedLiteral(des,NS));
        ObjectProperty prop1 = modelo.getObjectProperty(NS+"Inc_puede_ser_RI");
        nuevoIncidente.removeAll(prop1);
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                String[] auxpro = proyectos[i].split("Proy");
                String auxinc = (auxpro[1].split(":")[0]);
                Individual incer = modelo.getIndividual(NS + "Ince" + auxinc);                
                ObjectProperty objp = modelo.getObjectProperty(NS + "Inc_puede_ser_RI");
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    incer.addProperty(objp, modelo.getIndividual(NS + id));
                } else {
                    Property predicate = modelo.getObjectProperty(NS + "Inc_puede_ser_RI");
                    RDFNode rdf = modelo.getIndividual(NS + id);
                    modelo.remove(incer, predicate, rdf);
                }
            }
        }
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Incidente editado";
    }
    
    public String eliminarIncidentes (String id) throws FileNotFoundException{
        cargarOntologia();
        Individual individuo = modelo.getIndividual(NS+id);
        individuo.remove();      
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Incidente eliminado";
    }
    
    public String ejecutarConsulta(Model model){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf"  + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#"       + "> ");
        queryStr.append("PREFIX xsd"  + ": <" + "http://www.w3.org/2001/XMLSchema#"          + "> ");
        String queryRequesteaux="SELECT ?NombreIncidente ?descriInc ?idIncidente\n" +
                                "WHERE \n" +
                                "{\n" +
                                "	?Registro_Incidente OntoBLOGP:nombreRI ?NombreIncidente.\n" +
                                "       ?Registro_Incidente OntoBLOGP:descripcionRI ?descriInc.\n" +
                                "       ?Registro_Incidente OntoBLOGP:idRI ?idIncidente.\n" +
                                "}\n" +
                                "Orderby ?NombreIncidente";
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
                RDFNode NombreIncidente = soln.get("?NombreIncidente");
                RDFNode descripcionI = soln.get("?descriInc");
                RDFNode idIncidente = soln.get("?idIncidente");
                if((NombreIncidente!=null) && (idIncidente!=null))
                {
                    json += "{\"Incidente\":\""+eliminarPrefijos(NombreIncidente.toString()) + "\"," +
                            "\"descripcionI\":\""+eliminarPrefijos(descripcionI.toString()) + "\"," +
                            "\"proyectos\":\""+mostrarProyectosPorIncidente(eliminarPrefijos(idIncidente.toString())) + "\"," +
                            "\"idIncidente\":\""+eliminarPrefijos(idIncidente.toString()) + "\"}";
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
                json= "Incidente:SIN INCIDENTES";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        return "[" + json + "]";
    }
    
    public String mostrarProyectosPorIncidente(String idincidente){
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?proyecto \n" +
                                "WHERE \n" +
                                "{ \n" +
                                "   ?Incertidumbre OntoBLOGP:Inc_puede_ser_RI OntoBLOGP:"+idincidente+".\n" +
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
                json= "Proyecto:INCIDENTE NO ASIGNADO A PROYECTO";
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
        OntClass supuestos = modelo.getOntClass(NS+"Registro_Incidente");
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
