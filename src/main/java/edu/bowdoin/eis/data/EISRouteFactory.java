package edu.bowdoin.eis.data;

import edu.bowdoin.eis.core.EISRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class EISRouteFactory {
    @Autowired
    private EISRouteRepository eisRouteRepo;

    public EISRouteFactory() {}

    public EISRoute saveEISRoute(EISRoute eisRoute) {
        EISRoute latest = eisRouteRepo.findFirstByRouteNameOrderBySeqDesc(eisRoute.getRouteName());


        if (latest == null) {
            eisRoute.setSeq(0);
        } else if (eisRoute.getRouteDefinition().equals(latest.getRouteDefinition())) {
            return latest;
        } else {
            eisRoute.setSeq(latest.getSeq() + 1);
        }

        eisRoute.setCreateDate(new Date());
        eisRoute.setCreatedBy("epearson");
        eisRoute.setDescription("foo");
        eisRoute.setActive(true);

        eisRouteRepo.clearActiveByRouteName(eisRoute.getRouteName());
        eisRouteRepo.save(eisRoute);

        return eisRoute;
    }

    public EISRoute getActiveEISRouteByName(String routeName) {
        return eisRouteRepo.findFirstByActiveTrueAndRouteNameOrderBySeqDesc(routeName);
    }

    public List<EISRoute> getAllActiveEISRoutes() {
        return eisRouteRepo.findByActiveTrue();
    }


}
