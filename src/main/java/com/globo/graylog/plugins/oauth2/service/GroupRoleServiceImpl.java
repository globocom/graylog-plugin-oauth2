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
import org.graylog2.security.AccessToken;
import org.graylog2.security.AccessTokenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class GroupRoleServiceImpl extends PersistedServiceImpl implements GroupRoleService {
    private static final Logger LOG = LoggerFactory.getLogger(GroupRoleServiceImpl.class);

    @Inject
    public GroupRoleServiceImpl(MongoConnection mongoConnection) {
        super(mongoConnection);
    }

    @Override
    public GroupRole load(GroupRole groupRole) {
        DBObject query = new BasicDBObject();
        query.put(AccessTokenImpl.TOKEN, groupRole);
        final List<DBObject> objects = query(AccessTokenImpl.class, query);

        if (objects.isEmpty()) {
            return null;
        }
        if (objects.size() > 1) {
            LOG.error("Multiple access tokens found, this is a serious bug.");
            throw new IllegalStateException("Access tokens collection has no unique index!");
        }
        final DBObject tokenObject = objects.get(0);
        final Object id = tokenObject.get("_id");
        return null;
    }

    @Override
    public List<GroupRole> loadAll(GroupRole groupRoles) {
        DBObject query = new BasicDBObject();
        query.put(AccessTokenImpl.USERNAME, groupRoles);
        final List<DBObject> objects = query(AccessTokenImpl.class, query);
        List<AccessToken> tokens = Lists.newArrayList();
        for (DBObject tokenObject : objects) {
            final Object id = tokenObject.get("_id");
            final AccessToken accessToken = new AccessTokenImpl((ObjectId) id, tokenObject.toMap());
            tokens.add(accessToken);
        }
        return null;
    }

    @Override
    public GroupRole save(GroupRole groupRole){
        // make sure we cannot overwrite an existing access token
        collection(AccessTokenImpl.class).createIndex(new BasicDBObject(AccessTokenImpl.TOKEN, 1), new BasicDBObject("unique", true));
        return null;
    }
}
