import React from "react";
import Reflux from "reflux";
import { Row, Col, Button, Alert } from "react-bootstrap";
import { Input } from 'components/bootstrap';

import { PageHeader, Spinner } from "components/common";
import GloboAuthActions from "./GloboAuthActions";
import GloboAuthStore from "./GloboAuthStore";

import StoreProvider from 'injection/StoreProvider';
const RolesStore = StoreProvider.getStore('Roles')

import ObjectUtils from 'util/ObjectUtils';

const GloboAuthConfiguration = React.createClass({

   mixins: [
      Reflux.connect(GloboAuthStore),
    ],

    componentDidMount() {
      GloboAuthActions.config();
      RolesStore.loadRoles().done(roles => {
        this.setState({ roles: roles.map(role => role.name) });
      });
    },

    saveSettings(ev) {
        ev.preventDefault();
        GloboAuthActions.saveConfig(this.state.config);
  },

   _setSetting(attribute, value) {
      const newState = {};

      const settings = ObjectUtils.clone(this.state.config);
      settings[attribute] = value;
      newState.config = settings;
      this.setState(newState);
    },

   _bindChecked(ev, value) {
       this._setSetting(ev.target.name, typeof value === 'undefined' ? ev.target.checked : value);
   },

   _bindValue(ev) {
      this._setSetting(ev.target.name, ev.target.value);
   },

  render() {
    let content;
    content = (
      <h1>teste</h1>
    );

    return (
      <div>
        <PageHeader title="Globo Oauth" subpage>
          <span>Configuration page for the Oauth.</span>
        </PageHeader>
        {content}
      </div>
    );
  }


});

export default GloboAuthConfiguration;