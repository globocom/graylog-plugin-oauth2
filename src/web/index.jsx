import webpackEntry from 'webpack-entry';

import packageJson from '../../package.json';
import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';
import OAuth2Configuration from 'OAuth2Configuration';

PluginStore.register(new PluginManifest(packageJson, {
  authenticatorConfigurations: [
    {
      name: 'oauth2',
      displayName: 'OAuth2',
      description: 'authenticates users based on Oauth 2.0 with backstage',
      canBeDisabled: true,
      component: OAuth2Configuration,
    },
  ]
}));