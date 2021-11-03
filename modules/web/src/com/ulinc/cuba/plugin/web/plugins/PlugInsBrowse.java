package com.ulinc.cuba.plugin.web.plugins;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.ulinc.cuba.plugin.entity.PlugIns;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UiController("rtneo_PlugIns.browse")
@UiDescriptor("plug-ins-browse.xml")
@LookupComponent("plugInsesTable")
@LoadDataBeforeShow
public class PlugInsBrowse extends StandardLookup<PlugIns> {
    @Inject
    private GroupTable<PlugIns> plugInsesTable;

    @Inject
    private Screens screens;

    @Inject
    UiComponents uiComponents;

    @Inject
    private Notifications notifications;

    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private DataManager dataManager;


    @Subscribe("runBtn")
    public void onRunButtonClick(Button.ClickEvent event) {

        PlugIns ne = plugInsesTable.getSingleSelected();

        if (ne != null)
            try {
                nonReflectionsScan(ne);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

    }

    private void nonReflectionsScan(PlugIns ne) throws IOException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

        String path = ne.getPath();
        String classToLoad = ne.getClassName();
        String formClass = ne.getFormClass();
        Screen screen = null;

        File file = new File(path);
        URL url = file.toURI().toURL();

        Method method1;

        URL jarUrl = new URL("jar", "", "file:" + file.getAbsolutePath() + "!/");
        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

        Thread.currentThread().setContextClassLoader(classLoader);

        Class loadedClass = classLoader.loadClass(classToLoad);
        Object obj = loadedClass.getDeclaredConstructor().newInstance();

        if (ne.getOpenForm()) {
            Class cl = this.getClass().getClassLoader().loadClass(formClass);
            screen = screens.create(cl);
        }

        method1 = loadedClass.getMethod("execute", AppContext.class, DataManager.class, UiComponents.class,
                Notifications.class, Screen.class, ScreenBuilders.class);
        String res1 = (String) method1.invoke(obj, null, dataManager, uiComponents, notifications, screen, screenBuilders);

        screens.show(screen);
        classLoader.close();

    }

    public List<Class<?>> scanClasses() throws IOException {

        String CLASS_FILE_SUFFIX = ".class";
        char PKG_SEPARATOR = '.';
        char DIR_SEPARATOR = '/';

        String packageName = getClass().getPackage().getName();
        String path = packageName.replace(".", "/");

        Enumeration<URL> en = getClass().getClassLoader().getResources(path);//"META-INF");

        List<Class<?>> classes = new ArrayList<>();

        while (en.hasMoreElements()) {

            URL url = en.nextElement();
            JarURLConnection urlcon = (JarURLConnection) (url.openConnection());

            try (JarFile jar = urlcon.getJarFile();) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {

                    JarEntry nextEl = entries.nextElement();
                    String entry = nextEl.getName();
                    System.out.println(entry);

                    if (entry.endsWith(".class")) {

                        int endIndex = entry.length() - CLASS_FILE_SUFFIX.length();
                        String className = entry.substring(0, endIndex);
                        String scannedPath = className.replace(DIR_SEPARATOR, PKG_SEPARATOR);
                        try {
                            Class cl = Class.forName(scannedPath);
                            classes.add(cl);
                        } catch (ClassNotFoundException ignore) {
                        }
                    }

                }
            }

        }

        return classes;

    }
}