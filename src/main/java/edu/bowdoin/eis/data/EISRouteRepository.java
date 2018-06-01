package edu.bowdoin.eis.data;

import edu.bowdoin.eis.core.EISRoute;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EISRouteRepository extends CrudRepository<EISRoute, Long> {
    EISRoute findFirstByRouteNameOrderBySeqDesc(String routeName);
    EISRoute findFirstByRouteNameAndSeq(String routeName, Integer seq);
    EISRoute findFirstByActiveTrueAndRouteNameOrderBySeqDesc(String routeName);

    @Modifying
    @Transactional
    @Query("update EISRoute r set r.active=false where r.routeName=?1")
    void clearActiveByRouteName(@Param("routeName") String routeName);

    List<EISRoute> findByActiveTrue();
}

