package com.sgc.logic;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Aliases
{
    @XmlElement
    private String alias;

    @XmlElement
    private String text;

    public List<Aliases>

    private Aliases() { }

    public Aliases(String alias, String text)
    {
        this.alias = alias;
        this.text = text;
    }

}