package edu.touro.mco152.bm.persist;

import edu.touro.mco152.bm.ObserverInterface;
import jakarta.persistence.EntityManager;
import org.apache.commons.math3.analysis.function.Add;

public class AddToDatabaseObserver implements ObserverInterface {

    @Override
    public void update(DiskRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }
}
