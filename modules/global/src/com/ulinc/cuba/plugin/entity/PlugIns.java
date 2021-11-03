package com.ulinc.cuba.plugin.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "RTNEO_PLUG_INS")
@Entity(name = "rtneo_PlugIns")
@NamePattern("%s|name")
public class PlugIns extends StandardEntity {
    private static final long serialVersionUID = -868120403987852718L;

    @Column(name = "NAME", unique = true, length = 128)
    private String name;

    @Column(name = "FORM_CLASS")
    private String formClass;

    @Column(name = "CLASS_NAME")
    private String className;

    @Column(name = "PATH", length = 512)
    private String path;

    @Column(name = "COMMAND", length = 128)
    private String command;

    @Column(name = "OPEN_FORM")
    private Boolean openForm;

    @Column(name = "RUN_BEAN")
    private Boolean runBean;

    public String getFormClass() {
        return formClass;
    }

    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getRunBean() {
        return runBean;
    }

    public void setRunBean(Boolean runBean) {
        this.runBean = runBean;
    }

    public Boolean getOpenForm() {
        return openForm;
    }

    public void setOpenForm(Boolean openForm) {
        this.openForm = openForm;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}