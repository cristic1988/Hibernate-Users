package org.example.app.repository.impl;

import jakarta.persistence.criteria.*;
import org.example.app.entity.User;
import org.example.app.repository.AppRepository;
import org.example.app.utils.Constants;
import org.example.app.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserRepository implements AppRepository<User> {

    @Override
    public String create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return Constants.DATA_INSERT_MSG;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            return e.getMessage();
        }
    }

    @Override
    public Optional<List<User>> read() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root);
            Query<User> query = session.createQuery(cq);
            List<User> list = query.getResultList();
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String update(User user) {
        if (!isEntityWithSuchIdExists(user)) {
            return Constants.DATA_ABSENT_MSG;
        } else {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaUpdate<User> cu = cb.createCriteriaUpdate(User.class);
                Root<User> root = cu.from(User.class);
                cu.set("name", user.getName());
                cu.set("email", user.getEmail());
                cu.where(cb.equal(root.get("id"), user.getId()));
                session.createMutationQuery(cu).executeUpdate();
                transaction.commit();
                return Constants.DATA_UPDATE_MSG;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return e.getMessage();
            }
        }
    }

    @Override
    public String delete(Long id) {
        if (readById(id).isEmpty()) {
            return Constants.DATA_ABSENT_MSG;
        } else {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
                Root<User> root = cd.from(User.class);
                cd.where(cb.equal(root.get("id"), id));
                session.createMutationQuery(cd).executeUpdate();
                transaction.commit();
                return Constants.DATA_DELETE_MSG;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return e.getMessage();
            }
        }
    }

    @Override
    public Optional<User> readById(Long id) {
        Transaction transaction = null;
        Optional<User> optional;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("id"), id));
            Query<User> query = session.createQuery(cq);
            optional = query.uniqueResultOptional();
            transaction.commit();
            return optional;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Optional.empty();
        }
    }

    private boolean isEntityWithSuchIdExists(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, user.getId());
            if (user != null) {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<User> cq = cb.createQuery(User.class);
                cq.from(User.class);
                session.createQuery(cq).setMaxResults(1);
            }
            return user != null;
        }
    }

}
