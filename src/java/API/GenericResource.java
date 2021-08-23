package API;

import Conceptos.Entregable;
import Conceptos.Riesgo;
import Conceptos.Incidente;
import Conceptos.Interesado;
import Conceptos.Supuesto;
import Conceptos.Proyecto;
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
import com.hp.hpl.jena.sparql.resultset.JSONObjectResult;
import com.hp.hpl.jena.util.FileManager; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream; 
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DefaultValue;

/**
* REST Web Service 
* * @author eduardos 
*/
@Path("generic") 

public class GenericResource 
{
    //static String defaultNameSpace = "http://www.semanticweb.org/jegjo/ontologies/Myontology1#";
    static String defaultNameSpace = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#";
    Model _student = null; 
    Model schema = null; 
    InfModel inferredStudent = null;
    int opciones=0;
    
    @Context 
    private UriInfo context;
    /**
     * Creates a new instance of GenericResource */ 
    public GenericResource()
    {
    
    }
    
    /**
     * Retrieves representation of an instance of API.GenericResource
     * @return an instance of java.lang.String
     */
    
    @GET 
    @Produces(MediaType.APPLICATION_JSON) 
    public String search
    ( 
        @DefaultValue("") @QueryParam("option") String option, 
        @DefaultValue("") @QueryParam("atributo1") String atributo1, 
        @DefaultValue("") @QueryParam("atributo2") String atributo2, 
        @DefaultValue("") @QueryParam("atributo3") String atributo3, 
        @DefaultValue("") @QueryParam("atributo4") String atributo4, 
        @DefaultValue("") @QueryParam("atributo5") String atributo5, 
        @DefaultValue("") @QueryParam("atributo6") String atributo6,
        @DefaultValue("") @QueryParam("atributo7") String atributo7,
        @DefaultValue("") @QueryParam("atributo8") String atributo8,
        @DefaultValue("") @QueryParam("superatributo") String superatributo
    ) throws IOException
            
    {
        GenericResource myontology = new GenericResource();
        myontology.populateFOAFFriends();
        String json = myontology.myStudents(myontology._student, option, atributo1, atributo2, atributo3, atributo4, atributo5, atributo6, atributo7, atributo8, superatributo); 
        return json;
    }
    
    private void populateFOAFFriends()
    { 
        _student = ModelFactory.createOntologyModel(); 
        InputStream inFoafInstance = 
        FileManager.get().open("Onto/OntoBLOGP1.0.owl"); 
        _student.read(inFoafInstance,defaultNameSpace); 
        //System.out.println("Ruta es: "+System.getProperty("user.dir" ));
    }
    
    private String myStudents(Model model, String option, String atributo1, String atributo2, String atributo3, String atributo4, String atributo5, String atributo6, String atributo7, String atributo8, String superatributo) throws IOException
    { 
        String query = "";
        opciones = Integer.parseInt(option); 
        //System.out.println("option es "+option);
        switch (option) 
        { 
            case "1": 
                {
                Proyecto proyec = new Proyecto(); 
                query = proyec.consultarProyectos(model); 
                //proyec.mostrarInteresadosPorProyecto("hola");
                break;
                }
            case "2": 
                {
                String titulo      = atributo1;
                String objetivo    = atributo2;
                String descripcion = atributo3;
                String fechainicio = atributo4;
                String fechafin    = atributo5;
                String presupuesto = atributo6;
                String otrosconceptos = superatributo;
                Proyecto proyec = new Proyecto();
                query = proyec.crearProyectos(titulo,objetivo,descripcion,fechainicio,fechafin,presupuesto,otrosconceptos);
                break;
                }
            case "3": 
                {   
                String id          = atributo7;
                String titulo      = atributo1;
                String objetivo    = atributo2;
                String descripcion = atributo3;
                String fechainicio = atributo4;
                String fechafin    = atributo5;
                String presupuesto = atributo6;
                String otrosconceptos = superatributo;
                Proyecto proyec = new Proyecto();
                query = proyec.editarProyectos(id, titulo, objetivo, descripcion, fechainicio, fechafin, presupuesto,otrosconceptos);
                break; 
                } 
            case "4": 
                {   
                String id = atributo7;
                Proyecto proyec = new Proyecto();
                query = proyec.eliminarProyectos(id);
                break; 
                }
            case "5":
                {
                Proyecto proyec = new Proyecto();
                query = proyec.mostrarTodoPorTodo(model);
                break;
                }
            case "11": 
                {
                Interesado inte = new Interesado(); 
                query = inte.consultarInteresados(model); 
                //proyec.mostrarInteresadosPorProyecto("hola");
                break;
                }
            case "12": 
                {
                String nombre      = atributo1;
                String influencia    = atributo2;
                String cargo = atributo3;
                String rol = atributo4;
                String equipo    = atributo5;
                String telefono = atributo6;
                String email = atributo8;
                String otrosconceptos = superatributo;
                Interesado inte = new Interesado();
                query = inte.crearInteresados(nombre,influencia,cargo,rol,equipo,telefono,email,otrosconceptos);
                break;
                }
            case "13": 
                {   
                String id          = atributo7;
                String nombre      = atributo1;
                String influencia    = atributo2;
                String cargo = atributo3;
                String rol = atributo4;
                String equipo    = atributo5;
                String telefono = atributo6;
                String email = atributo8;
                String otrosconceptos = superatributo;
                Interesado inte = new Interesado();
                query = inte.editarInteresados(id, nombre, influencia, cargo, rol, equipo, telefono,email,otrosconceptos);
                break; 
                } 
            case "14": 
                {   
                String id = atributo7;
                Interesado inte = new Interesado();
                query = inte.eliminarInteresados(id);
                break; 
                }
            case "15":
                {
                Interesado inte = new Interesado();
                query = inte.mostrarProyectos(model);
                break;
                }
            case "21": 
                {
                Entregable entr = new Entregable(); 
                query = entr.consultarEntregables(model); 
                //proyec.mostrarInteresadosPorProyecto("hola");
                break;
                }
            case "22": 
                {
                String nombre      = atributo1;
                String tipo    = atributo2;
                String estado = atributo3;
                String version = atributo4;
                String fechaentrega    = atributo5;
                String url = atributo6;
                String proceso = atributo8;
                String otrosconceptos = superatributo;
                Entregable entr = new Entregable();
                query = entr.crearEntregables(nombre,tipo,estado,version,fechaentrega,url,otrosconceptos);
                break;
                }
            case "23": 
                {   
                String id          = atributo7;
                String nombre      = atributo1;
                String tipo    = atributo2;
                String estado = atributo3;
                String version = atributo4;
                String fechaentrega    = atributo5;
                String url = atributo6;
                String otrosconceptos = superatributo;
                Entregable entr = new Entregable();
                query = entr.editarEntregables(id, nombre, tipo, estado, version, fechaentrega, url,otrosconceptos);
                break; 
                } 
            case "24": 
                {   
                String id = atributo7;
                Entregable entr = new Entregable();
                query = entr.eliminarEntregables(id);
                break; 
                }
            case "25":
                {
                Entregable entr = new Entregable();
                query = entr.mostrarTodoPorTodo(model);
                break;
                }
            case "26":
                {
                //System.out.println("url "+atributo1);
                //System.out.println("token "+atributo2);
                String id  = atributo7;
                String nombrearchi = atributo1;
                String token = atributo2;
                Entregable entr = new Entregable();
                query = entr.editarURL(id,nombrearchi,token);
                break;
                }
            case "31":
                {
                Supuesto supues = new Supuesto();
                query= supues.consultarSupuestos(model);
                break;
                }
            case "32":
                {
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Supuesto supues = new Supuesto(); 
                //System.out.println("proyec "+otrosconceptos);
                query= supues.crearSupuestos(nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "33":
                {
                String id          = atributo7;
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Supuesto supues = new Supuesto();
                query= supues.editarSupuestos(id,nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "34":
                {
                String id = atributo7;
                Supuesto supues = new Supuesto();
                query= supues.eliminarSupuestos(id);
                break;
                }
            case "35":
                {
                Supuesto supues = new Supuesto();
                query= supues.mostrarProyectos(model);
                break;
                }
            case "41":
                {
                Incidente inciden = new Incidente();
                query= inciden.consultarIncidentes(model);
                break;
                }
            case "42":
                {
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Incidente inciden = new Incidente(); 
                query= inciden.crearIncidentes(nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "43":
                {
                String id          = atributo7;
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Incidente inciden = new Incidente();
                query= inciden.editarIncidentes(id,nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "44":
                {
                String id = atributo7;
                Incidente inciden = new Incidente();
                query= inciden.eliminarIncidentes(id);
                break;
                }
            case "45":
                {
                Incidente inciden = new Incidente();
                query= inciden.mostrarProyectos(model);
                break;
                }
            case "51":
                {
                Riesgo ries = new Riesgo();
                query= ries.consultarRiesgos(model);
                break;
                }
            case "52":
                {
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Riesgo ries = new Riesgo(); 
                query= ries.crearRiesgos(nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "53":
                {
                String id          = atributo7;
                String nombreRDS      = atributo1;
                String descripcionRDS    = atributo2;
                String otrosconceptos = superatributo;
                Riesgo ries = new Riesgo();
                query= ries.editarRiesgos(id,nombreRDS,descripcionRDS,otrosconceptos);
                break;
                }
            case "54":
                {
                String id = atributo7;
                Riesgo ries = new Riesgo();
                query= ries.eliminarRiesgos(id);
                break;
                }
            case "55":
                {
                Riesgo ries = new Riesgo();
                query= ries.mostrarProyectos(model);
                break;
                }
            default: 
                return "DEFAULT";
        } 
        return query;
    }
    
    private String runQuery(String queryRequest, Model model)
    {
        StringBuffer queryStr = new StringBuffer();
        
        // Establish Prefixes 
        //Set default Name space first 
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        //Now add query
        queryStr.append(queryRequest); 
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        String json = "";
        try 
        {
            ResultSet response = qexec.execSelect();
            System.out.println("Starting search");
            
            while(response.hasNext())
            {
                switch(opciones)
                {
                    case 1:
                        QuerySolution soln = response.nextSolution();
                        RDFNode titulo = soln.get("?Titulo");
                        RDFNode objetivo = soln.get("?Objetivo");
                        RDFNode descripcion = soln.get("?Descripcion"); 
                        RDFNode fechainicio = soln.get("?FechaInicio");
                        RDFNode fechafin = soln.get("?FechaFin");
                        if( (titulo != null) && (objetivo != null) && (descripcion != null) && (fechainicio != null) && (fechafin != null))
                        {
                            json += "{\"titulo\":\""+ titulo.toString() +"\"," + 
                                    "\"objetivo\":\""+ objetivo.toString() +"\"," + 
                                    "\"descripcion\":\""+ descripcion.toString() +"\"," +
                                    "\"fechainicio\":\""+ fechainicio.toString() +"\"," +
                                    "\"fechafin\":\""+ fechafin.toString() +"\"}" ;
                            if (response.hasNext())
                            {
                                json += ",";
                            }
                        }
                        else
                        {
                            System.out.println("No data found!"); 
                        }
                        break;
                    case 3:
                        QuerySolution soln3 = response.nextSolution();
                        RDFNode nombreinteresado = soln3.get("?Nombre");
                        RDFNode tipoinfluencia = soln3.get("?TipoInfluencia");
                        RDFNode cargo = soln3.get("?Cargo"); 
                        RDFNode rolproyecto = soln3.get("?RolProyecto");
                        RDFNode equipoproyecto = soln3.get("?EquipoProyecto"); 
                        RDFNode telefono = soln3.get("?Telefono");
                        RDFNode email = soln3.get("?Email");
                        if( (nombreinteresado != null) && (tipoinfluencia != null) && (equipoproyecto != null) && (telefono != null) && (email != null))
                        {
                            json += "{\"nombreinteresado\":\""+ nombreinteresado.toString() +"\"," + 
                                    "\"tipoinfluencia\":\""+ tipoinfluencia.toString() +"\"," + 
                                    "\"cargo\":\""+ cargo.toString() +"\"," +
                                    "\"rolproyecto\":\""+ rolproyecto.toString() +"\"," +
                                    "\"equipoproyecto\":\""+ equipoproyecto.toString() +"\"," +
                                    "\"telefono\":\""+ telefono.toString() +"\"," +
                                    "\"email\":\""+ email.toString() +"\"}" ;
                            if (response.hasNext())
                            {
                                json += ",";
                            }
                        }
                        else
                        {
                            System.out.println("No data found!"); 
                        }
                        break;
                }
                
            }
        }
        finally 
        {
            qexec.close(); 
        } 
        return "[" + json + "]";
    }
}
   