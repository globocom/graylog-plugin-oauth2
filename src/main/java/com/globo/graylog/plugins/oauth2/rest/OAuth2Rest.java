package com.globo.graylog.plugins.oauth2.rest;

import com.globo.graylog.plugins.oauth2.audit.OAuth2AuditEventTypes;
import com.globo.graylog.plugins.oauth2.permissions.OAuth2Permissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.graylog2.audit.jersey.AuditEvent;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api(value = "Globo/oauth", description = "Manage Globo Oauth")
@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
@RequiresAuthentication
public class OAuth2Rest extends RestResource implements PluginRestResource {

    private final ClusterConfigService clusterConfigService;

    @Inject
    private OAuth2Rest(ClusterConfigService clusterConfigService) {
        this.clusterConfigService = clusterConfigService;
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
}
