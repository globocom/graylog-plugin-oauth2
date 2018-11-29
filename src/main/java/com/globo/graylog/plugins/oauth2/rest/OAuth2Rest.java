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

package com.globo.graylog.plugins.oauth2.rest;

import com.globo.graylog.plugins.oauth2.audit.OAuth2AuditEventTypes;
import com.globo.graylog.plugins.oauth2.permissions.OAuth2Permissions;
import com.globo.graylog.plugins.oauth2.service.GroupRoleImpl;
import com.globo.graylog.plugins.oauth2.service.GroupRoleInterface;
import com.globo.graylog.plugins.oauth2.service.GroupRoleService;
import com.globo.graylog.plugins.oauth2.service.GroupRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.graylog2.audit.jersey.AuditEvent;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Api(value = "OAuth", description = "Manage OAuth")
@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
@RequiresAuthentication
public class OAuth2Rest extends RestResource implements PluginRestResource {

    private final ClusterConfigService clusterConfigService;
    private final GroupRoleService groupRoleServiceImpl;

    @Inject
    private OAuth2Rest(ClusterConfigService clusterConfigService, GroupRoleServiceImpl groupRoleServiceImpl) {
        this.clusterConfigService = clusterConfigService;
        this.groupRoleServiceImpl = groupRoleServiceImpl;
    }

    @ApiOperation(value = "Get Oauth configuration")
    @GET
    @RequiresPermissions(OAuth2Permissions.CONFIG_READ)
    public OAuth2Config get() {
        final OAuth2Config config = clusterConfigService.getOrDefault(OAuth2Config.class,
                OAuth2Config.defaultConfig());

        return config.toBuilder().build();
    }

    @ApiOperation(value = "Update Oauth configuration")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @RequiresPermissions(OAuth2Permissions.CONFIG_UPDATE)
    @AuditEvent(type = OAuth2AuditEventTypes.CONFIG_UPDATE)
    public OAuth2Config update(@ApiParam(name = "config", required = true) @NotNull OAuth2Config config) {
        final OAuth2Config cleanConfig = config.toBuilder().build();
        clusterConfigService.write(cleanConfig);

        return config;
    }

    @ApiOperation(value = "Get all groups")
    @GET
    @RequiresPermissions(OAuth2Permissions.CONFIG_READ)
    @Path("/group")
    public List<GroupRoleInterface> getGroups() {
        List<GroupRoleInterface> groups = groupRoleServiceImpl.loadAll();

        return groups;
    }

    @ApiOperation(value = "Saving group")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @RequiresPermissions(OAuth2Permissions.CONFIG_UPDATE)
    @AuditEvent(type = OAuth2AuditEventTypes.CONFIG_UPDATE)
    @Path("/group")
    public GroupRole postGroup(@ApiParam(name = "group", required = true) @NotNull GroupRole group) {
        final GroupRole cleanGroup = group.toBuilder().build();

        Map<String, Object> fields = new HashMap<>();
        fields.put("group", cleanGroup.group());
        fields.put("role", cleanGroup.role());


        GroupRoleImpl groupRole= new GroupRoleImpl(fields);

        try{
            groupRoleServiceImpl.save(groupRole);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        return group;
    }

    @ApiOperation(value = "Removing group")
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @RequiresPermissions(OAuth2Permissions.CONFIG_UPDATE)
    @AuditEvent(type = OAuth2AuditEventTypes.CONFIG_UPDATE)
    @Path("/group")
    public void removeGroup(@QueryParam("group") @NotNull String group) {
        groupRoleServiceImpl.remove(group);
    }
}
