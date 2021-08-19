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


public class Interesado 
{
    static String NS = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    OntModel modelo = null;
    Model model=null;
    String queryRequest = "SELECT ?Nombre ?Influencia ?Cargo ?Rol ?Equipo ?Telefono ?Email ?Id \n" +
                "WHERE \n" +
                "{\n" +
                "?Interesado OntoBLOGP:nombreInteresado ?Nombre. \n" +
                "?Interesado OntoBLOGP:tipoInfluencia ?Influencia. \n" +
                "?Interesado OntoBLOGP:cargo ?Cargo. \n" +
                "?Interesado OntoBLOGP:rolProyecto ?Rol. \n" +
                "?Interesado OntoBLOGP:equipoProyecto ?Equipo. \n" +
                "?Interesado OntoBLOGP:telefono ?Telefono. \n" +
                "?Interesado OntoBLOGP:email ?Email. \n" +
                "?Interesado OntoBLOGP:idInteresado ?Id." +
                "}\n" + 
                "Orderby ?Nombre";
    
    public Interesado(){
        
    }
    
    public String consultarInteresados(Model model){
        this.model= model;
        return ejecutarConsulta(this.model);
    }
    
    public String crearInteresados(String nom, String inf, String car, String rolE, String equi, String tele, String email,String otroscon) throws FileNotFoundException{
        cargarOntologia();
        int cont = contarIndividuos();
        OntClass interesados = modelo.getOntClass(NS+"Interesado");
        DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"nombreInteresado");
        DatatypeProperty influencia = modelo.getDatatypeProperty(NS+"tipoInfluencia");
        DatatypeProperty cargo = modelo.getDatatypeProperty(NS+"cargo");
        DatatypeProperty rol = modelo.getDatatypeProperty(NS+"rolProyecto");
        DatatypeProperty equipo = modelo.getDatatypeProperty(NS+"equipoProyecto");
        DatatypeProperty telefono = modelo.getDatatypeProperty(NS+"telefono");
        DatatypeProperty correo = modelo.getDatatypeProperty(NS+"email");
        DatatypeProperty idinteresado = modelo.getDatatypeProperty(NS+"idInteresado");
        //System.out.println("Otroscon "+otroscon);
        String[] proyectos = otroscon.split(",");
        Individual nuevoInteresado = modelo.createIndividual(NS+"Inte00"+String.valueOf(cont),interesados);
        nuevoInteresado.setPropertyValue(nombre, modelo.createTypedLiteral(nom,NS));
        nuevoInteresado.setPropertyValue(influencia, modelo.createTypedLiteral(inf,NS));
        nuevoInteresado.setPropertyValue(cargo, modelo.createTypedLiteral(car,NS));
        nuevoInteresado.setPropertyValue(rol, modelo.createTypedLiteral(rolE,NS));
        nuevoInteresado.setPropertyValue(equipo, modelo.createTypedLiteral(equi,NS));
        nuevoInteresado.setPropertyValue(telefono, modelo.createTypedLiteral(tele,NS));
        nuevoInteresado.setPropertyValue(correo, modelo.createTypedLiteral(email,NS));
        nuevoInteresado.setPropertyValue(idinteresado, modelo.createTypedLiteral("Inte00"+String.valueOf(cont),NS));
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    Individual proyecto = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);
                    ObjectProperty objp = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
                    proyecto.addProperty(objp, modelo.getIndividual(NS + "Inte00" + String.valueOf(cont)));
                    ObjectProperty objp2 = modelo.getObjectProperty(NS + "Int_pertenece_PDTI");
                    nuevoInteresado.addProperty(objp2, modelo.getIndividual(NS + (proyectos[i].split(":"))[0]));
                }
            }
        }
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
        /*System.out.println("Proyecto agregado "+cont);
        System.out.println("titulo "+tit);
        System.out.println("objetivo "+obj);
        System.out.println("descripcion "+des);
        System.out.println("inicio "+fechai);
        System.out.println("fin "+fechaf);
        */
        return "Interesado creado";
    }
    
    public String editarInteresados(String id, String nom, String inf, String car, String rolE, String equi, String tele, String email,String otroscon) throws IOException{
        cargarOntologia();
        DatatypeProperty nombre = modelo.getDatatypeProperty(NS+"nombreInteresado");
        DatatypeProperty influencia = modelo.getDatatypeProperty(NS+"tipoInfluencia");
        DatatypeProperty cargo = modelo.getDatatypeProperty(NS+"cargo");
        DatatypeProperty rol = modelo.getDatatypeProperty(NS+"rolProyecto");
        DatatypeProperty equipo = modelo.getDatatypeProperty(NS+"equipoProyecto");
        DatatypeProperty telefono = modelo.getDatatypeProperty(NS+"telefono");
        DatatypeProperty correo = modelo.getDatatypeProperty(NS+"email");
        //System.out.println("Otroscon "+otroscon);
        String[] proyectos = otroscon.split(",");
        Individual nuevoInteresado = modelo.getIndividual(NS+id);
        nuevoInteresado.setPropertyValue(nombre, modelo.createTypedLiteral(nom,NS));
        nuevoInteresado.setPropertyValue(influencia, modelo.createTypedLiteral(inf,NS));
        nuevoInteresado.setPropertyValue(cargo, modelo.createTypedLiteral(car,NS));
        nuevoInteresado.setPropertyValue(rol, modelo.createTypedLiteral(rolE,NS));
        nuevoInteresado.setPropertyValue(equipo, modelo.createTypedLiteral(equi,NS));
        nuevoInteresado.setPropertyValue(telefono, modelo.createTypedLiteral(tele,NS));
        nuevoInteresado.setPropertyValue(correo, modelo.createTypedLiteral(email,NS));
        
        ObjectProperty prop1 = modelo.getObjectProperty(NS+"Int_pertenece_PDTI");
        nuevoInteresado.removeAll(prop1);
        if ((proyectos.length>0)&&(!proyectos[0].isEmpty())) {
            for (int i = 0; i < proyectos.length; i++) {
                Individual proyecto = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);                
                ObjectProperty objp = modelo.getObjectProperty(NS + "PDTI_tiene_Int");                
                ObjectProperty objp2 = modelo.getObjectProperty(NS + "Int_pertenece_PDTI");
                if ((proyectos[i].split(":"))[1].equals("S")) {
                    proyecto.addProperty(objp, modelo.getIndividual(NS + id));                    
                    nuevoInteresado.addProperty(objp2, modelo.getIndividual(NS + (proyectos[i].split(":"))[0]));
                }
                else {
                    Resource subject = modelo.getIndividual(NS + (proyectos[i].split(":"))[0]);
                    Property predicate = modelo.getObjectProperty(NS + "PDTI_tiene_Int");
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
        return "Interesado editado";
    }
    
    public String eliminarInteresados (String id) throws FileNotFoundException{
        cargarOntologia();
        System.out.println("Eliminando interesado");
        Individual individuo = modelo.getIndividual(NS+id);
        individuo.remove(); 
        OutputStream out = new FileOutputStream("Onto/OntoBLOGP1.0.owl");
        modelo.write(out, "RDF/XML");
       return "Interesado eliminado";
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
            System.out.println("Iniciando busqueda de interesados");
            while(response.hasNext())
            {
                QuerySolution soln = response.nextSolution();
                RDFNode nombre = soln.get("?Nombre");
                RDFNode influencia = soln.get("?Influencia");
                RDFNode cargo = soln.get("?Cargo"); 
                RDFNode rol = soln.get("?Rol");
                RDFNode equipo = soln.get("?Equipo");
                RDFNode telefono = soln.get("?Telefono");
                RDFNode email = soln.get("?Email");
                RDFNode idinteresado = soln.get("?Id");
                if( (nombre != null) && (influencia != null) && (cargo != null) && (rol != null) && (equipo != null) && (telefono != null) && (email != null))
                { 
                    json += "{\"nombre\":\""+eliminarPrefijos(nombre.toString())  +"\"," + 
                            "\"influencia\":\""+eliminarPrefijos(influencia.toString())  +"\"," + 
                            "\"cargo\":\""+eliminarPrefijos(cargo.toString())  +"\"," +
                            "\"rol\":\""+eliminarPrefijos(rol.toString())  +"\"," +
                            "\"equipo\":\""+eliminarPrefijos(equipo.toString()) +"\"," + 
                            "\"telefono\":\""+eliminarPrefijos(telefono.toString()) +"\"," +
                            "\"email\":\""+eliminarPrefijos(email.toString())+"\"," +
                            "\"proyectos\":\""+mostrarProyectosPorInteresado(eliminarPrefijos(idinteresado.toString()))+"\"," +
                            "\"idinteresado\":\""+ eliminarPrefijos(idinteresado.toString()) +"\"}" ;
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
    
    
    public String mostrarProyectosPorInteresado(String idinteresado){
        StringBuffer queryStr = new StringBuffer();System.out.println("id "+idinteresado);
        String queryRequesteaux="SELECT ?proyecto \n" +
                                "WHERE \n" +
                                "{ \n" +
                 "?Proyecto_De_TI OntoBLOGP:PDTI_tiene_Int OntoBLOGP:"+idinteresado+".\n" +
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
                json= "Proyecto:INTERESADO NO ASIGNADO A PROYECTO";
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

    public  int contarIndividuos( ){
        int cont = 0;
        OntClass proyectos = modelo.getOntClass(NS+"Interesado");
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
