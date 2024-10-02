package r16a.Athena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import r16a.Athena.models.UserAuthMetadata;

import java.util.Optional;

@Repository
public interface UserAuthMetadataRepository extends JpaRepository<UserAuthMetadata, Integer> {
    @Query("SELECT u FROM UserAuthMetadata u WHERE u.user.id = :userId")
    Optional<UserAuthMetadata> findByUserId(@Param("userId") Integer userId);
}
