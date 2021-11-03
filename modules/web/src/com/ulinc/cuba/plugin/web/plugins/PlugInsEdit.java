package com.ulinc.cuba.plugin.web.plugins;

import com.haulmont.cuba.gui.screen.*;
import com.ulinc.cuba.plugin.entity.PlugIns;

@UiController("rtneo_PlugIns.edit")
@UiDescriptor("plug-ins-edit.xml")
@EditedEntityContainer("plugInsDc")
@LoadDataBeforeShow
public class PlugInsEdit extends StandardEditor<PlugIns> {
}