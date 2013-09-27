package com.example.tada.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import com.example.tada.model.Post;
import com.example.tada.rest.dto.PostDTO;

/**
 * 
 */
@Stateless
@Path("/posts")
public class PostEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(PostDTO dto)
   {
      Post entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(PostEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Post entity = em.find(Post.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Post> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Post p WHERE p.id = :entityId ORDER BY p.id", Post.class);
      findByIdQuery.setParameter("entityId", id);
      Post entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      PostDTO dto = new PostDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<PostDTO> listAll()
   {
      final List<Post> searchResults = em.createQuery("SELECT DISTINCT p FROM Post p ORDER BY p.id", Post.class).getResultList();
      final List<PostDTO> results = new ArrayList<PostDTO>();
      for (Post searchResult : searchResults)
      {
         PostDTO dto = new PostDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, PostDTO dto)
   {
      TypedQuery<Post> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Post p WHERE p.id = :entityId ORDER BY p.id", Post.class);
      findByIdQuery.setParameter("entityId", id);
      Post entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}