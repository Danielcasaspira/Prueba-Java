package test;

import java.util.List;
import domain.Persona;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TestPersonaServiceRS {

    //Variables que usaremos 
    private static final String URL_BASE = "http://localhost:8080/SgaJeeWeb/webservice";
    private static Client cliente;
    private static WebTarget webTarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;
    
    public static void main(String[] args) {

        cliente = ClientBuilder.newClient();
        
        //Leer una persona (metodo get)
        webTarget = cliente.target(URL_BASE).path("/personas");
        //Proporcionamos un idPersona valido
        persona = webTarget.path("/25").request(MediaType.APPLICATION_XML).get(Persona.class);        
        System.out.println("Persona recuperada: " + persona);

        //Leer todas las personas (metodo get con readEntity de tipo List<>)
        personas = webTarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        System.out.println("\nPersonas recuperadas:");
        imprimirPersonas(personas);

        //Agregar una persona (metodo post)       
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre("Daniel");
        nuevaPersona.setApellidoPaterno("Casas");
        nuevaPersona.setApellidoPaterno("Pira");
        nuevaPersona.setEmail("hdaniel0519@mail.com");
        invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(nuevaPersona, MediaType.APPLICATION_XML));
 
        System.out.println("");
        System.out.println(response.getStatus());
        //Recuperamos a la persona recien agregada para despues modificarla y al final eliminarla
        Persona personaRecuperada = response.readEntity(Persona.class);
        System.out.println("Persona agregada: " + personaRecuperada);
        
        //Modificar una persona (metodo put)
        //persona recuperada anteriormente
        Persona personaModificar = personaRecuperada;
        personaModificar.setApellidoMaterno("CambioApeMat");
        String pathId = "/" + personaModificar.getIdPersona();
        invocationBuilder =  webTarget.path( pathId ).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(personaModificar, MediaType.APPLICATION_XML));
 
        System.out.println("");
        System.out.println(response.getStatus());
        System.out.println("Persona modificada: " + response.readEntity(Persona.class));

        //Eliminar una persona       
        // persona recuperada anteriormente        
        Persona personaEliminar = personaRecuperada;
        String pathEliminarId = "/" + personaEliminar.getIdPersona();
        invocationBuilder =  webTarget.path( pathEliminarId ).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();
 
        System.out.println("");
        System.out.println(response.getStatus());
        System.out.println("Persona Eliminada: ");
    }
    
    private static void imprimirPersonas(List<Persona> personas){
       for(Persona persona: personas){
            System.out.println("Persona:" + persona);
        }
    }
}