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

import { DocumentTitle, PageHeader, Spinner, DataTable} from "components/common";

import OAuth2Actions from "OAuth2Actions";
import OAuth2Store from "OAuth2Store";

import StoreProvider from "injection/StoreProvider";
const RolesStore = StoreProvider.getStore("Roles");

import ObjectUtils from "util/ObjectUtils";

const OAuth2GroupMapping = React.createClass({

     mixins: [
           Reflux.connect(OAuth2Store),
    ],

     componentDidMount() {

          OAuth2Actions.groups();
          this.result = [];
          this.setState({ form: { group: "", role: "Reader"}, roles: [], result: []});
          RolesStore.loadRoles().done((roles) => {
            this.setState({ roles: roles.map((role) => role.name) });
          });
     },

     _add(ev) {
        ev.preventDefault();
        OAuth2Actions.saveGroup(this.state.form);
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

     _headerCellFormatter(header) {
         const className = (header === 'Actions' ? 'actions' : '');
         return <th className={className}>{header}</th>;
     },

      _editButton(group) {
           return (<Button key="edit" bsSize="xsmall" bsStyle="info" onClick={() => this._showEditRole(role)} title="Edit group">Edit</Button>);
       },

      _deleteButton(group) {
           return (<Button key="delete" bsSize="xsmall" bsStyle="primary" onClick={() => this._deleteGroup(group)} title="Delete group">Delete</Button>);
      },

      _reload() {
      },

      _deleteGroup(group) {
          if (window.confirm(`Do you really want to delete group ${group}?`)) {
                OAuth2Actions.deleteGroup(group).then(this._reload);
          }
      },
      _roleInfoFormatter(group) {
          return (
            <tr key={group.group}>
              <td>{group.group}</td>
              <td className="limited">{group.role}</td>
              <td>
                {this._editButton(group.group)}
                <span key="space">&nbsp;</span>
                {this._deleteButton(group.group)}
              </td>
            </tr>
          );
      },

    render() {
        let content;
        if (!this.state.groups) {
          content = <Spinner />;
        } else {
            const headers = ['Group', 'Role', 'Actions'];
            const roles = this.state.roles.map((role) => <option key={"default-group-" + role} value={role}>{role}</option>);
            content = (
                <Row>
                        <fieldset>
                            <form id="oauth-config-form" className="form-horizontal" onSubmit={this._add}>
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
                            </form>
                        </fieldset>
                        <fieldset>
                              <div className="form-group">
                                   <DataTable id="role-list"
                                               className="table-hover"
                                               headers={headers}
                                               headerCellFormatter={this._headerCellFormatter}
                                               sortByKey={'group'}
                                               rows={this.state.groups}
                                               filterBy="Group"
                                               dataRowFormatter={this._roleInfoFormatter}
                                               filterLabel="Filter"
                                               filterKeys={[]}/>
                               </div>
                        </fieldset>
                </Row>
            )
        }

        return (
            content
        );
    }
});

export default OAuth2GroupMapping;
