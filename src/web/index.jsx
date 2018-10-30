import webpackEntry from 'webpack-entry';

import packageJson from '../../package.json';
import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';
import GloboAuthConfiguration from 'GloboAuthConfiguration';

PluginStore.register(new PluginManifest(packageJson, {
  authenticatorConfigurations: [
    {
      name: 'globo-oauth',
      displayName: 'Globo OAuth',
      description: 'authenticates users based on Oauth 2.0 with backstage',
      canBeDisabled: true,
      component: GloboAuthConfiguration,
    },
  ]
}));