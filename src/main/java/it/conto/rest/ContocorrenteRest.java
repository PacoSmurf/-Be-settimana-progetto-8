package it.conto.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.contatto.entity.ContoCorrente;
import it.contatto.entity.Movimento;
import it.contatto.entity.Operazione;



@Path("/conto")
public class ContocorrenteRest {
	private static ArrayList<ContoCorrente> conti = new ArrayList<>();
	private static ArrayList<Movimento> movimenti = new ArrayList<>();
	//http://localhost:8080/Progetto/rest/conto
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaConto (ContoCorrente c) {
		for(ContoCorrente conto : conti) {
			if(conto.getIban().equals(c.getIban())) {
				return Response.status(404).entity("Conto corrente gia esistente").build();
				
			}
		}
		
		conti.add(c);
		return Response.status(200).entity("creazione conto avenuto con successo").build();
		
	}
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancellaConto(ContoCorrente c) {
		
		for (ContoCorrente con : conti  ) {
			
			if(con.getIban().equals(c.getIban())){
				conti.remove(con);
				return Response.status(200).entity("rimozione avenuta con successo").build();
			}
			
			
			
		}
		return Response.status(404).entity("rimozione non avvenuta").build();
		
		
		
	}
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateConto( ContoCorrente c) {
		for(ContoCorrente con: conti) {
			if(con.getIban().equals(c.getIban())) {
				int index = conti.lastIndexOf(con);
				conti.set(index, c);
				return Response.status(200).entity("conto aggiornato").build();
				
			}
		}
		return Response.status(404).entity("conto non aggiornato").build();
		
		
	}
	@PUT
	@Path("/movimento")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response movimento(Movimento m) {
		for(ContoCorrente con : conti) {
			if(con.getIban().equals(m.getIban())) {
				
			}
		
		if(m.getTipo().equals(Operazione.PRELIEVO)) {
			if(m.getImporto()> con.getSaldo() && m.getImporto()<0) {
				return Response.status(406).entity("non dispensabile").build();
				
			}
			double nuovosaldo = con.getSaldo()-m.getImporto();
			con.setSaldo(nuovosaldo);
			movimenti.add(m);
			return Response.status(200).entity("Operaione andata a buon fine  il saldo è:" + nuovosaldo).build();
			
		}
		
		if(m.getTipo().equals(Operazione.VERSAMENTO)) {
			if(m.getImporto() < 0) {
				return Response.status(406).entity("non dispensabile").build();
				
			}
			double nuovosaldo = con.getSaldo()+m.getImporto();
			con.setSaldo(nuovosaldo);
			movimenti.add(m);
			return Response.status(200).entity("Operaione andata a buon fine  il saldo è:" + nuovosaldo).build();
			
			
		}
		
		
		
	}
		return Response.status(404).entity("non effetuato").build();
	}
	
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List <Movimento>getMovimenti(){
		
		return movimenti;
		
	}
		
	
	
		

}
