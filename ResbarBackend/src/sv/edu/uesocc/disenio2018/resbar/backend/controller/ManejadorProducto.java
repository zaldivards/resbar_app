/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.uesocc.disenio2018.resbar.backend.controller;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.edu.uesocc.disenio2018.resbar.backend.controller.exceptions.ErrorApplication;
import sv.edu.uesocc.disenio2018.resbar.backend.entities.Producto;

/**
 *
 * @author zaldivar
 */
public class ManejadorProducto  {
   
    private ManejadorProducto(){
        
    }
    
    protected static EntityManager getEM() {
        return DBUtil.getEmFactory("ResbarBackendPU").createEntityManager();
    }

    protected static void insertar(Producto entity) {
        
        EntityManager eml = getEM();
        EntityTransaction et = eml.getTransaction();
        try {
            if (!et.isActive()) {
                et.begin();
            }
            eml.persist(entity);
            et.commit();
        } catch (Exception ex) {
              if (et.isActive()) {
                et.rollback();
            }
            throw new ErrorApplication("Algo fallo intentando insertar un nuevo producto --> $ManejadorProducto.insertar() ---> "+ex.getMessage());       
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }

        }
    }

    protected static Producto eliminar(Producto entity) {
        EntityManager eml = getEM();
        EntityTransaction trans = eml.getTransaction();
        try {
            if (!trans.isActive()) {
                trans.begin();
            }
            eml.remove(eml.merge(entity));

            trans.commit();
            return entity;
        } catch (Exception ex) {
            if (trans.isActive()) {
                trans.rollback();
            }
            
            return null;
            
        } finally {

            eml.close();
        }
    }

    protected static boolean actualizar(Producto entityObject) {
        EntityManager eml = getEM();
        EntityTransaction et = eml.getTransaction();
        try {
            if (!et.isActive()) {
                et.begin();
            }
            eml.merge(entityObject);
            et.commit();
            return true;
        } catch (Exception ex) {
            if (et.isActive()) {
                et.rollback();
            }
            return false;
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }

        }
    }

//  

    protected static Object obtener(Integer id) {
        EntityManager eml = getEM();
        try {
            return eml.find(Producto.class, id);
        } catch (Exception e) {
            System.err.print("El error es :"+e);
            return null;
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }

        }
    }

    protected static int obtenerID() {
        EntityManager eml = getEM();
        try {
            CriteriaQuery cq = eml.getCriteriaBuilder().createQuery(Integer.class);
            Root producto = cq.from(Producto.class);
            cq.select(eml.getCriteriaBuilder().max(producto.get("idProducto")));
            Query q = eml.createQuery(cq);
            return ((int)q.getSingleResult()) + 1;
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }
        }
    }

    public static List<Producto> obtenerxCategoria(int id) {
        EntityManager eml = getEM();//entitymanagerlocal
        try {

            Query query = eml.createNamedQuery("Producto.findByCategoria");
            query.setParameter("categoria", id);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }
        }
    }

    public static List<Producto> buscar(String charSequence) {
        EntityManager eml = getEM();
        try {
            Query query = eml.createNamedQuery("Producto.findByCharsequence");
            query.setParameter("nombre", charSequence);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }
        }
    }


    
    

}
//  protected static List<Object> findEntities() {
//        return findEntities(true, -1, -1);
//    }
//
//    protected static List<Object> findEntities(int maxResults, int firstResult) {
//        return findEntities(false, maxResults, firstResult);
//    }
//
//    private static List<Object> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager eml = getEM();
//        try {
//            CriteriaQuery cq = eml.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Producto.class));
//            Query q = eml.createQuery(cq);
//            if (!all) {
//                q.setMaxResults(maxResults);
//                q.setFirstResult(firstResult);
//            }
//            return q.getResultList();
//        } finally {
//            if (eml.isOpen()) {
//                eml.close();
//            }
//        }
//    }