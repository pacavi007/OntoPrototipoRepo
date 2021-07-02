package API;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.ObjectPropertyImpl;
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
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.util.FileManager; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.ws.rs.DefaultValue;
import org.apache.jasper.tagplugins.jstl.ForEach;

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
    
    public String crearProyectos(String tit, String obj, String des, String fechai, String fechaf, String pre, String otroscon) throws FileNotFoundException{
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
        //System.out.println("Otroscon "+otroscon);
        //ObjectProperty objp = modelo.getObjectProperty(NS+"is_Enrrolled");
        String[] auxotroscon = otroscon.split(";");
        String[] interesados = auxotroscon[0].split(",");
        String[] cvps        = auxotroscon[1].split(",");
        String[] entregables = auxotroscon[2].split(",");
        
        
        
        //ObjectProperty objp = modelo.getObjectProperty(NS+"Int_pertenece_PDTI");
        
        Individual nuevoProyecto = modelo.createIndividual(NS+"Proy00"+String.valueOf(cont),proyectos);
        nuevoProyecto.setPropertyValue(titulo, modelo.createTypedLiteral(tit,NS));
        nuevoProyecto.setPropertyValue(objetivo, modelo.createTypedLiteral(obj,NS));
        nuevoProyecto.setPropertyValue(descripcion, modelo.createTypedLiteral(des,NS));
        nuevoProyecto.setPropertyValue(fechainicio, modelo.createTypedLiteral(fechai,NS));
        nuevoProyecto.setPropertyValue(fechafin, modelo.createTypedLiteral(fechaf,NS));
        nuevoProyecto.setPropertyValue(presupuesto, modelo.createTypedLiteral(pre,NS));
        nuevoProyecto.setPropertyValue(idproyecto, modelo.createTypedLiteral("Proy00"+String.valueOf(cont),NS));
        for (int i = 0; i < interesados.length; i++) {
            //System.out.println("auxotroscon "+interesados[i]);
          
            if ((interesados[i].split(":"))[1].equals("S")) {
                Individual interesado = modelo.getIndividual(NS + (interesados[i].split(":"))[0]);
                ObjectProperty objp = modelo.getObjectProperty(NS + "Int_pertenece_PDTI");
                interesado.addProperty(objp, modelo.getIndividual(NS + "Proy00" + String.valueOf(cont)));
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (interesados[i].split(":"))[0]));
            }
        }
        for (int i = 0; i < cvps.length; i++) {
            if ((cvps[i].split(":"))[1].equals("S")) {
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_tiene_CVP");
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (cvps[i].split(":"))[0]));
            }
        }
        for (int i = 0; i < entregables.length; i++) {            
            if (((entregables[i].split(":"))[1].equals("S"))) {
                Individual entregable = modelo.getIndividual(NS + (entregables[i].split(":"))[0]);
                ObjectProperty objp = modelo.getObjectProperty(NS + "Ent_pertenece_PDTI");
                entregable.addProperty(objp, modelo.getIndividual(NS + "Proy00" + String.valueOf(cont)));
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_comprende_E");
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (entregables[i].split(":"))[0]));
            }
        }
        
        //nuevoProyecto.setPropertyValue(objp, modelo.getIndividual(NS+"Inte001"));
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
    
    public String editarProyectos(String id, String tit, String obj, String des, String fechai, String fechaf, String pre, String otroscon) throws IOException{
        cargarOntologia();
        //DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"First_Name");
        DatatypeProperty titulo = modelo.getDatatypeProperty(NS+"titulo");
        DatatypeProperty objetivo = modelo.getDatatypeProperty(NS+"objetivo");
        DatatypeProperty descripcion = modelo.getDatatypeProperty(NS+"descripcionPDTI");
        DatatypeProperty fechainicio = modelo.getDatatypeProperty(NS+"fechaInicio");
        DatatypeProperty fechafin = modelo.getDatatypeProperty(NS+"fechaFin");
        DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"presupuesto");
        System.out.println("Otroscon "+otroscon);
        //DatatypeProperty presupuesto = modelo.getDatatypeProperty(NS+"fechaFin");
        String[] auxotroscon = otroscon.split(";");
        String[] interesados = auxotroscon[0].split(",");
        String[] cvps        = auxotroscon[1].split(",");
        String[] entregables = auxotroscon[2].split(",");
        
        //Individual newEstudiante = modelo.getIndividual(NS+"Std0014");
        Individual nuevoProyecto = modelo.getIndividual(NS+id);
        //newEstudiante.setPropertyValue(nombre, modelo.createTypedLiteral("Pedro",NS));
        nuevoProyecto.setPropertyValue(titulo, modelo.createTypedLiteral(tit,NS));
        nuevoProyecto.setPropertyValue(objetivo, modelo.createTypedLiteral(obj,NS));
        nuevoProyecto.setPropertyValue(descripcion, modelo.createTypedLiteral(des,NS));
        nuevoProyecto.setPropertyValue(fechainicio, modelo.createTypedLiteral(fechai,NS));
        nuevoProyecto.setPropertyValue(fechafin, modelo.createTypedLiteral(fechaf,NS));
        nuevoProyecto.setPropertyValue(presupuesto, modelo.createTypedLiteral(pre,NS));
        
        ObjectProperty prop1 = modelo.getObjectProperty(NS+"PDTI_tiene_Int");
        ObjectProperty prop2 = modelo.getObjectProperty(NS+"PDTI_tiene_CVP");
        ObjectProperty prop3 = modelo.getObjectProperty(NS+"PDTI_comprende_E");
        //RDFNode objp33 = ; 
        nuevoProyecto.removeAll(prop1);
        nuevoProyecto.removeAll(prop2);
        nuevoProyecto.removeAll(prop3);
        //ObjectProperty objobj = modelo.getObjectProperty(NS+"Int_petenece_PDTI");
        
        for (int i = 0; i < interesados.length; i++) {
            //System.out.println("auxotroscon "+interesados[i]); 
            Individual interesado = modelo.getIndividual(NS + (interesados[i].split(":"))[0]); //
            ObjectProperty objp = modelo.getObjectProperty(NS + "Int_pertenece_PDTI"); //
            ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
            
            if ((interesados[i].split(":"))[1].equals("S")) {
                //Individual interesado = modelo.getIndividual(NS + (interesados[i].split(":"))[0]); //
                //ObjectProperty objp = modelo.getObjectProperty(NS + "Int_pertenece_PDTI"); //
                //interesado.removeAll(objp);
                interesado.addProperty(objp, modelo.getIndividual(NS+id)); // 
                
                
                //interesado.removeProperty(predicate, rdf);
                //Statement sta = modelo.createStatement(subject, predicate, "Proy003");
                //modelo.remove(subject, predicate, rdf);
                //modelo.remove(sta);
                //ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (interesados[i].split(":"))[0]));
            } 
            else {
                Resource subject = modelo.getIndividual(NS + (interesados[i].split(":"))[0]);
                Property predicate = modelo.getObjectProperty(NS + "Int_pertenece_PDTI");
                RDFNode rdf = modelo.getIndividual(NS + id);
                modelo.remove(subject, predicate, rdf);
            }
        }
        for (int i = 0; i < cvps.length; i++) {
            ObjectProperty objp2 = modelo.getObjectProperty(NS+"PDTI_tiene_CVP");
            if ((cvps[i].split(":"))[1].equals("S")) {
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (cvps[i].split(":"))[0]));
            } 
            else {
                
            }
        }
        for (int i = 0; i < entregables.length; i++) {            
            Individual entregable = modelo.getIndividual(NS+ (entregables[i].split(":"))[0]);
            ObjectProperty objp = modelo.getObjectProperty(NS+"Ent_pertenece_PDTI");
            ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_comprende_E");
            if ((entregables[i].split(":"))[1].equals("S")) {
                entregable.addProperty(objp, modelo.getIndividual(NS + id));
                nuevoProyecto.addProperty(objp2, modelo.getIndividual(NS + (entregables[i].split(":"))[0]));
            } 
            else {
                Resource subject = modelo.getIndividual(NS + (entregables[i].split(":"))[0]);
                Property predicate = modelo.getObjectProperty(NS + "Ent_pertenece_PDTI");
                RDFNode rdf = modelo.getIndividual(NS + id);
                modelo.remove(subject, predicate, rdf);
            }
        }
        /*System.out.println("Proyecto editado");
        System.out.println("titulo "+tit);
        System.out.println("objetivo "+obj);
        System.out.println("descripcion "+des);
        System.out.println("inicio "+fechai);
        System.out.println("fin "+fechaf);*/
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Proyecto editado";
    }
    
    public String eliminarProyectos (String id) throws FileNotFoundException{
        cargarOntologia();
        System.out.println("Eliminando proyecto");System.out.println(" "+id);
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
        String json = "";int conta=0;String interesados="";
        try 
        {
            ResultSet response = qexec.execSelect();
            System.out.println("Starting searchita");
            while(response.hasNext())
            {
                conta++;interesados="";
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
                            "\"interesados\":\""+mostrarInteresadosPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"cvp\":\""+mostrarCVPPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"entregables\":\""+mostrarEntregablesPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"procesos\":\""+mostrarProcesosPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"incidentes\":\""+mostrarIncidentesPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"supuestos\":\""+mostrarSupuestosPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"riesgos\":\""+mostrarRiesgosPorProyecto(eliminarPrefijos(idproyecto.toString()))+"\"," +
                            "\"idproyecto\":\""+ eliminarPrefijos(idproyecto.toString()) +"\"}" ;
                           //interesados= mostrarInteresadosPorProyecto(idproyecto.toString());
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
        //String proyectos= "[" + json + "]";
        //String aux[]= new String[2];
        
        return "[" + json + "]";
    }
    
    public String mostrarTodoPorTodo(Model modelo){
        model = modelo;
        String json="";
        json = json+mostrarInteresados()+";";
        json = json+mostrarCVP()+";";
        json = json+mostrarEntregables()+";";
        json = json+mostrarProcesos()+"";
        return json;
    }
    
    public String mostrarInteresadosPorProyecto(String idproyecto){
        //idproyecto="Proy003";
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?Titulo ?Objetivo ?Descripcion ?FechaInicio ?FechaFin ?Presupuesto ?Nombre ?Telefono\n" +
                                "WHERE \n" +
                                "{\n" +
                                "	?Interesado OntoBLOGP:nombreInteresado ?Nombre.\n" +
                                "    	?Interesado OntoBLOGP:telefono ?Telefono.\n" +
                                "	?Interesado OntoBLOGP:Int_pertenece_PDTI OntoBLOGP:"+idproyecto+" .\n" +
                                "} \n" +
                                "Orderby (?Nombre)";
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
                RDFNode nombre = soln.get("?Nombre");
                RDFNode telefono = soln.get("?Telefono");
                //RDFNode idproyecto = soln.get("?IdProyecto");
                //Individual individuo = soln.getLiteral();
                //modelo.getIndividual(NS+"Proy00"+String.valueOf(cont));
                //cont++;
                
                //System.out.println("Id individuo: "+soln);
                //String idindividuo = "";
                if( (nombre != null) && (telefono != null) && (idproyecto != null))
                {
                    json += "Nombre: "+eliminarPrefijos(nombre.toString())+"&&"+ 
                            "Teléfono: "+eliminarPrefijos(telefono.toString());
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
                json= "Nombre: SIN DEFINIR&&Teléfono: SIN DEFINIR";
            }  
                
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("IDProyecto: "+idproyecto);
        //System.out.println("Datos: "+json);
        return json;
        //return "[" + json+"]";
    }
    
    public String mostrarInteresados(){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        String queryRequesteaux="SELECT ?Nombre ?idinteresado \n" +
                                "WHERE \n" +
                                "{\n" +
                                "?Interesado OntoBLOGP:nombreInteresado ?Nombre.\n" +
                                "?Interesado OntoBLOGP:idInteresado ?idinteresado" +
                                "}" +
                                "Orderby (?Nombre) \n";
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
                RDFNode nombre = soln.get("?Nombre");
                RDFNode idinteresado = soln.get("?idinteresado");
                if(nombre!=null)
                {
                    json += "Nombre:"+eliminarPrefijos(nombre.toString())+"&&";
                    json += "idInteresado:"+eliminarPrefijos(idinteresado.toString());
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
                json= "Nombre:SIN INTERESADOS";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("Datos Interesados: "+json);
        return json;
    }
    
    public String mostrarCVPPorProyecto(String idproyecto){
        //System.out.println("Proyecto ID "+idproyecto);
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?Ciclo_De_Vida \n" +
                                "WHERE \n" +
                                "{\n" +
                                "       ?Ciclo_Vida_Proyecto OntoBLOGP:nombreCicloVidaProyecto ?Ciclo_De_Vida.\n" +
                                "    	#?Proyecto_De_TI OntoBLOGP:PDTI_tiene_CVP ?Ciclo_Vida_Proyecto.\n" +
                                "       OntoBLOGP:"+idproyecto+" OntoBLOGP:PDTI_tiene_CVP ?Ciclo_Vida_Proyecto. \n" +
                                "}\n" ;
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
                RDFNode ciclodevida = soln.get("?Ciclo_De_Vida");
                if(ciclodevida != null) 
                {
                    json += "Ciclo_De_Vida:"+eliminarPrefijos(ciclodevida.toString());  
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
                json= "Ciclo_De_Vida:SIN DEFINIR";
            }     
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("Datos CVP por proyecto: "+json);
        return json;
    }
    
    public String mostrarCVP(){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        String queryRequesteaux="SELECT ?Ciclo_De_Vida ?idcvp \n" +
                                "WHERE\n" +
                                "{\n" +
                                "?Ciclo_Vida_Proyecto OntoBLOGP:nombreCicloVidaProyecto ?Ciclo_De_Vida.\n" +
                                "?Ciclo_Vida_Proyecto OntoBLOGP:idCVP ?idcvp.\n" +
                                "}\n" ;
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
                RDFNode cvp = soln.get("?Ciclo_De_Vida");
                RDFNode idcvp = soln.get("?idcvp");
                if(cvp!=null)
                {
                    json += "CVP:"+eliminarPrefijos(cvp.toString())+"&&";
                    json += "idCVP:"+eliminarPrefijos(idcvp.toString());
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
                json= "SIN CVP";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("Datos CVP por todo: "+json);
        return json;
    }
    
    public String mostrarEntregablesPorProyecto(String idproyecto){
        //System.out.println("línea 353 Proyecto ID "+idproyecto);
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?entregables \n" +
                                "WHERE \n" +
                                "{\n" +
                                "	?Entregable   OntoBLOGP:nombreEntregable  ?entregables. \n" +
                                "	?Entregable   OntoBLOGP:Ent_pertenece_PDTI  OntoBLOGP:"+idproyecto+" \n" +
                                "}" ;
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
                RDFNode entrega = soln.get("?entregables");
                if(entrega != null) 
                {
                    json += "Entregable:"+eliminarPrefijos(entrega.toString());  
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
                json= "Entregable:SIN DEFINIR";
            }     
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public String mostrarEntregables(){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        String queryRequesteaux="SELECT ?Entregables ?identregable\n" +
                                "WHERE\n" +
                                "{\n" +
                                "?Entregable OntoBLOGP:nombreEntregable ?Entregables.\n" +
                                "?Entregable OntoBLOGP:idEntregable ?identregable.\n" +
                                "}";
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
                RDFNode entregable = soln.get("?Entregables");
                RDFNode identregable = soln.get("?identregable");
                if(entregable!=null)
                {
                    json += "Entregable:"+eliminarPrefijos(entregable.toString())+"&&";
                    json += "idEntregable:"+eliminarPrefijos(identregable.toString());
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
                json= "Entregable:SIN ENTREGABLES";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("Datos Entregables: "+json);
        return json;
    }
    
    public String mostrarProcesosPorProyecto(String idproyecto){
        //System.out.println("Proyecto ID "+idproyecto);
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?Nombre_Proceso \n" +
                                "WHERE \n" +
                                "{\n" +
                                "	?Entregable OntoBLOGP:Ent_pertenece_PDTI OntoBLOGP:"+idproyecto+". \n" +
                                "	?Proceso OntoBLOGP:nombreProceso ?Nombre_Proceso. \n" +
                                "	?Entregable OntoBLOGP:Ent_se_genera_Pro ?Proceso. \n" +
                                "}\n" ;
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
                RDFNode nombreproceso = soln.get("?Nombre_Proceso");
                if(nombreproceso != null) 
                {
                    json += "Proceso:"+eliminarPrefijos(nombreproceso.toString());  
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
                json= "Proceso:SIN DEFINIR";
            }     
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public String mostrarProcesos(){
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        String queryRequesteaux="SELECT ?Nombre_Proceso\n" +
                                "WHERE\n" +
                                "{\n" +
                                "?Proceso OntoBLOGP:nombreProceso ?Nombre_Proceso.\n" +
                                "}" ;
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
                RDFNode proceso = soln.get("?Nombre_Proceso");
                if(proceso!=null)
                {
                    json += "Proceso:"+eliminarPrefijos(proceso.toString());
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
                json= "Proceso:SIN PROCESOS";
            }   
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("Datos Procesos: "+json);
        return json;
    }
    
    public String mostrarIncidentesPorProyecto(String idproyecto){
        //System.out.println("Proyecto ID "+idproyecto);
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT   ?registroIncidente ?Proyecto  ?incertidumbre ?nombreincidente \n" +
                                "WHERE \n " +
                                "{\n" +
                                "	# Busca la incertidumbre de un proyecto\n" +
                                "	?Proyecto_De_TI OntoBLOGP:titulo  ?Proyecto.\n" +
                                "	?Incertidumbre OntoBLOGP:PDTI_presenta_Inc ?incertidumbre.\n" +
                                "	?Proyecto_De_TI  OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "	OntoBLOGP:"+idproyecto+" OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "       ?Registro_Incidente OntoBLOGP:nombreRI ?nombreincidente. \n" +
                                "	?Registro_Incidente OntoBLOGP:descripcionRI ?registroIncidente.\n" +
                                "	?incertidumbre OntoBLOGP:Inc_puede_ser_RI ?Registro_Incidente.\n" +
                                "}\n";
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
                RDFNode registroIncidente = soln.get("?registroIncidente");
                RDFNode Proyecto = soln.get("?Proyecto");
                RDFNode incertidumbre = soln.get("?incertidumbre");
                RDFNode nombreincidente = soln.get("?nombreincidente");
                if((registroIncidente != null) && (nombreincidente != null) ) 
                {
                    json += "descripcionRI: "+eliminarPrefijos(registroIncidente.toString());  
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
                json= "descripcionRI:SIN INCIDENTES";
            }     
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public String mostrarSupuestosPorProyecto(String idproyecto){
        //idproyecto="Proy003";
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT   ?Proyecto ?registroSupuesto ?nombreSupuesto\n" +
                                "WHERE \n " +
                                "{\n" +
                                "	# Busca la incertidumbre de un proyecto\n" +
                                "	?Proyecto_De_TI OntoBLOGP:titulo  ?Proyecto.\n" +
                                "	?Incertidumbre OntoBLOGP:PDTI_presenta_Inc ?incertidumbre.\n" +
                                "	?Proyecto_De_TI  OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "	OntoBLOGP:"+idproyecto+" OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "	?Registro_De_Supuesto OntoBLOGP:nombreRDS ?nombreSupuesto.\n" +
                                "	?Registro_De_Supuesto OntoBLOGP:descripcionRDS ?registroSupuesto.\n" +
                                "	?incertidumbre OntoBLOGP:Inc_puede_ser_S ?Registro_De_Supuesto.\n" +
                                "}\n";
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
                RDFNode registroSupuesto = soln.get("?registroSupuesto");
                RDFNode nombreSupuesto = soln.get("?nombreSupuesto");
                //RDFNode idproyecto = soln.get("?IdProyecto");
                //Individual individuo = soln.getLiteral();
                //modelo.getIndividual(NS+"Proy00"+String.valueOf(cont));
                //cont++;
                
                //System.out.println("Id individuo: "+soln);
                //String idindividuo = "";
                if( (registroSupuesto != null) && (nombreSupuesto != null) )
                {
                    json += "descripciónRDS: "+eliminarPrefijos(registroSupuesto.toString());
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
                json= "descripciónRDS:SIN SUPUESTOS";
            }  
                
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("IDProyecto: "+idproyecto);
        System.out.println("DatosSupuestos: "+json);
        return json;
        //return "[" + json+"]";
    }
        
    public String mostrarRiesgosPorProyecto(String idproyecto){
        //idproyecto="Proy003";
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT   ?Proyecto ?registroRiesgo ?nombreRiesgo\n" +
                                "WHERE \n" +
                                "{\n" +
                                "	# Busca la incertidumbre de un proyecto\n" +
                                "	?Proyecto_De_TI OntoBLOGP:titulo  ?Proyecto.\n" +
                                "	?Incertidumbre OntoBLOGP:PDTI_presenta_Inc ?incertidumbre.\n" +
                                "	?Proyecto_De_TI  OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "	OntoBLOGP:"+idproyecto+" OntoBLOGP:PDTI_presenta_Inc  ?incertidumbre.\n" +
                                "	?Riesgo OntoBLOGP:nombreRiesgo ?nombreRiesgo.\n" +
                                "	?Riesgo OntoBLOGP:descripcionRiesgo ?registroRiesgo.\n" +
                                "	?incertidumbre OntoBLOGP:Inc_puede_ser_RP ?Riesgo.\n" +
                                "}\n";
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
                RDFNode registroriesgo = soln.get("?registroRiesgo");
                RDFNode nombreRiesgo = soln.get("?nombreRiesgo");
                //RDFNode idproyecto = soln.get("?IdProyecto");
                //Individual individuo = soln.getLiteral();
                //modelo.getIndividual(NS+"Proy00"+String.valueOf(cont));
                //cont++;
                
                //System.out.println("Id individuo: "+soln);
                //String idindividuo = "";
                if( (registroriesgo != null) && (nombreRiesgo != null) )
                {
                    json += "descripciónRiesgo: "+eliminarPrefijos(registroriesgo.toString());
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
                json= "descripciónRiesgo:SIN RIESGOS";
            }  
                
        }
        finally 
        {
            qexec.close(); 
        } 
        //System.out.println("IDProyecto: "+idproyecto);
        //System.out.println("Datos: "+json);
        return json;
        //return "[" + json+"]";
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
    
    public String  eliminarPrefijos (String linea){  // System.out.println("Entrando a eliminar pre");
        linea=linea.replace("^^http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#","");
        return linea;   
    }
    
}
