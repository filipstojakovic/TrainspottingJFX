package project.Util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericLogger
{
    public static void createLog(Class<?> C, Exception ex)
    {
        GenericLogger.createLog(C, Level.WARNING, ex.fillInStackTrace().toString(), ex);
    }

    public static void createLog(Class<?> C, Level level, String msg, Throwable thrown)
    {
        Logger logger = Logger.getLogger(C.getName());
        //if (logger.getHandlers().length == 0)
        //{
        try
        {
            String path = "." + File.separator + "res" + File.separator + "logs" + File.separator;
            Utils.createFolderIfNotExists(path);
            Handler handler = new FileHandler(path + C.getName() + LocalDateTime.now().toLocalTime().toString().replace(':', '_') + ".log");
            logger.addHandler(handler);
            logger.log(level, msg, thrown);
            handler.close();

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        //}
    }

    public static void createAsyncLog(Class<?> C, Exception ex)
    {
        GenericLogger.createAsyncLog(C, Level.WARNING, ex.fillInStackTrace().toString(), ex);
    }

    public static void createAsyncLog(Class<?> C, Level level, String msg, Throwable thrown)
    {
        new Thread(() -> GenericLogger.createLog(C, level, msg, thrown)).start();
    }
}
