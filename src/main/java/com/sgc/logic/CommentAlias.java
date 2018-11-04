package com.sgc.logic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommentAlias
{
    @XmlElement
    private String alias;
    @XmlElement
    private String text;
    @XmlElement
    private int    id;

    private CommentAlias() { }

    public CommentAlias(String alias, String text, int id) {
        this.alias = alias;
        this.text = text;
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return alias + " " + text + " " + id;
    }
}