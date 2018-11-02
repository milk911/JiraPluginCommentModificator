package com.sgc.logic;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.comment.CommentEvent;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.MutableComment;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentListener implements InitializingBean, DisposableBean {

    @JiraImport
    private final EventPublisher eventPublisher;

    @Autowired
    public CommentListener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
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
    public void CommentCreatedEvent(CommentEvent commentEvent) {
        Comment currentComment = commentEvent.getComment();
        //System.out.println("COMMMMMMMMMMMENT!!!");
        //System.out.println(commentEvent.getComment().getBody());
        CommentManager commentManager = ComponentAccessor.getCommentManager();
        if (currentComment != null) {
            MutableComment mutableComment = commentManager.getMutableComment(currentComment.getId());
            mutableComment.setBody(mutableComment.getBody() + "\n" + "My SUPER update");
            commentManager.update(mutableComment, false);
        }
    }

}