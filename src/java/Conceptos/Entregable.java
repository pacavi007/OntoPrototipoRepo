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


public class Entregable 
{
    static String NS = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    OntModel modelo = null;
    Model model=null;
    String queryRequest = "SELECT ?Nombre ?tipo ?estado ?version ?fechaEnt ?urlweb ?Id \n" +
                "WHERE \n" +
                "{\n" +
                "?Entregable OntoBLOGP:nombreEntregable ?Nombre. \n" +
                "?Entregable OntoBLOGP:tipoEntregable ?tipo. \n" +
                "?Entregable OntoBLOGP:estadoEntregable ?estado. \n" +
                "?Entregable OntoBLOGP:versionEntregable ?version. \n" +
                //"?Entregable OntoBLOGP:responsable ?respon. \n" +
                "?Entregable OntoBLOGP:fechaEntrega ?fechaEnt. \n" +
                "?Entregable OntoBLOGP:URL ?urlweb." +
                "?Entregable OntoBLOGP:idEntregable ?Id." +
                "}\n" + 
                "Orderby ?Nombre";
    
    public Entregable(){
        
    }
    
    public String consultarEntregables(Model model){
        this.model= model;
        return ejecutarConsulta(this.model);
    }
    
    public String crearEntregables(String nom, String tip, String est, String vers, String fechaE, String url,String otroscon) throws FileNotFoundException{
        cargarOntologia();
        int cont = contarIndividuos();
        OntClass entregables = modelo.getOntClass(NS+"Entregable");
        DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"nombreEntregable");
        DatatypeProperty tipo = modelo.getDatatypeProperty(NS+"tipoEntregable");
        DatatypeProperty estado = modelo.getDatatypeProperty(NS+"estadoEntregable");
        DatatypeProperty version = modelo.getDatatypeProperty(NS+"versionEntregable");
        //DatatypeProperty responsable = modelo.getDatatypeProperty(NS+"responsable");
        DatatypeProperty fechaEntrega = modelo.getDatatypeProperty(NS+"fechaEntrega");
        DatatypeProperty urlweb = modelo.getDatatypeProperty(NS+"URL");
        DatatypeProperty identregable = modelo.getDatatypeProperty(NS+"idEntregable");
        System.out.println("Otroscon "+otroscon);
        String[] auxotroscon = otroscon.split(";");
        String[] interesados = null;
        if ((auxotroscon.length>0)) {
            interesados = auxotroscon[0].split(",");
        }
        String[] procesos = null;
        if ((auxotroscon.length>1)) {
            procesos = auxotroscon[1].split(",");
        }
        String[] proyectos = null;
        if ((auxotroscon.length>2)) {
            proyectos = auxotroscon[2].split(",");
        }
        //System.out.println("Entran a crear entre");
        
        Individual nuevoEntregable = modelo.createIndividual(NS+"Etgb00"+String.valueOf(cont),entregables);
        nuevoEntregable.setPropertyValue(nombre, modelo.createTypedLiteral(nom,NS));
        nuevoEntregable.setPropertyValue(tipo, modelo.createTypedLiteral(tip,NS));
        nuevoEntregable.setPropertyValue(estado, modelo.createTypedLiteral(est,NS));
        nuevoEntregable.setPropertyValue(version, modelo.createTypedLiteral(vers,NS));
        //nuevoEntregable.setPropertyValue(responsable, modelo.createTypedLiteral(respon,NS));
        nuevoEntregable.setPropertyValue(fechaEntrega, modelo.createTypedLiteral(fechaE,NS));
        nuevoEntregable.setPropertyValue(urlweb, modelo.createTypedLiteral(url,NS));
        nuevoEntregable.setPropertyValue(identregable, modelo.createTypedLiteral("Etgb00"+String.valueOf(cont),NS));
        if ((auxotroscon.length>0)&&(!auxotroscon[0].isEmpty())) {
            for (int i = 0; i < interesados.length; i++) {
                if ((interesados[i].split(":"))[1].equals("S")) {
                    Individual interesado = modelo.getIndividual(NS + (interesados[i].split(":"))[0]);
                    ObjectProperty objp = modelo.getObjectProperty(NS + "Ent_es_responsabilidad_De");
                    nuevoEntregable.addProperty(objp, interesado);
                }
            }
        }
        if ((auxotroscon.length>1)&&(!auxotroscon[1].isEmpty())) {
            for (int i = 0; i < procesos.length; i++) {
                if ((procesos[i].split(":"))[1].equals("S")) {
                    Individual proceso = modelo.getIndividual(NS + (procesos[i].split(":"))[0]);
                    ObjectProperty obj = modelo.getObjectProperty(NS + "Pro_genera_Ent");
                    proceso.addProperty(obj, nuevoEntregable);
                    ObjectProperty objp2 = modelo.getObjectProperty(NS + "Ent_se_genera_Pro");
                    nuevoEntregable.addProperty(objp2, modelo.getIndividual(NS + (procesos[i].split(":"))[0]));
                }
            }
        }
        if ((auxotroscon.length>2)&&(!auxotroscon[2].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    Individual proyecto = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);
                    ObjectProperty objp = modelo.getObjectProperty(NS + "PDTI_comprende_E");
                    proyecto.addProperty(objp, modelo.getIndividual(NS + "Etgb00" + String.valueOf(cont)));
                    ObjectProperty objp2 = modelo.getObjectProperty(NS + "Ent_pertenece_PDTI");
                    nuevoEntregable.addProperty(objp2, modelo.getIndividual(NS + (proyectos[i].split(":"))[0]));
                }
            }
        } 
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "Entregable creado";
    }
    
    public String editarEntregables(String id, String nom, String tip, String est, String vers, String fechaE, String url,String otroscon) throws IOException{
        cargarOntologia();
        DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"nombreEntregable");
        DatatypeProperty tipo = modelo.getDatatypeProperty(NS+"tipoEntregable");
        DatatypeProperty estado = modelo.getDatatypeProperty(NS+"estadoEntregable");
        DatatypeProperty version = modelo.getDatatypeProperty(NS+"versionEntregable");
        //DatatypeProperty responsable = modelo.getDatatypeProperty(NS+"responsable");
        DatatypeProperty fechaEntrega = modelo.getDatatypeProperty(NS+"fechaEntrega");
        DatatypeProperty urlweb = modelo.getDatatypeProperty(NS+"URL");
        System.out.println("Otroscon "+otroscon);
        String[] auxotroscon = otroscon.split(";");
        String[] interesados = null;
        if ((auxotroscon.length>0)) {
            interesados = auxotroscon[0].split(",");
        }
        String[] procesos = null;
        if ((auxotroscon.length>1)) {
            procesos = auxotroscon[1].split(",");
        }
        String[] proyectos = null;
        if ((auxotroscon.length>2)) {
            proyectos = auxotroscon[2].split(",");
        }
        
        Individual nuevoEntregable = modelo.getIndividual(NS+id);
        nuevoEntregable.setPropertyValue(nombre, modelo.createTypedLiteral(nom,NS));
        nuevoEntregable.setPropertyValue(tipo, modelo.createTypedLiteral(tip,NS));
        nuevoEntregable.setPropertyValue(estado, modelo.createTypedLiteral(est,NS));
        nuevoEntregable.setPropertyValue(version, modelo.createTypedLiteral(vers,NS));
        //nuevoEntregable.setPropertyValue(responsable, modelo.createTypedLiteral(respon,NS));
        nuevoEntregable.setPropertyValue(fechaEntrega, modelo.createTypedLiteral(fechaE,NS));
        nuevoEntregable.setPropertyValue(urlweb, modelo.createTypedLiteral(url,NS));
        
        ObjectProperty prop1 = modelo.getObjectProperty(NS+"Ent_es_responsabilidad_De");
        ObjectProperty prop2 = modelo.getObjectProperty(NS+"Ent_se_genera_Pro");
        ObjectProperty prop3 = modelo.getObjectProperty(NS+"Ent_pertenece_PDTI");
        nuevoEntregable.removeAll(prop1);
        nuevoEntregable.removeAll(prop2);
        nuevoEntregable.removeAll(prop3);
        if ((auxotroscon.length>0)&&(!auxotroscon[0].isEmpty())) {
            for (int i = 0; i < interesados.length; i++) {
                //System.out.println("auxotroscon "+interesados[i]); 
                Individual interesado = modelo.getIndividual(NS + (interesados[i].split(":"))[0]);                
                ObjectProperty objp = modelo.getObjectProperty(NS + "Ent_es_responsabilidad_De");                
                //ObjectProperty objp2 = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
                if ((interesados[i].split(":"))[1].equals("S")) {
                    //interesado.addProperty(objp, modelo.getIndividual(NS + id));                    
                    nuevoEntregable.addProperty(objp, interesado);
                } /*else {
                    Resource subject = modelo.getIndividual(NS + (interesados[i].split(":"))[0]);
                    Property predicate = modelo.getObjectProperty(NS + "Int_pertenece_PDTI");
                    RDFNode rdf = modelo.getIndividual(NS + id);
                    modelo.remove(subject, predicate, rdf);
                }*/
            }
        }
        if ((auxotroscon.length>1)&&(!auxotroscon[1].isEmpty())) {
            for (int i = 0; i < procesos.length; i++) {
                Individual proceso = modelo.getIndividual(NS + (procesos[i].split(":"))[0]);
                ObjectProperty objp = modelo.getObjectProperty(NS + "Pro_genera_Ent");
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "Ent_se_genera_Pro");
                if ((procesos[i].split(":"))[1].equals("S")) {
                    proceso.addProperty(objp, modelo.getIndividual(NS + id));
                    nuevoEntregable.addProperty(objp2, modelo.getIndividual(NS + (procesos[i].split(":"))[0]));
                }  
                else{
                    Resource subject = modelo.getIndividual(NS + (procesos[i].split(":"))[0]);
                    Property predicate = modelo.getObjectProperty(NS + "Pro_genera_Ent");
                    RDFNode rdf = modelo.getIndividual(NS + id);
                    modelo.remove(subject, predicate, rdf);
                }
            }
        }
        if ((auxotroscon.length>2)&&(!auxotroscon[2].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                Individual proyecto = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);                
                ObjectProperty objp = modelo.getObjectProperty(NS + "PDTI_comprende_E");                
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "Ent_pertenece_PDTI");
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    proyecto.addProperty(objp, modelo.getIndividual(NS + id));                    
                    nuevoEntregable.addProperty(objp2, modelo.getIndividual(NS + (proyectos[i].split(":"))[0]));
                }
                else {
                    Resource subject = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);
                    Property predicate = modelo.getObjectProperty(NS + "PDTI_comprende_E");
                    RDFNode rdf = modelo.getIndividual(NS + id);
                    modelo.remove(subject, predicate, rdf);
                }
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
        return "Entregable editado";
    }
    
    public String editarURL (String id, String url,String token) throws IOException{
        cargarOntologia();
        DatatypeProperty urlweb = modelo.getDatatypeProperty(NS+"URL");
        //System.out.println("id "+id);
        System.out.println("url "+url);
        System.out.println("token "+token);
        //String enlace =url+"&token="+token;
        Individual nuevoEntregable = modelo.getIndividual(NS+id);
        //nuevoEntregable.setPropertyValue(urlweb, modelo.createTypedLiteral(url,NS));
        nuevoEntregable.setPropertyValue(urlweb, modelo.createTypedLiteral(url,NS));
        /*ObjectProperty prop1 = modelo.getObjectProperty(NS+"Ent_es_responsabilidad_De");
        ObjectProperty prop2 = modelo.getObjectProperty(NS+"Ent_se_genera_Pro");
        ObjectProperty prop3 = modelo.getObjectProperty(NS+"Ent_pertenece_PDTI");
        nuevoEntregable.removeAll(prop1);
        nuevoEntregable.removeAll(prop2);
        nuevoEntregable.removeAll(prop3);*/
       
        
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        return "URL editada";
    }
    
    public String eliminarEntregables (String id) throws FileNotFoundException{
        cargarOntologia();
        System.out.println("Eliminando Entregable");
        Individual individuo = modelo.getIndividual(NS+id);
        individuo.remove(); 
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
       return "Entregable eliminado";
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
        String json = "";
        try 
        {
            ResultSet response = qexec.execSelect();
            System.out.println("Iniciando busqueda de Entregables");
            while(response.hasNext())
            {
                QuerySolution soln = response.nextSolution();
                RDFNode nombre = soln.get("?Nombre"); //
                RDFNode tipo = soln.get("?tipo"); //
                RDFNode estado = soln.get("?estado"); //
                RDFNode version = soln.get("?version"); //
                RDFNode fechaEnt = soln.get("?fechaEnt"); //
                RDFNode urlweb = soln.get("?urlweb"); //
                RDFNode identregable = soln.get("?Id");
                if( (nombre != null) && (tipo != null) && (estado != null) && (version != null) && (fechaEnt != null) && (urlweb != null))
                { 
                    json += "{\"nombre\":\""+eliminarPrefijos(nombre.toString())  +"\"," + 
                            "\"tipo\":\""+eliminarPrefijos(tipo.toString())  +"\"," + 
                            "\"estado\":\""+eliminarPrefijos(estado.toString())  +"\"," +
                            "\"version\":\""+eliminarPrefijos(version.toString())  +"\"," +
                            "\"fechaent\":\""+eliminarPrefijos(fechaEnt.toString()) +"\"," + 
                            "\"url\":\""+eliminarPrefijos(urlweb.toString()) +"\"," +
                            "\"interesados\":\""+mostrarInteresadosPorEntregable(eliminarPrefijos(identregable.toString()))+"\"," +
                            "\"procesos\":\""+mostrarProcesosPorEntregable(eliminarPrefijos(identregable.toString()))+"\"," +
                            "\"proyectos\":\""+mostrarProyectosPorEntregable(eliminarPrefijos(identregable.toString()))+"\"," +
                            "\"identregable\":\""+ eliminarPrefijos(identregable.toString()) +"\"}" ;
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
    
    public String mostrarTodoPorTodo(Model modelo){
        model = modelo;
        String json="";
        json = json+mostrarInteresados()+";";
        json = json+mostrarProcesos()+";";
        json = json+mostrarProyectos()+"";
        return json;
    }
    
    public String mostrarInteresadosPorEntregable(String identregable){
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?Nombre ?Telefono \n" +
                                "WHERE \n" +
                                "{\n" +
                                "       ?Interesado OntoBLOGP:telefono ?Telefono.\n" +
                                "	?Interesado OntoBLOGP:nombreInteresado ?Nombre.\n" +
                                "	OntoBLOGP:"+identregable+" OntoBLOGP:Ent_es_responsabilidad_De ?Interesado .\n" +
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
                if( (nombre != null) && (telefono != null) && (identregable != null))
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
        return json;
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
                                "?Interesado OntoBLOGP:idInteresado ?idinteresado." +
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
    
    public String mostrarProcesosPorEntregable(String identregable){
        StringBuffer queryStr = new StringBuffer();
        String queryRequesteaux="SELECT ?nombreproceso \n" +
                                "WHERE \n" +
                                "{\n" +
                                "       ?Proceso OntoBLOGP:nombreProceso ?nombreproceso.\n" +
                                "       ?Proceso OntoBLOGP:Pro_genera_Ent OntoBLOGP:"+identregable+".\n" +
                                "}\n" +
                                "Orderby (?nombreproceso) \n";
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
                RDFNode nombreproceso = soln.get("?nombreproceso");
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
                json= "Proceso:SIN PROCESOS";
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
        String queryRequesteaux="SELECT ?nombreproceso ?idproceso \n" +
                                "WHERE\n" +
                                "{\n" +
                                "?Proceso OntoBLOGP:nombreProceso ?nombreproceso.\n" +
                                "?Proceso OntoBLOGP:idProceso ?idproceso.\n" +
                                "}\n" +
                                "Orderby (?nombreproceso) \n";
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
                RDFNode Proceso = soln.get("?nombreproceso");
                RDFNode idproceso = soln.get("?idproceso");
                if(Proceso!=null)
                {
                    json += "Proceso:"+eliminarPrefijos(Proceso.toString())+"&&";
                    json += "idproceso:"+eliminarPrefijos(idproceso.toString());
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
        //System.out.println("Datos CVP por todo: "+json);
        return json;
    }
    
    public String mostrarProyectosPorEntregable(String identregable){
        StringBuffer queryStr = new StringBuffer();
        //System.out.println("id "+identregable);
        String queryRequesteaux="SELECT ?proyecto \n" +
                                "WHERE \n" +
                                "{ \n" +
                                "   ?Proyecto_De_TI OntoBLOGP:PDTI_comprende_E OntoBLOGP:"+identregable+".\n" +
                                "   ?Proyecto_De_TI OntoBLOGP:titulo ?proyecto.\n" +
                                "} \n" +
                                "Orderby (?proyecto)";
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
                if( (proyecto != null) )
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
                json= "Proyecto:ENTREGABLE NO ASIGNADO A PROYECTO";
            }     
        }
        finally 
        {
            qexec.close(); 
        } 
        return json;
    }
    
    public String mostrarProyectos(){
        //model = modelo;
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

    public  int contarIndividuos( ){
        int cont = 0;
        OntClass proyectos = modelo.getOntClass(NS+"Entregable");
        for(Iterator it = proyectos.listInstances(true);it.hasNext();){
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
