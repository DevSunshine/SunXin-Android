package sunxin.sunshine.com.sunxin;

import com.sunshine.sun.lib.socket.request.SSITaskListener;
import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sun.lib.socket.request.SSTaskManager;
import com.sunshine.sun.lib.socket.toolbox.Priority;

/**
 * Created by gyzhong on 16/7/17.
 */
public class TestTask {

    public static void getSession(SSITaskListener listener){
        SSTask task = SSTaskManager.instance().createQueryTask() ;
        if (task != null){
            task.setTaskListener(listener).setPriority(Priority.HIGH) ;
            SSTaskManager.instance().commitTask(task);
        }
    }
}
