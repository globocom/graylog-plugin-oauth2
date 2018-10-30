package com.globo.main;

import com.globo.main.GloboAuthMetaData;
import com.globo.main.GloboAuthModule;
import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

/**
 * Implement the Plugin interface here.
 */
public class GloboAuthPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new GloboAuthMetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new GloboAuthModule());
    }
}
