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
import com.hp.hpl.jena.util.FileManager; 
import java.io.InputStream; 
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
        @DefaultValue("1") @QueryParam("option") String option, 
        @DefaultValue("Grp0001") @QueryParam("group") String group, 
        @DefaultValue("") @QueryParam("comparative") String comparative, 
        @DefaultValue("") @QueryParam("assignment") String assignment, 
        @DefaultValue("") @QueryParam("gender") String gender, 
        @DefaultValue("") @QueryParam("age") String age, 
        @DefaultValue("") @QueryParam("grade") String grade 
    )
            
    {
        GenericResource myontology = new GenericResource();
        myontology.populateFOAFFriends();
        String json = myontology.myStudents(myontology._student, option, age, comparative, group, assignment, grade, gender); 
        return json;
    }
    
    private void populateFOAFFriends()
    { 
        _student = ModelFactory.createOntologyModel(); 
        InputStream inFoafInstance = 
        FileManager.get().open("Onto/OntoBLOGP1.0.owl");
        //FileManager.get().open("Onto/SampleUniversity4.owl");
        //FileManager.get().open("/Users/Unicauca/Documents/NetBeansProjects/Example/src/java/API/SampleUniversity4.owl"); 
        _student.read(inFoafInstance,defaultNameSpace); 
        System.out.println("Ruta es: "+System.getProperty("user.dir" ));
    }
    
    private String myStudents(Model model, String option, String age, String comparativeExpression, String group, String assignment, String grade, String gender)
    { 
        String query = "";
        opciones = Integer.parseInt(option);
        switch (option) 
        { 
            case "1": 
            {
                query = "SELECT  ?nombreIN \n" +
                "WHERE { \n" +
                "?Interesado OntoBLOGP:nombreInteresado ?nombreIN. \n" +   
                "?Interesado OntoBLOGP:Co_tiene_varios_Int OntoBLOGP:Consorcio_0.}";
                
                /*query = "SELECT ?entrega ?proyecto \n" +
	        " WHERE {\n" + 
		" ?Entregable   OntoBLOGP:descripcionEntregable  ?entrega.}"; */
		                 /*       
                query = "SELECT ?first_name ?last_name ?age ?name_group\n" +
                " WHERE {\n" +
                " ?Student ROSCC:First_Name ?first_name. \n" +
                " ?Student ROSCC:is_Enrrolled ROSCC:" + group + ".\n" +
                " ?Student ROSCC:Last_Name ?last_name.\n" +
                " ?Student ROSCC:Age ?age.} \n" +
                " Orderby ?first_name"; */
            break;
            }
            case "2": 
                {  System.out.println("en 08");
                query = "SELECT ?Titulo ?Objetivo ?Descripcion ?FechaInicio ?FechaFin \n" +
                "WHERE { \n" +
                "?Proyecto_De_TI OntoBLOGP:titulo ?Titulo. \n" +
                "?Proyecto_De_TI OntoBLOGP:objetivo ?Objetivo. \n" +
                "?Proyecto_De_TI OntoBLOGP:descripcion ?Descripcion. \n" +
                "?Proyecto_De_TI OntoBLOGP:fechaInicio ?FechaInicio. \n" +
                "?Proyecto_De_TI OntoBLOGP:fechaFin ?FechaFin.}";
 
                /* query = "SELECT ?first_name ?last_name ?age\n" + " WHERE {\n" +
                " ?Student ROSCC:First_Name ?first_name. \n" +
                " ?Student ROSCC:is_Enrrolled ROSCC:" + group + ". \n" +
                " ?Student ROSCC:Last_Name ?last_name. \n" +
                " ?Student ROSCC:Age ?age. Filter(?age "+ comparativeExpression + "'" + age + "') } \n" + 
                " Orderby ?first_name"; */
                break;
                }
            case "3": 
                {   
                    query = "SELECT ?first_name ?last_name ?age\n" +
                    " WHERE {\n" +
                    " ?Teacher ROSCC:First_Name ?first_name. \n" +
                    " ?Teacher ROSCC:is_Imparted ROSCC:" + assignment + ". \n" +
                    " ?Teacher ROSCC:Last_Name ?last_name. \n" + 
                    " ?Teacher ROSCC:Age ?age.} \n" + 
                    " Orderby ?first_name"; 
                break; 
                } 
            case "4": 
                {   
                    query = "SELECT ?first_name ?last_name ?age\n" +
                    " WHERE {\n" +
                    " ?Student ROSCC:First_Name ?first_name. \n" +
                    " ?Grading ROSCC:is_A_Student ?Student. \n" +
                    " ?Student ROSCC:Age ?age. \n" +
                    " ?Student ROSCC:Last_Name ?last_name.\n" +
                    " ?Grading ROSCC:Finish_Grade ?finish_grade. Filter(?finish_grade " + comparativeExpression +
                    "'" + grade + "') } \n" + 
                    " Orderby ?first_name";
                break; 
                }
            case "5":
                {
                    query = "SELECT ?first_name ?last_name ?age\n" +
                    " WHERE {\n" + 
                    " ?Teacher ROSCC:First_Name ?first_name. \n" +
                    " ?Teacher ROSCC:is_Imparted ?assignment. \n" +
                    " ?Teacher ROSCC:Last_Name ?last_name. \n" + 
                    " ?Teacher ROSCC:Age ?age. \n" + 
                    " ?Teacher ROSCC:Gender ?gender. Filter(?gender = '" + gender + "') } \n" + 
                    " Orderby ?first_name"; 
                    break;
                }
            default: 
                return " ";
        }
        //Listing students
        return runQuery(query,model);//Add the query string
    }
    
    private String runQuery(String queryRequest, Model model)
    {
        StringBuffer queryStr = new StringBuffer();
        
        // Establish Prefixes 
        //Set default Name space first 
        queryStr.append("PREFIX OntoBLOGP: <http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#>"); 
        //queryStr.append("PREFIX ROSCC:<http://www.semanticweb.org/jegjo/ontologies/Myontology1#>");
        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n") ;
        queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdfsyntax-ns#" + "> "); 
        queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdfschema#" + "> ");
        queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + ">");
        //Now add query
        queryStr.append(queryRequest); 
        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        System.out.println("en 183 "+qexec.toString());
        //System.out.println("en 184 "+queryStr);
        String json = "";
        try 
        {
            ResultSet response = qexec.execSelect();
            System.out.println("Starting search"); //System.out.println("en 189 "+response.hasNext());
            while(response.hasNext())
            {System.out.println("en 191 "+opciones);
                switch(opciones)
                {
                    case 2:
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
                        RDFNode nombreinteresado = soln3.get("?NombreI");
                        RDFNode tipoinfluencia = soln3.get("?TipoI");
                        RDFNode equipoproyecto = soln3.get("?Equipo"); 
                        RDFNode telefono = soln3.get("?Telefono");
                        RDFNode email = soln3.get("?Email");
                        if( (nombreinteresado != null) && (tipoinfluencia != null) && (equipoproyecto != null) && (telefono != null) && (email != null))
                        {
                            json += "{\"nombreinteresado\":\""+ nombreinteresado.toString() +"\"," + 
                                    "\"tipoinfluencia\":\""+ tipoinfluencia.toString() +"\"," + 
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
                /* QuerySolution soln = response.nextSolution();
                RDFNode nombreIN = soln.get("?nombreIN");
                if((nombreIN!=null))
                {
                    json += "{\"nombre\":\""+ nombreIN.toString() +"\"}" ;
                            
                    if (response.hasNext())
                    {
                        json += ",";
                    }
                }
                else
                {
                    System.out.println("No data found!"); 
                } */
                /* RDFNode firstname = soln.get("?first_name");
                RDFNode lastname = soln.get("?last_name"); 
                RDFNode age = soln.get("?age");
                if( (firstname != null) && (lastname != null) && (age != null))
                {
                    json += "{\"name\":\""+ firstname.toString() +"\"," + 
                            "\"last_name\":\""+ lastname.toString() +"\"," + 
                            "\"age\":\""+ age.toString() +"\"}";
                    if (response.hasNext())
                    {
                        json += ",";
                    }
                }
                else
                {
                    System.out.println("No data found!"); 
                } */
            }
        }
        finally 
        {
            qexec.close(); 
        }
        return "[" + json + "]";
    }
}
   