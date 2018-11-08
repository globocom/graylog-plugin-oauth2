import Reflux from 'reflux';

import OAuth2Actions from 'OAuth2Actions';

import UserNotification from 'util/UserNotification';
import URLUtils from 'util/URLUtils';
import fetch from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/com.globo.graylog.plugins.oauth2';

const OAuth2Store = Reflux.createStore({
  listenables: [OAuth2Actions],

  getInitialState() {
    return {
      config: undefined,
    };
  },

  _errorHandler(message, title, cb) {
    return (error) => {
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
    const promise = fetch('GET', this._url('/oauth'));

    promise.then((response) => {
      this.trigger({ config: response });
    }, this._errorHandler('Fetching config failed', 'Could not retrieve Oauth2'));

    OAuth2Actions.config.promise(promise);
  },

  saveConfig(config) {
    const promise = fetch('PUT', this._url('/oauth'), config);

    promise.then((response) => {
      this.trigger({ config: response });
      UserNotification.success('Oauth2 configuration was updated successfully');
    }, this._errorHandler('Updating Oauth2 config failed', 'Unable to update Oauth2 authenticator config'));

     OAuth2Actions.saveConfig.promise(promise);
  },
});

export default OAuth2Store;