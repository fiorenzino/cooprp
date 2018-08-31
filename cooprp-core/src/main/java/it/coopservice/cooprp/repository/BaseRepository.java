package it.coopservice.cooprp.repository;

import org.giavacms.api.repository.AbstractRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseRepository<T> extends AbstractRepository<T>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   public EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "uuid";
   }
}
