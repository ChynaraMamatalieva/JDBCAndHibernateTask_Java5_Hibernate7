package peaksoft.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import peaksoft.model.User;
import peaksoft.util.Util;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao, AutoCloseable {
    private final EntityManagerFactory entityManagerFactory = Util.createEntityManagerFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        EntityManager entityManager  = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public void dropUsersTable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("drop table users", User.class).executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();


    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public void removeUserById(long id) {
        User user = new User();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("select u from User u  where u.id = ?1", User.class)
                .setParameter(1, id)
                .getSingleResult();
        entityManager.remove(entityManager.find(User.class, id));
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public List<User> getAllUsers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<User> users = entityManager.createQuery("select u from User u", User.class)
                .getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        return users;
    }

    @Override
    public void cleanUsersTable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("truncate table users", User.class).executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public boolean existsByFirstName(String firstName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("select case when count(u) > 0 " +
                "then true " +
                "else false end " +
                "from User u where u.name = ?1", Boolean.class);
        query.setParameter(1, firstName);
        Boolean singleResult = (Boolean) query.getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();
        return singleResult;
    }

    @Override
    public void close() throws Exception {
        entityManagerFactory.close();

    }
}
