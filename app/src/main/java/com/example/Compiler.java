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
import java.util.concurrent.Executors;

import dalvik.system.DexClassLoader;

public class Compiler {

    private static final String SECONDARY_DEX_NAME = "NumberProcessorDex.jar";
    private static final int BUF_SIZE = 8 * 1024;

    private Callbacks callbacks;

    Compiler(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    void execute(Context context, int number) {
        Executors.newSingleThreadExecutor().execute(() -> Compiler.this.run(context, number));
    }

    void run(Context context, int number) {

        File dexInternalStoragePath = new File(context.getDir("dex", Context.MODE_PRIVATE), SECONDARY_DEX_NAME);
        BufferedInputStream bis;
        OutputStream dexWriter;

        try {
            bis = new BufferedInputStream(context.getAssets().open(SECONDARY_DEX_NAME));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File optimizedDexOutputPath = context.getDir("outdex", Context.MODE_PRIVATE);

        DexClassLoader dexLoader = new DexClassLoader(
            dexInternalStoragePath.getAbsolutePath(),
            optimizedDexOutputPath.getAbsolutePath(),
            null,
            this.getClass().getClassLoader()
        );

        try {

            Class<?> numberProcessor = dexLoader.loadClass("NumberProcessor");
            Object instance = numberProcessor.newInstance();
            Method method = numberProcessor.getMethod("getRandomResponse", int.class);
            Object result = method.invoke(instance, number);

            if (callbacks != null)
                callbacks.onResult((String) result);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    interface Callbacks {

        void onResult(String result);
    }
}
