package com.example;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import dalvik.system.DexClassLoader;

import static android.content.Context.MODE_PRIVATE;

public class Compiler {

    void execute1(Context context, String url) {
        
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
            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
//            DexClassLoader dcl = new DexClassLoader(url, optimizedDir, null, this.getClass().getClassLoader());

            // Load the target class
            Class<?> beanClass = urlClassLoader.loadClass("com.example.TestCode");

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
    private String SECONDARY_DEX_NAME = "DexJar.jar";
    static final int BUF_SIZE = 8 * 1024;

    void execute(Context context) {

        File dexInternalStoragePath = new File(context.getDir("dex",
            Context.MODE_PRIVATE), SECONDARY_DEX_NAME);
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;

        try {
            bis = new BufferedInputStream(context.getAssets().open(SECONDARY_DEX_NAME));
            dexWriter = new BufferedOutputStream(new FileOutputStream(
                dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final File optimizedDexOutputPath = context.getDir("outdex",
            Context.MODE_PRIVATE);

        DexClassLoader cl = new DexClassLoader(
            dexInternalStoragePath.getAbsolutePath(),
            optimizedDexOutputPath.getAbsolutePath(), null,
            this.getClass().getClassLoader());
        Class libProviderClazz = null;
        // Load the library.
        try {
            libProviderClazz = cl.loadClass("com.example.TestCode");
            System.out.println(cl.toString());

            System.out.println(libProviderClazz.getClassLoader().toString());

            Object lib = libProviderClazz.newInstance();
            // Getting a method from the loaded class and invoke it
            Method method = libProviderClazz.getMethod("testing");
            method.invoke(lib);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
