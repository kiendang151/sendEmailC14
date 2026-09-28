package murach.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import murach.business.User;

public class UserDB {


    // =========================================
    // INSERT USER
    // =========================================

    public static int insert(User user) {

        EntityManager em =
                DBUtil.getEmf()
                      .createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            em.persist(user);

            transaction.commit();

            return 1;

        } catch (Exception e) {

            if (transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();

            return 0;

        } finally {

            em.close();
        }
    }


    // =========================================
    // SELECT USER
    // =========================================

    public static User selectUser(
            String email) {

        EntityManager em =
                DBUtil.getEmf()
                      .createEntityManager();

        try {

            TypedQuery<User> query =
                    em.createQuery(
                            "SELECT u FROM User u "
                            + "WHERE u.email = :email",
                            User.class
                    );


            query.setParameter(
                    "email",
                    email
            );


            try {

                return query.getSingleResult();

            } catch (NoResultException e) {

                return null;
            }

        } finally {

            em.close();
        }
    }


    // =========================================
    // CHECK EMAIL
    // =========================================

    public static boolean emailExists(
            String email) {

        User user =
                selectUser(email);

        return user != null;
    }
}