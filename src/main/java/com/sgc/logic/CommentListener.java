package com.sgc.logic;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.comment.CommentEvent;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.MutableComment;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CommentListener implements InitializingBean, DisposableBean {

    @JiraImport
    private final EventPublisher eventPublisher;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Autowired
    public CommentListener(EventPublisher eventPublisher, PluginSettingsFactory pluginSettingsFactory) {
        this.eventPublisher = eventPublisher;
        this.pluginSettingsFactory = pluginSettingsFactory;
        PluginConfiguration.initPluginConfiguration(pluginSettingsFactory);
    }

    /**
     * Called when the plugin has been enabled.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    /**
     * Called when the plugin is being disabled or removed.
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void commentCreatedEvent(CommentEvent commentEvent) {
        Comment currentComment = commentEvent.getComment();
        modificateComment(currentComment);
    }

    public static void modificateComment(Comment currentComment) {
        if (currentComment == null)
            return;

        List<CommentAlias> aliases = PluginConfiguration.getAliases();
        if (aliases.isEmpty())
            return;

        sortAliasesByLength(aliases);

        String commentBody = currentComment.getBody();

        for (int i = 0; i < aliases.size(); i++) {
            commentBody = commentBody.replaceAll(aliases.get(i).getAlias(), aliases.get(i).getText());
        }

        CommentManager commentManager = ComponentAccessor.getCommentManager();

        MutableComment mutableComment = commentManager.getMutableComment(currentComment.getId());
        mutableComment.setBody(commentBody);
        commentManager.update(mutableComment, false);

    }

    public static void sortAliasesByLength(List<CommentAlias> aliases) {
        Collections.sort(aliases, (CommentAlias object1, CommentAlias object2) -> (object2.getAlias().length() - object1.getAlias().length()));
    }

}