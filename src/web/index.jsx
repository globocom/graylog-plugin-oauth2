/*
 * This file is part of Graylog Plugin Oauth.
 *
 * Graylog Plugin Oauth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Graylog Plugin Oauth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>
 */

import packageJson from '../../package.json';
import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';
import OAuth2Configuration from "OAuth2Configuration";

PluginStore.register(new PluginManifest(packageJson, {
  authenticatorConfigurations: [
    {
      name: "oauth2",
      displayName: "OAuth2",
      description: "authenticates users based on Oauth 2.0",
      canBeDisabled: true,
      component: OAuth2Configuration,
    },
  ]
}));