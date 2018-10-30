import React from "react";
import Reflux from "reflux";
import { Row, Col, Button, Alert } from "react-bootstrap";
import { Input } from 'components/bootstrap';

import { PageHeader, Spinner } from "components/common";
import GloboAuthActions from "GloboAuthActions";
import GloboAuthStore from "GloboAuthStore";

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

   _saveSettings(ev) {
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
    if (!this.state.config) {
          content = <Spinner />;
    } else {
        content = (
           <Row>
             <Col lg={8}>
                <form id="oauth-config-form" className="form-horizontal" onSubmit={this._saveSettings}>
                     <fieldset>
                        <legend className="col-sm-12">Oauth configuration</legend>
                        <Input type="text" id="name" name="name" labelClassName="col-sm-3"
                            wrapperClassName="col-sm-9" placeholder="Name" label="Name"
                            value={this.state.config.name} help="Application name"
                            onChange={this._bindValue} required/>
                     </fieldset>
                     <fieldset>
                         <Input type="text" id="client_id" name="client_id" labelClassName="col-sm-3"
                             wrapperClassName="col-sm-9" placeholder="Client id" label="Client Id"
                             value={this.state.config.client_id} help="Client Id"
                             onChange={this._bindValue} required/>
                     </fieldset>
                     <fieldset>
                          <Input type="text" id="client_secret" name="client_secret" labelClassName="col-sm-3"
                              wrapperClassName="col-sm-9" placeholder="Client Secret" label="Client Secret"
                              value={this.state.config.client_secret} help="Client Secret"
                              onChange={this._bindValue} required/>
                     </fieldset>
                     <fieldset>
                         <legend className="col-sm-12">User creation</legend>
                         <Input type="checkbox" label="Automatically create users"
                                help="Enable this if Graylog should automatically create a user account for externally authenticated users. If disabled, an administrator needs to manually create a user account."
                                wrapperClassName="col-sm-offset-3 col-sm-9"
                                checked={this.state.config.auto_create_user}
                                name="auto_create_user"
                                onChange={this._bindChecked}/>
                     </fieldset>
                     <fieldset>
                        <legend className="col-sm-12">Oauth settings</legend>
                        <div className="form-group">
                          <Col sm={9} smOffset={3}>
                            <Button type="submit" bsStyle="success">Save Oauth settings</Button>
                          </Col>
                        </div>
                      </fieldset>
                </form>
             </Col>
           </Row>
        );
    }

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