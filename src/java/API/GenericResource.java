package API;

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
        @DefaultValue("") @QueryParam("superatributo") String superatributo
    ) throws IOException
            
    {
        GenericResource myontology = new GenericResource();
        myontology.populateFOAFFriends();
        String json = myontology.myStudents(myontology._student, option, atributo1, atributo2, atributo3, atributo4, atributo5, atributo6, atributo7, superatributo); 
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
    
    private String myStudents(Model model, String option, String atributo1, String atributo2, String atributo3, String atributo4, String atributo5, String atributo6, String atributo7, String superatributo) throws IOException
    { 
        String query = "";
        opciones = Integer.parseInt(option);
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
                    //System.out.println("Otros concep "+superatributo);
                query = proyec.crearProyectos(titulo,objetivo,descripcion,fechainicio,fechafin,presupuesto,otrosconceptos);
                break;
                }
            case "3": 
                {   
                /*query = "SELECT ?Nombre ?TipoInfluencia ?Cargo ?RolProyecto ?EquipoProyecto ?Telefono ?Email \n" +
                "WHERE { \n" +
                "?Interesado OntoBLOGP:nombreInteresado ?Nombre. \n" +
                "?Interesado OntoBLOGP:tipoInfluencia ?TipoInfluencia. \n" +
                "?Interesado OntoBLOGP:cargo ?Cargo. \n" +
                "?Interesado OntoBLOGP:rolProyecto ?RolProyecto. \n" +
                "?Interesado OntoBLOGP:equipoProyecto ?EquipoProyecto. \n" +
                "?Interesado OntoBLOGP:telefono ?Telefono. \n" +
                "?Interesado OntoBLOGP:email ?Email.}";*/
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
                    /*query = "SELECT ?first_name ?last_name ?age\n" +
                    " WHERE {\n" +
                    " ?Student ROSCC:First_Name ?first_name. \n" +
                    " ?Grading ROSCC:is_A_Student ?Student. \n" +
                    " ?Student ROSCC:Age ?age. \n" +
                    " ?Student ROSCC:Last_Name ?last_name.\n" +
                    " ?Grading ROSCC:Finish_Grade ?finish_grade. Filter(?finish_grade " + comparativeExpression +
                    "'" + grade + "') } \n" + 
                    " Orderby ?first_name"; */
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
            default: 
                return " ";
        } 
        /*StringTokenizer aux= new StringTokenizer(query,":");
        System.out.println("LA cadena es ");
        while(aux.hasMoreTokens())
        {
            System.out.println(""+aux.nextToken());
        }*/
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
   