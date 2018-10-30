import Reflux from 'reflux';

const GloboAuthActions = Reflux.createActions({
  config: { asyncResult: true },
  saveConfig: { asyncResult: true },
});

export default GloboAuthActions;