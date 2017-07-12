package chams.open.cluster.jms;

import chams.open.cluster.ClusterMgr;
import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import java.util.Date;

/**
 * Created by chams on 03/07/17.
 */
@Singleton
@Startup
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Resource(mappedName = "java:/jms/queue/jobQueue")
    private Queue queueExample;


    @Resource(lookup = "java:jboss/infinispan/jobSentCache")
    private Cache<String, Long> jobSentCache;

    @Inject
    private JMSContext context;

    @EJB
    private ClusterMgr clusterMgr;


    @PostConstruct
    public void startNotClustered() {
//        try {
//            this.jobSentCache = this.container.getCache();
//        } catch (final Exception ex) {
//            logger.error("unable to get the cache");
//        }
    }
    public void sendMessage() {
        try {
            for (int i = 0; i <10; i++) {
                context.createProducer().send(queueExample, "" + new Date().getTime());
            }
            final Long current;
            if (jobSentCache.get("id") != null) {
                current = jobSentCache.get("id");
            } else {
                current = 0l;
            }
            jobSentCache.put("id", current + 10);
            logger.info("Sent 10 messages, Total : {} ", jobSentCache.get("id"));
        } catch (Exception exc) {
            logger.error("error ", exc);
        }

    }

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void automaticTimeout() {
        if (clusterMgr.isMaster()) {
            sendMessage();
        }
    }
}