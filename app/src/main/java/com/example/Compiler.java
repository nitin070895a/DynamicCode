package com.example;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.Executors;

import dalvik.system.DexClassLoader;

public class Compiler {

    private static final String DEX_NAME = "NumberProcessorDex.jar";
    private static final int BUF_SIZE = 8 * 1024;

    private final Callbacks callbacks;

    Compiler(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    void execute(Context context, int number) {
        Executors.newSingleThreadExecutor().execute(() -> Compiler.this.run(context, number));
    }

    void run(Context context, int number) {

        File dexPath = new File(context.getDir("dex", Context.MODE_PRIVATE), DEX_NAME);

        try {

            URL url = new URL("https://github.com/nitin070895a/DynamicCode/raw/master/app/src/main/assets/NumberProcessorDex.jar");
            BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            //inputStream = new BufferedInputStream(context.getAssets().open(DEX_NAME)); // Loading from assets
            print("Opening jar file stream from url: https://github.com/nitin070895a/DynamicCode/raw/master/app/src/main/assets/NumberProcessorDex.jar...");

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dexPath));
            print("Writing file in dex folder...");

            byte[] buffer = new byte[BUF_SIZE];
            int len;
            while ((len = inputStream.read(buffer, 0, BUF_SIZE)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        print("Creating class loader...");
        final File outDexPath = context.getDir("outdex", Context.MODE_PRIVATE);
        DexClassLoader dexLoader = new DexClassLoader(
            dexPath.getAbsolutePath(), outDexPath.getAbsolutePath(), null, this.getClass().getClassLoader()
        );

        try {

            print("Getting class...");
            Class<?> numberProcessor = dexLoader.loadClass("NumberProcessor");
            Object instance = numberProcessor.newInstance();
            Method method = numberProcessor.getMethod("getRandomResponse", int.class);
            print("Invoking method...");
            Object result = method.invoke(instance, number);

            if (callbacks != null) {
                callbacks.onResult((String) result);
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    void print(String msg) {
        if (callbacks != null)
            callbacks.log(msg);
    }

    interface Callbacks {

        void log(String msg);

        void onResult(String result);
    }
}
