package r16a.Athena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import r16a.Athena.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c WHERE c.name = :clientName")
    boolean existsByName(@Param("clientName") String clientName);
}
