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

const OAuth2Actions = Reflux.createActions({
  config: { asyncResult: true },
  saveConfig: { asyncResult: true },
  groups: { asyncResult: true },
  saveGroup: { asyncResult: true },
  deleteGroup: { asyncResult: true },
});

export default OAuth2Actions;