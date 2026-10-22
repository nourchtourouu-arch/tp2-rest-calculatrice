package com.exemple.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/operations")
@Produces(MediaType.APPLICATION_JSON)
public class OperationRest {

    // "static" : partagés par toutes les requêtes
    private static final Map<Integer, Operation> OPERATIONS = new ConcurrentHashMap<>();
    private static final AtomicInteger COMPTEUR = new AtomicInteger(0);

    private final Calculatrice calculService = new Calculatrice();

    // Calcule le résultat et le range dans l'opération.
    // Retourne false si le calcul est impossible.
    private boolean calculer(Operation op) {
        if (op == null || op.getOperateur() == null) {
            return false;
        }
        switch (op.getOperateur()) {
            case "somme":
                op.setResultat(calculService.somme(op.getA(), op.getB()));
                return true;
            case "soustraction":
                op.setResultat(calculService.soustraction(op.getA(), op.getB()));
                return true;
            case "produit":
                op.setResultat(calculService.produit(op.getA(), op.getB()));
                return true;
            case "division":
                if (op.getB() == 0) {
                    return false;
                }
                op.setResultat(calculService.division(op.getA(), op.getB()));
                return true;
            default:
                return false;
        }
    }

    // POST /operations : crée une opération
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creer(Operation op, @Context UriInfo uriInfo) {
        if (!calculer(op)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\":\"Calcul impossible\"}")
                    .build();
        }
        int id = COMPTEUR.incrementAndGet();
        op.setId(id);
        OPERATIONS.put(id, op);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.created(location).entity(op).build();
    }

    // GET /operations : retourne toutes les opérations
    @GET
    public List<Operation> lister() {
        return new ArrayList<>(OPERATIONS.values());
    }

    // GET /operations/{id} : retourne une opération
    @GET
    @Path("/{id}")
    public Response lire(@PathParam("id") int id) {
        Operation op = OPERATIONS.get(id);
        if (op == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(op).build();
    }

    // PUT /operations/{id} : modifie une opération
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifier(@PathParam("id") int id, Operation op) {
        if (!OPERATIONS.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!calculer(op)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\":\"Calcul impossible\"}")
                    .build();
        }
        op.setId(id);
        OPERATIONS.put(id, op);
        return Response.ok(op).build();
    }

    // DELETE /operations/{id} : supprime une opération
    @DELETE
    @Path("/{id}")
    public Response supprimer(@PathParam("id") int id) {
        Operation supprimee = OPERATIONS.remove(id);
        if (supprimee == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}