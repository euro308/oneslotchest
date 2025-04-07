package net.educanet.oneslotchest.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private static EntityManagerFactory emf;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void initialize() {
        try {
            emf = Persistence.createEntityManagerFactory("oneslotchest-pu");
            System.out.println("JPA bylo úspěšně inicializováno!");
        } catch (Exception e) {
            System.err.println("Chyba při inicializaci JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        executor.shutdown();
    }

    public static void recordChestPlacement(String playerName, int x, int y, int z, String dimension) {
        executor.submit(() -> {
            if (emf == null || !emf.isOpen()) {
                System.err.println("EntityManagerFactory není dostupná");
                return;
            }

            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = null;

            try {
                tx = em.getTransaction();
                tx.begin();

                ChestPlacement placement = new ChestPlacement();
                placement.setPlayerName(playerName);
                placement.setX(x);
                placement.setY(y);
                placement.setZ(z);
                placement.setDimension(dimension);
                placement.setPlacementTime(LocalDateTime.now());

                em.persist(placement);
                tx.commit();

                System.out.println("Položení truhly zaznamenáno pro hráče: " + playerName);
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                System.err.println("Chyba při záznamu položení truhly: " + e.getMessage());
                e.printStackTrace();
            } finally {
                em.close();
            }
        });
    }
}
