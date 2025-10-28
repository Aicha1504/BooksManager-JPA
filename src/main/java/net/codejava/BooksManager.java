package net.codejava;

import jakarta.persistence.*;
import java.util.List;

public class BooksManager {
    static EntityManagerFactory factory;
    static EntityManager entityManager;

    public static void main(String[] args) {
        begin(); // Démarre la connexion et la transaction

        
     // Création de plusieurs livres pour le test
        createBook("Livre Pas Cher", "Auteur 1", 25.0f);
        createBook("Livre Cher", "Auteur 2", 60.0f);
        createBook("Livre Moyen", "Auteur 3", 35.0f);
        
        // ICI, VOUS APPELLEREZ LES MÉTHODES DE TEST :
        //create();   // Pour créer un livre
        //find();  // Pour lire un livre
        // update(); // Pour modifier un livre
        // remove(); // Pour supprimer un livre
         query();  // Pour lister des livres

        end(); // Valide la transaction et ferme la connexion
    }

    private static void begin() {
        factory = Persistence.createEntityManagerFactory("BookUnit");
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin(); // Début de la transaction
    }

    private static void end() {
        entityManager.getTransaction().commit(); // Validation de la transaction
        entityManager.close();
        factory.close();
    }

    // --- MÉTHODES À COMPLÉTER CI-DESSOUS ---

    private static void create() {
        System.out.println("Création d'un nouveau livre");
        Book newBook = new Book();
        newBook.setTitle("Effective Java");
        newBook.setAuthor("Joshua Bloch");
        newBook.setPrice(39.0f);
        entityManager.persist(newBook); // Sauvegarde le livre en base
        System.out.println("Livre créé avec l'ID : " + newBook.getBookId());
    }

    private static void find() {
        Integer primaryKey = 1; // L'ID du livre à chercher
        Book book = entityManager.find(Book.class, primaryKey);
        if (book != null) {
            System.out.println("Livre trouvé :");
            System.out.println("Titre : " + book.getTitle());
            System.out.println("Auteur : " + book.getAuthor());
            System.out.println("Prix : " + book.getPrice());
        } else {
            System.out.println("Livre non trouvé.");
        }
    }

    private static void update() {
        // On récupère d'abord le livre à modifier
        Book existingBook = entityManager.find(Book.class, 1); // ID 1
        if (existingBook != null) {
            System.out.println("Mise à jour du livre...");
            existingBook.setTitle("Effective Java (Third Edition)");
            existingBook.setPrice(45.0f);
            // entityManager.merge(existingBook); // Inutile, les changements sont automatiquement suivis
            System.out.println("Livre mis à jour.");
        }
    }

 // Ajoutez cette méthode utilitaire
    private static void createBook(String titre, String auteur, float prix) {
        Book book = new Book();
        book.setTitle(titre);
        book.setAuthor(auteur);
        book.setPrice(prix);
        entityManager.persist(book);
        System.out.println("Livre créé: " + titre);
    }
    
    private static void remove() {
        // On récupère une référence au livre (plus léger que find)
        Book reference = entityManager.getReference(Book.class, 1); // ID 1
        if (reference != null) {
            System.out.println("Suppression du livre...");
            entityManager.remove(reference);
            System.out.println("Livre supprimé.");
        }
    }

    private static void query() {
        System.out.println("Liste des livres de moins de 30 euros :");
        String jpql = "SELECT b FROM Book b WHERE b.price < 30"; // JPQL, pas du SQL !
        Query query = entityManager.createQuery(jpql);
        List<Book> listBooks = query.getResultList();

        for (Book aBook : listBooks) {
            System.out.println(aBook.getTitle() + ", " + aBook.getAuthor() + ", " + aBook.getPrice());
        }
    }
}