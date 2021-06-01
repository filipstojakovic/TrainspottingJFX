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
    /*
    useage example
    } catch (IOException ex)
    {
        GenericLogger.log(this.getClass(), ex);
    }
     */
    public static void log(Class<?> C, Exception ex)
    {
        GenericLogger.log(C, Level.WARNING, ex.fillInStackTrace().toString(), ex);
    }

    public static void log(Class<?> C, Level level, String msg, Throwable thrown)
    {
        Logger logger = Logger.getLogger(C.getName());
        if (logger.getHandlers().length == 0)
        {
            try
            {
                String path = "." + File.separator + "logs" + File.separator;
                Utils.createFolderIfNotExists(path);
                Handler handler = new FileHandler(path + C.getName() + LocalDateTime.now().toLocalTime().toString().replace(':', '_') + ".log");
                logger.addHandler(handler);
                logger.log(level, msg, thrown);
                handler.close();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
