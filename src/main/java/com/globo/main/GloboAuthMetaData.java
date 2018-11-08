package com.globo.main;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class GloboAuthMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "com.globo.graylog-plugin-oauth2/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return "com.globo.main.GloboAuthPlugin";
    }

    @Override
    public String getName() {
        return "GloboAuth";
    }

    @Override
    public String getAuthor() {
        return "Matheus Da Luz Costa <matheus.costa@corp.globo.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://gitlab.globoi.com/time-evolucao-infra/com.globo.graylog-plugin-oauth2");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "Description of GloboAuth plugin";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
