package com.globo;

import com.globo.audit.GloboAuthAuditEventTypes;
import com.globo.audit.GloboAuthPermissions;
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
public class GloboAuthRest extends RestResource implements PluginRestResource {

    private final ClusterConfigService clusterConfigService;

    @Inject
    private GloboAuthRest(ClusterConfigService clusterConfigService) {
        this.clusterConfigService = clusterConfigService;
    }

    @ApiOperation(value = "Get Oauth configuration")
    @GET
    @RequiresPermissions(GloboAuthPermissions.CONFIG_READ)
    public GloboAuthConfig get() {
        final GloboAuthConfig config = clusterConfigService.getOrDefault(GloboAuthConfig.class,
                GloboAuthConfig.defaultConfig());

        return config.toBuilder().build();
    }

    @ApiOperation(value = "Update Oauth configuration")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @RequiresPermissions(GloboAuthPermissions.CONFIG_UPDATE)
    @AuditEvent(type = GloboAuthAuditEventTypes.CONFIG_UPDATE)
    public GloboAuthConfig update(@ApiParam(name = "config", required = true) @NotNull GloboAuthConfig config) {
        final GloboAuthConfig cleanConfig = config.toBuilder().build();
        clusterConfigService.write(cleanConfig);

        return config;
    }
}
