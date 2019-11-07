package com.itzap.ebeans;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.Maps;
import com.itzap.common.AnyConfig;
import com.itzap.common.exception.IZapException;
import com.itzap.config.ConfigBuilder;
import com.itzap.config.ConfigType;
import com.itzap.ebeans.execptions.EBeanErrors;
import com.itzap.ebeans.model.ServerProperties;
import com.typesafe.config.ConfigException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.avaje.datasource.DataSourceConfig;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class Servers {
    private static final AnyConfig CONFIG = ConfigBuilder.builder(ConfigType.TYPE_SAFE)
            .build();
    private static final Map<String, Server> SERVERS = Maps.newConcurrentMap();
    private static final Map<String, DataSource> DATA_SOURCES = Maps.newConcurrentMap();

    public static class Server {
        private final EbeanServer server;
        private final ServerConfig config;


        public Server(EbeanServer server, ServerConfig config) {
            this.server = server;
            this.config = config;
        }

        public EbeanServer getServer() {
            return server;
        }

        public ServerConfig getConfig() {
            return config;
        }
    }

    public static Server get(String serverName, AnyConfig config) {
        return SERVERS.computeIfAbsent(serverName,
                k -> loadServer(k, config));
    }

    public static Server get(String serverName) {
        return get(serverName, CONFIG.getConfig("srv"));
    }

    public static DataSource getDataSource(String serverName, AnyConfig config) {
        return DATA_SOURCES.computeIfAbsent(serverName,
                k -> loadDataSource(k, config));
    }

    public static DataSource getDataSource(String serverName) {
        return getDataSource(serverName, CONFIG.getConfig("srv"));
    }

    private static Server loadServer(String serverName, AnyConfig srv) {
        AnyConfig serverConf = srv.getConfig(serverName);
        if (serverConf == null) {
            throw new IZapException(String.format("Server initialization for server=%s failed. Missing config", serverName),
                    EBeanErrors.SQL_SERVER_INIT);
        }
        if (srv.hasProperty(ServerProperties.DEFAULT)) {
            serverConf = serverConf
                    .withFallback(srv.getConfig(ServerProperties.DEFAULT));
        }

        ServerConfig serverConfig = new ServerConfig();
        DataSourceConfig sourceConfig = new DataSourceConfig();

        try {
            if (serverConf.hasProperty(ServerProperties.DRIVER)) {
                sourceConfig.setDriver(serverConf.getString(ServerProperties.DRIVER));
            }
            if (serverConf.hasProperty(ServerProperties.USER)) {
                sourceConfig.setUsername(serverConf.getString(ServerProperties.USER));
            }
            if (serverConf.hasProperty(ServerProperties.USER_NAME)) {
                sourceConfig.setUsername(serverConf.getString(ServerProperties.USER_NAME));
            }
            sourceConfig.setPassword(serverConf.getString(ServerProperties.PASSWORD));
            sourceConfig.setUrl(serverConf.getString(ServerProperties.URL));

            serverConfig.setName(serverName);
            serverConfig.setDataSourceConfig(sourceConfig);
            if (serverConf.hasProperty(ServerProperties.IS_DEFAULT)) {
                serverConfig.setDefaultServer(serverConf.getBool(ServerProperties.IS_DEFAULT));
            }
            if (serverConf.hasProperty(ServerProperties.PACKAGES)) {
                List<String> packages = serverConf.getList(ServerProperties.PACKAGES);
                for (String packageName: packages) {
                    serverConfig.addPackage(packageName.replace(".**", ""));
                }
            }

            if (serverConf.hasProperty(ServerProperties.AUTO_DDL) &&
                    serverConf.getBool(ServerProperties.AUTO_DDL)) {
                serverConfig.setDdlGenerate(true);
                serverConfig.setDdlRun(true);
            }

            return new Server(EbeanServerFactory.create(serverConfig), serverConfig);

        } catch (ConfigException configEx) {
            throw new IZapException(String.format("Failed to initialize SQL server=%s",
                    serverName), EBeanErrors.SQL_SERVER_INIT, configEx);
        }
    }

    public static DataSource loadDataSource(String serverName, AnyConfig srv) {
        AnyConfig serverConf = srv.getConfig(serverName);
        if (serverConf == null) {
            throw new IZapException(String.format("DataSource initialization for server=%s failed. Missing config", serverName),
                    EBeanErrors.SQL_SERVER_INIT);
        }

        HikariConfig dsConfig = new HikariConfig();
        dsConfig.setJdbcUrl(serverConf.getString(ServerProperties.URL));
        if (serverConf.hasProperty(ServerProperties.USER_NAME)) {
            dsConfig.setUsername(serverConf.getString(ServerProperties.USER_NAME));
        }
        dsConfig.setPassword(serverConf.getString(ServerProperties.PASSWORD));
        if (serverConf.hasProperty(ServerProperties.DRIVER)) {
            dsConfig.setDriverClassName(serverConf.getString(ServerProperties.DRIVER));
        }

        return new HikariDataSource(dsConfig);
    }
}
