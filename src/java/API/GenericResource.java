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
import java.io.File;
import java.io.InputStream; 
import javax.ws.rs.DefaultValue;

/**
* REST Web Service 
* * @author eduardos 
*/
@Path("generic") 

public class GenericResource 
{
    static String defaultNameSpace = "http://www.semanticweb.org/asus/ontologies/2019/10/OntoBLOGP#"; 
    Model _student = null; 
    Model schema = null; 
    InfModel inferredStudent = null;
    
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
        myontology.populateFOAFFriends(); System.out.println("en linea 64");
        String json = myontology.myStudents(myontology._student, option, age, comparative, group, assignment, grade, gender); 
        return json;
    }
    
    private void populateFOAFFriends()
    { 
        _student = ModelFactory.createOntologyModel(); 
        InputStream inFoafInstance = 
        FileManager.get().open("/Users/Unicauca/Desktop/OntoBLOGP/OntoPrototipoRepo/src/java/API/OntoBLOGP.owl");
        //FileManager.get().openNoMap("SampleUniversity4.owl"); 
        //FileManager.get().open("/Users/Unicauca/Desktop/OntoBLOGP/OntoPrototipoRepo/src/java/API/SampleUniversity4.owl");
        _student.read(inFoafInstance,defaultNameSpace); 
        //System.out.println("Namespace es: "+defaultNameSpace);
  
    }
    
    private String myStudents(Model model, String option, String age, String comparativeExpression, String group, String assignment, String grade, String gender)
    { 
        String query = "";
        
        switch (option) 
        { 
            case "1": 
            {
                System.out.println("en linea 87");
                query = "SELECT ?entrega ?proyecto\n" +
                " WHERE {\n" +
                " ?entrega OntoBLOGP:PDTI_comprende_E  ?proyecto.} \n" ;
            break;
            }
            case "2": 
                {
                    query = "SELECT ?first_name ?last_name ?age\n" + " WHERE {\n" +
                    " ?Student ROSCC:First_Name ?first_name. \n" +
                    " ?Student ROSCC:is_Enrrolled ROSCC:" + group + ". \n" +
                    " ?Student ROSCC:Last_Name ?last_name. \n" +
                    " ?Student ROSCC:Age ?age. Filter(?age "+ comparativeExpression + "'" + age + "') } \n" + 
                    " Orderby ?first_name";
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
        queryStr.append("PREFIX ROSCC:<http://www.semanticweb.org/jegjo/ontologies/Myontology1#>"); 
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
                QuerySolution soln = response.nextSolution();
                RDFNode firstname = soln.get("?first_name");
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
   