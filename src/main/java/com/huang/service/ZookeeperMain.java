package com.huang.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

/**
 * Created by h on 2018/6/9.
 */
@Slf4j
public class ZookeeperMain extends QuorumPeerMain {

    private static final String USAGE = "Usage: QuorumPeerMain configfile";

    /**
     * 本文先介绍了zookeeper开源分布式协作系统及其特点、应用场景，然后根据zookeeper的启动方式，找到zookeeper的入口。
     * 在入口方法中，单机启动使用ZooKeeperServerMain，最终调用ZookeeperServer的startup()方法来RequestProcessor；
     * 集群启动时调用QuorumPeer的start方法，接着也是调用ZookeeperServer的startup()方法来RequestProcessor，
     * 最后调用选举算法选出leader。
     *
     */
    private String zkConfigPath;

    public ZookeeperMain(String zkConfigPath) {
        this.zkConfigPath = zkConfigPath;
    }

    public void start() {
        try {
            initializeAndRun(new String[]{zkConfigPath});
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments, exiting abnormally", e);
            log.info(USAGE);
            System.err.println(USAGE);
            System.exit(2);
        } catch (QuorumPeerConfig.ConfigException e) {
            log.error("Invalid config, exiting abnormally", e);
            System.err.println("Invalid config, exiting abnormally");
            System.exit(2);
        } catch (Exception e) {
            log.error("Unexpected exception, exiting abnormally", e);
            System.exit(1);
        }
        log.info("Exiting normally");
        System.exit(0);
    }
}
