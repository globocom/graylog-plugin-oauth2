/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.globo.graylog.plugins.oauth2.service;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.graylog2.database.MongoConnection;
import org.graylog2.database.PersistedServiceImpl;
import org.graylog2.plugin.database.ValidationException;

import javax.inject.Inject;
import java.util.List;

public class GroupRoleServiceImpl extends PersistedServiceImpl implements GroupRoleService {

    @Inject
    public GroupRoleServiceImpl(MongoConnection mongoConnection) {
        super(mongoConnection);
    }

    @Override
    public List<GroupRoleInterface> loadAll() {
        DBObject query = new BasicDBObject();
        final List<DBObject> objects = query(GroupRoleImpl.class, query);

        List<GroupRoleInterface> groups = Lists.newArrayList();
        for (DBObject groupObject : objects) {
            final Object id = groupObject.get("_id");
            final GroupRoleImpl group = new GroupRoleImpl((ObjectId) id, groupObject.toMap());
            groups.add(group);
        }

        return groups;
    }

    @Override
    public String save(GroupRoleInterface groupRoleInterface) throws ValidationException {
        collection(GroupRoleImpl.class).createIndex(new BasicDBObject(GroupRoleImpl.GROUP, 1), new BasicDBObject("unique", true));
        return super.save(groupRoleInterface);
    }

    @Override
    public void remove(String group) {
        DBObject query = new BasicDBObject();
        query.put(GroupRoleImpl.GROUP, group);

        destroy(query, "group_role");
    }
}
