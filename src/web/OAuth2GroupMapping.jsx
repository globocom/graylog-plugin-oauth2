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

import React from "react";
import Reflux from "reflux";
import { Row, Col, Button, Alert } from "react-bootstrap";
import { Input } from "components/bootstrap";

import { DocumentTitle, PageHeader, Spinner } from "components/common";

import StoreProvider from "injection/StoreProvider";
const RolesStore = StoreProvider.getStore("Roles");

import ObjectUtils from "util/ObjectUtils";

const OAuth2GroupMapping = React.createClass({


     componentDidMount() {
          this.setState({ form: { group: "", role: "Reader"} })
          RolesStore.loadRoles().done((roles) => {
            this.setState({ roles: roles.map((role) => role.name) });
          });
     },

     _add(ev) {
        ev.preventDefault();
        console.log(this.state.form);
     },

     _setSetting(attribute, value) {
           const newState = {};

           const form = ObjectUtils.clone(this.state.form);
           form[attribute] = value;
           newState.form = form;
           this.setState(newState);
     },

     _bindValue(ev) {
           this._setSetting(ev.target.name, ev.target.value);
     },

    render() {
        let content;
        if (!this.state) {
          content = <Spinner />;
        } else {
            const roles = this.state.roles.map((role) => <option key={"default-group-" + role} value={role}>{role}</option>);
            content = (
                <Row>
                        <form id="oauth-config-form" className="form-horizontal" onSubmit={this._add}>
                        <fieldset>
                            <legend className="col-sm-12">2. Group Mapping</legend>
                            <Col sm={6}>
                            <Input type="text" id="group" name="group" labelClassName="col-sm-3"
                             wrapperClassName="col-sm-9" label="Group Name"
                             value={this.state.form.group} onChange={this._bindValue} required/>
                            </Col>
                            <Col sm={5}>
                            <Input id="role" labelClassName="col-sm-3" wrapperClassName="col-sm-9" label="Role">
                               <Row>
                                 <Col sm={6}>
                                   <select id="role" name="role" className="form-control" value={this.state.form.role}
                                     onChange={this._bindValue} required>
                                    {roles}
                                   </select>
                                 </Col>
                               </Row>
                            </Input>
                            </Col>
                             <Col>
                                <Button type="submit" bsStyle="success">Add</Button>
                             </Col>
                        </fieldset>
                        </form>
                </Row>
            )
        }

        return (
            content
        );
    }
});

export default OAuth2GroupMapping;
