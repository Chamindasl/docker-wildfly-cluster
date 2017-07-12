package chams.open.cluster;

import javax.ejb.Singleton;

/**
 * Created by chams on 03/07/17.
 */
@Singleton
public class ClusterMgr {

    private boolean master;

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
