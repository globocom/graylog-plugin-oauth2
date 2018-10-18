import Reflux from 'reflux';

import GloboAuthActions from './GloboAuthActions';

import UserNotification from 'util/UserNotification';
import URLUtils from 'util/URLUtils';
import fetch from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/com.globo';

const GloboAuthStore = Reflux.createStore({
  listenables: [GloboAuthActions],

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
    const promise = fetch('GET', this._url('/config'));

    promise.then((response) => {
      this.trigger({ config: response });
    }, this._errorHandler('Fetching config failed', 'Could not retrieve Globo Oauth'));

    GloboAuthActions.config.promise(promise);
  },

  saveConfig(config) {
    const promise = fetch('PUT', this._url('/config'), config);

    promise.then((response) => {
      this.trigger({ config: response });
      UserNotification.success('Globo Oauth configuration was updated successfully');
    }, this._errorHandler('Updating Globo Oauth config failed', 'Unable to update Oauth authenticator config'));

     GloboAuthActions.saveConfig.promise(promise);
  },
});

export default GloboAuthStore;