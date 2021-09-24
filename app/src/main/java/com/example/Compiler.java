package com.example;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.Executors;

import dalvik.system.DexClassLoader;

/**
 * Dynamic code executor,
 */
public class Compiler {

    private static final String DEX_NAME = "NumberProcessorDex.jar";      // File name to be stored

    private final ExecutionCallbacks callbacks;                           // Execution callbacks
    
    /**
     * Constructor
     * @param callbacks Events callbacks for code execution
     */
    Compiler(ExecutionCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    
    /**
     * Broadcast a log event on the {@link this#callbacks}
     * @param msg The message to be logged
     */
    private void print(String msg) {
        if (callbacks != null)
            callbacks.log(msg);
    }
    
    /**
     * Executes the dynamic code
     *
     * @param context The context of the activity
     * @param number The number to be processed
     */
    private void run(Context context, int number)
    {
        String uri = "https://github.com/nitin070895a/DynamicCode/raw/master/app/src/main/assets/NumberProcessorDex.jar";
       
        String dexPath = "";
        try {
    
            print("Opening jar file stream from url:" + uri + "...");
            URL url = new URL(uri);
            BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            //inputStream = new BufferedInputStream(context.getAssets().open(DEX_NAME)); // Loading from assets

            print("Writing file in dex folder...");
            dexPath = HelperKt.downloadDex(context, inputStream, DEX_NAME);
            
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        print("Creating class loader...");
        final File outDexPath = context.getDir("outdex", Context.MODE_PRIVATE);
        DexClassLoader dexLoader = new DexClassLoader(
            dexPath, outDexPath.getAbsolutePath(), null, this.getClass().getClassLoader()
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
    
    /**
     * Executes the dynamic code asynchronously
     *
     * @param context The context of the activity
     * @param number The number to be processed
     */
    void execute(Context context, int number) {
        Executors.newSingleThreadExecutor().execute(() -> Compiler.this.run(context, number));
    }
}
