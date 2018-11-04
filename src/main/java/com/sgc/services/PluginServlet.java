package com.sgc.services;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Scanned
public class PluginServlet extends HttpServlet
{
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final LoginUriProvider loginUriProvider;
    @ComponentImport
    private final TemplateRenderer renderer;

    final Logger logger = LoggerFactory.getLogger(PluginServlet.class);

    @Inject
    public PluginServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        UserKey userKey = userManager.getRemoteUser(request).getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey))
        {
            try {
                redirectToLogin(request, response);
            }
            catch (IOException ex) {
                logger.warn("Redirecting error", ex);
            }
            return;
        }

        response.setContentType("text/html;charset=utf-8");

        try {
            renderer.render("aliasConfigPage.vm", response.getWriter());
        }
        catch (IOException ex) {
            logger.warn("Redirecting error", ex);
        }
     }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request)
    {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null)
        {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}
