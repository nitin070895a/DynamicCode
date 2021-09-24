package com.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Compiler {

    void execute(String url) {
        
        try {
            URLClassLoader loader = URLClassLoader.newInstance(new URL[] {new URL(url)}, this.getClass().getClassLoader());
            Class<?> cls = loader.loadClass("com.example.TestCode");

            Object o = cls.getConstructor().newInstance();
            Method m = cls.getMethod("testing");
            m.invoke(o);

//            Method[] m = cls.getMethods();
//            cls.getMethod("TestCode$testing").invoke(null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
