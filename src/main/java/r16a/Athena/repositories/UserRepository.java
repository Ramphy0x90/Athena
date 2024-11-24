package r16a.Athena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import r16a.Athena.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :user OR u.userName = :user")
    Optional<User> findByEmailOrUserName(@Param("user") String user);

    @Query("SELECT u FROM User u WHERE (u.email = :user OR u.userName = :user) AND u.internal = TRUE")
    Optional<User> findAdminByEmailOrUserName(@Param("user") String user);

    @Query("SELECT u " +
            "FROM User u LEFT JOIN u.clients c " +
            "WHERE (u.email = :user OR u.userName = :user) AND c.id = :clientId")
    Optional<User> findByEmailOrUserNameAndClientId(@Param("user") String user, @Param("clientId") Integer clientId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u LEFT JOIN u.clients c " +
            "WHERE (u.email = :user OR u.userName = :user) AND c.id = :clientId")
    boolean existsByEmailOrUserNameAncClientId(@Param("user") String user, @Param("clientId") Integer clientId);
}
