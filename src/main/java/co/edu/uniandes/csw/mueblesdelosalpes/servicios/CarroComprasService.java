/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockRemote;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/CarroCompras")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
 

public class CarroComprasService {
 
    /**
     * Referencia al Ejb de carritos encargada de realizar las operaciones del mismo.
     */
    @EJB
    private IServicioCarritoMockRemote carroEjb;
    /**
     * Servicio que recibe un objeto JSON con un mueble que se desea agregar al carrito de compras. Este servicio es el que se va a probar en 
     * JMeter. Ejecución sin problemas hasta los 5000 usuarios en 10 segundos.)
     *
     * @param mb Mueble en formato JSON, que automáticamente se parsea a un objeto Mueble por el API REST.
     */
   
 
    public List<Mueble> agregarMuebles(List<Mueble> mb) {
        for (Mueble mueble : mb) {
            carroEjb.agregarItem(mueble);
        }
        
        return mb;
    }
    
    public void eliminarMuebles(List<Mueble> mb) {
        for (Mueble mueble : mb) {
            carroEjb.removerItem(mueble, true);
        }
    }
 
    /**
     * Servicio que ofrece una lista JSON con los elementos que estan actualmente en el carrito.
     * @return la lista JSON con los muebles actuales del carrito.
  
     */
    
    public List<Mueble> getTodosLosMuebles() {
        return carroEjb.getInventario();
 
    }
    
    @GET
    @Path("/CarroCompras")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mueble> getCarrito() {
        return carroEjb.getInventario();
    }
    
    @POST
    @Path("/CarroCompras")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Mueble> agregarAlCarrito(List<Mueble> muebles) {
        for (Mueble mueble : muebles) {
            carroEjb.agregarItem(mueble);
        }
        return carroEjb.getInventario();
    }
    
    @PUT
    @Path("/CarroCompras/{referencia}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void actualizarCantidadCarrito(@PathParam("referencia") long referencia, Mueble mueble) {
        Mueble muebleExistente = carroEjb.getInventario().stream()
                                      .filter(m -> m.getReferencia() == referencia)
                                      .findFirst()
                                      .orElse(null);
        if (muebleExistente != null) {
            carroEjb.removerItem(muebleExistente, true);
            mueble.setCantidad(mueble.getCantidad());
            carroEjb.agregarItem(mueble);
        }
    }
    
    @DELETE
    @Path("/CarroCompras/{referencia}")
    public void eliminarDelCarrito(@PathParam("referencia") long referencia) {
        Mueble muebleExistente = carroEjb.getInventario().stream()
                                      .filter(m -> m.getReferencia() == referencia)
                                      .findFirst()
                                      .orElse(null);
        if (muebleExistente != null) {
            carroEjb.removerItem(muebleExistente, true);
        }
    }
    
    @POST
    @Path("/CarroCompras/comprar")
    public void realizarCompra(Usuario usuario) {
        carroEjb.comprar(usuario);
    }
}
