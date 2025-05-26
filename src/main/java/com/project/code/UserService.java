package com.project.code;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveUserWithProfile(User user) {
        entityManager.persist(user); // cascade saves profile too
    }

    public User getUserById(int userId) {
        return entityManager.find(User.class, userId);
    }

    @Transactional
    public void savePost(Post post) {
        entityManager.persist(post);
    }

    public List<Post> getPostsByUserId(int userId) {
        return entityManager
                .createQuery("FROM Post p WHERE p.user.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
