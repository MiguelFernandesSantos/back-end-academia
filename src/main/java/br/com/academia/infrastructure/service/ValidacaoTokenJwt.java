package br.com.academia.infrastructure.service;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.core.ResourceMethodInvoker;

import java.lang.reflect.Method;

@Provider
public class ValidacaoTokenJwt implements ContainerRequestFilter {

    private static final Response NAO_AUTORIZADO = Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();

    @Inject
    JsonWebToken token;

    @Override
    public void filter(ContainerRequestContext request) {
        ResourceMethodInvoker methodInvoker =
                (ResourceMethodInvoker) request.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        if(method.isAnnotationPresent(PermitAll.class)) return;
        if(token.getRawToken() == null){
            request.abortWith(NAO_AUTORIZADO);
        }
    }

}
