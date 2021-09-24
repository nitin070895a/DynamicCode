package com.example;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import dalvik.system.DexClassLoader;

import static android.content.Context.MODE_PRIVATE;

public class Compiler {

    void execute(Context context, String url) {
        
        try {
//            URLClassLoader loader = URLClassLoader.newInstance(new URL[] {new URL(url)}, this.getClass().getClassLoader());
//            Class<?> cls = loader.loadClass("com.example.TestCode");
//
//            Object o = cls.getConstructor().newInstance();
//            Method m = cls.getMethod("testing");
//            m.invoke(o);

//            Method[] m = cls.getMethods();
//            cls.getMethod("TestCode$testing").invoke(null);


            String optimizedDir = context.getApplicationContext().getDir("odex", MODE_PRIVATE).getAbsolutePath();

            URL[] classLoaderUrls = new URL[]{new URL(url)};

            // Create a new URLClassLoader
//            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
            DexClassLoader dcl = new DexClassLoader(url, optimizedDir, null, this.getClass().getClassLoader());

            // Load the target class
            Class<?> beanClass = dcl.loadClass("com.example.TestCode");

            // Create a new instance from the loaded class
            Constructor<?> constructor = beanClass.getConstructor();
            Object beanObj = constructor.newInstance();

            // Getting a method from the loaded class and invoke it
            Method method = beanClass.getMethod("testing");
            method.invoke(beanObj);

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
