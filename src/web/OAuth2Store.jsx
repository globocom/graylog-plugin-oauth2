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

import Reflux from "reflux";

import OAuth2Actions from "OAuth2Actions";

import UserNotification from "util/UserNotification";
import URLUtils from "util/URLUtils";
import fetch, { Builder } from "logic/rest/FetchProvider";

const urlPrefix = "/plugins/com.globo.graylog.plugins.oauth2";

const OAuth2Store = Reflux.createStore({
  listenables: [OAuth2Actions],

  getInitialState() {
    return {};
  },

  _errorHandler(message, title, cb) {
    return error => {
      let errorMessage;
      try {
        errorMessage = error.additional.body.message;
      } catch (e) {
        errorMessage = error.message;
      }
      UserNotification.error(`${message}: ${errorMessage}`, title);
      if (cb) {
        cb(error);
      }
    };
  },

  _url(path) {
    return URLUtils.qualifyUrl(`${urlPrefix}${path}`);
  },

  config() {
    const promise = fetch("GET", this._url("/oauth"));

    promise.then(response => {
      this.trigger({ config: response });
    }, this._errorHandler("Fetching config failed", "Could not retrieve Oauth2"));

    OAuth2Actions.config.promise(promise);
  },

  saveConfig(config) {
    const promise = new Builder("PUT", this._url("/oauth"))
      .authenticated()
      .setHeader("X-Requested-By", this._url("/oauth"))
      .json(config)
      .build();

    promise.then(response => {
      this.trigger({ config: response });
      UserNotification.success("Oauth2 configuration was updated successfully");
    }, this._errorHandler("Updating Oauth2 config failed", "Unable to update Oauth2 authenticator config"));

    OAuth2Actions.saveConfig.promise(promise);
  },

  groups() {
    const promise = fetch("GET", this._url("/oauth/group"));

    promise.then(response => {
      this.trigger({ groups: response });
    }, this._errorHandler("Fetching groups failed", "Could not groups"));

    OAuth2Actions.groups.promise(promise);
  },

  saveGroup(group) {
    const promise = new Builder("POST", this._url("/oauth/group"))
      .authenticated()
      .setHeader("X-Requested-By", this._url("/oauth"))
      .json(group)
      .build();

    promise.then(response => {
      this.trigger({ group: response });
      UserNotification.success("Group was saved successfully");
    }, this._errorHandler("Savinf group failed", "Unable to save group"));

    OAuth2Actions.saveGroup.promise(promise);
  },

  deleteGroup(group) {
    const promise = new Builder(
      "DELETE",
      this._url("/oauth/group?group=" + group)
    )
      .authenticated
      .setHeader("X-Requested-By", this._url("/oauth"))
      .json()
      .build();

    promise.then(response => {
      this.trigger({ group: response });
      UserNotification.success("Group was removed successfully");
    }, this._errorHandler("Removing group failed", "Unable to remove group"));

    OAuth2Actions.deleteGroup.promise(promise);
  }
});

export default OAuth2Store;
