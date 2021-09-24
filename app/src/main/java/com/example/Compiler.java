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

    private static final String URL = "https://github.com/nitin070895a/DynamicCode/raw/" +
        "master/app/src/main/assets/NumberProcessorDex.jar";              // Dynamic jar library
    private static final String CLASS = "NumberProcessor";                // Class to load
    private static final String METHOD = "getRandomResponse";             // Method to execute
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
        String dexPath = "";
        try {
    
            print("Opening jar file stream from url: " + URL);
            URL url = new URL(URL);
            BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            //inputStream = new BufferedInputStream(context.getAssets().open(DEX_NAME)); // Assets

            print("Writing file in dex folder...");
            dexPath = HelperKt.downloadDex(context, inputStream, DEX_NAME);
            print("File written at: " + dexPath);
            
            inputStream.close();

        } catch (IOException e) {
            print("Exception: Opening the jar file");
            e.printStackTrace();
        }

        print("Creating class loader...");
        final File outDexPath = context.getDir("outdex", Context.MODE_PRIVATE);
        DexClassLoader dexLoader = new DexClassLoader(
            dexPath,
            outDexPath.getAbsolutePath(),
            null
            , this.getClass().getClassLoader()
        );

        try {

            print("Getting class: " + CLASS + " ...");
            Class<?> cls = dexLoader.loadClass(CLASS);
            Object instance = cls.newInstance();
            Method method = cls.getMethod(METHOD, int.class);
            print("Invoking method: " + METHOD + " ...");
            Object result = method.invoke(instance, number);

            if (callbacks != null) {
                callbacks.onResult((String) result);
            }

        } catch (ClassNotFoundException e) {
            print("Exception: Dynamic class not found");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            print("Exception: Dynamic method not found");
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            print("Exception: Dynamic code execution");
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
