package com.example;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Compiler {

    void execute() {
    
        try {
            URLClassLoader loader = URLClassLoader.newInstance(new URL[] {new URL("")}, this.getClass().getClassLoader());
            loader.loadClass("TestCode").getMethod("testing").invoke(null);
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
        }
    }
}
