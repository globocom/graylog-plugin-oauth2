import Reflux from 'reflux';

const OAuth2Actions = Reflux.createActions({
  config: { asyncResult: true },
  saveConfig: { asyncResult: true },
});

export default OAuth2Actions;