package com.globo.graylog.plugins.oauth2;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

/**
 * Implement the Plugin interface here.
 */
public class OAuth2Plugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new OAuth2MetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new OAuth2Module());
    }
}
