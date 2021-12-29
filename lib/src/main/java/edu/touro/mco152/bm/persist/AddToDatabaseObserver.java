package edu.touro.mco152.bm.persist;

import edu.touro.mco152.bm.observer.ObserverInterface;
import jakarta.persistence.EntityManager;

public class AddToDatabaseObserver implements ObserverInterface {
    /**
     * updates the database
     * @param run takes in a DiskRun
     */
    @Override
    public void update(DiskRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }
}
