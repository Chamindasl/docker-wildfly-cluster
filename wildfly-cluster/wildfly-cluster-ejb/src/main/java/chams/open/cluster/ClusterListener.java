/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chams.open.cluster;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.clustering.group.Group.Listener;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;

@Singleton
@Startup
public class ClusterListener implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(ClusterListener.class);

    @Resource(lookup = "java:jboss/clustering/group/ejb")
    private Group channelGroup;

    @EJB
    private ClusterMgr clusterMgr;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void createrListener() {
        channelGroup.addListener(this);
        logger.info("Registered topology listener");
        clusterMgr.setMaster(channelGroup.isCoordinator());
    }

    @Override
    public void membershipChanged(final List<Node> previousMembers, final List<Node> members, final boolean merged) {
        logger.info("Channel Group : Co-ordinator Node : {} ", channelGroup.getCoordinatorNode().getName());
        clusterMgr.setMaster(channelGroup.isCoordinator());
    }

}