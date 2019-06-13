package lite.android.xiangwushuo.com.shangraostation;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class JavaHookActivity extends AppCompatActivity {

    private boolean intercepte = false;
    private int notificationId  = 10001  ;
    private String content = "This is big text" +
            "This is big text" +
            "This is big text" +
            "This is big text" +
            "This is big text" +
            "This is big text" +
            "This is big text" +
            "This is big text" ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_hook);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        createNotificationChannel() ;
    }

    private void click (View view ){
        intercepte = !intercepte;
        String toastStr;
        if (intercepte)
            toastStr = "拦截";
        else
            toastStr = "通知";
        Toast.makeText(JavaHookActivity.this, toastStr + "   " + notificationId ,  Toast.LENGTH_SHORT).show();
        NotificationManager notifyMng = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ActionService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        Intent deleteIntent = new Intent(this ,ActionReceiver.class) ;
        deleteIntent.putExtra("action" , 2) ;
        PendingIntent deletePendingIntent  = PendingIntent.getBroadcast(this ,1 , deleteIntent , 0 ) ;

        Intent likeIntent = new Intent(this , ActionService.class);
        likeIntent.putExtra("action" , 1) ;
        PendingIntent likePendingIntent  = PendingIntent.getService(this ,1 , likeIntent , 0 ) ;

        Bitmap bigPicture = Bitmap.createBitmap(500 , 300 , Bitmap.Config.ARGB_8888) ;
        Notification notification = new NotificationCompat
                .Builder(this, "googleChannel")
                .setSmallIcon(R.drawable.apply_success_icon)
                .setContentTitle("title")
                .setContentText(content.substring(0  , 20 ))
                .setContentIntent(pendingIntent)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
//                .setStyle(new NotificationCompat
//                        .BigPictureStyle()
//                        .setBigContentTitle(content)
//                        .setSummaryText("summary Text")
//                        .bigPicture(bigPicture))
//                .setStyle(new NotificationCompat
//                        .InboxStyle()
//                        .addLine("line")
//                        .setBigContentTitle("Big content Title , Big Content title ")
//                        .setSummaryText("summary Text"))
                .addAction(0 ,"收藏" , likePendingIntent )
                .addAction(2 , "删除" , deletePendingIntent )
                .addInvisibleAction(1, "Invisible" , null )
                .addInvisibleAction(2 , "Invisible2 " , null)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build() ;

        assert notifyMng != null;
        notifyMng.notify(notificationId ++ , notification);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "googleName";
            String description = "desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("googleChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        hookNotificationManager(newBase);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
    }

    public static class ActionService extends IntentService{


        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public ActionService(String name) {
            super(name);
        }

        public ActionService(){
            super("default Service name") ;
        }

        @Override
        protected void onHandleIntent( @Nullable Intent intent) {
            if(intent == null ) return ;
            int action = intent.getIntExtra("action" , -1) ;
            Handler uiHandler = new Handler(Looper.getMainLooper()) ;
            switch (action){
                case 1:
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext() , "收藏", Toast.LENGTH_LONG).show();
                        }
                    }) ;
                     break ;
                case 2:
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext() , "删除", Toast.LENGTH_LONG).show();
                        }
                    }) ;

            }
        }
    }

    public static class ActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Handler uiHandler = new Handler(Looper.getMainLooper()) ;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "删除", Toast.LENGTH_LONG).show();
                }
            }) ;

        }
    }

    private void hookNotificationManager(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 得到系统的 sService
            Method getService = NotificationManager.class.getDeclaredMethod("getService");
            getService.setAccessible(true);
            final Object sService = getService.invoke(notificationManager);

            Class iNotiMngClz = Class.forName("android.app.INotificationManager");
            // 动态代理 INotificationManager
            Object proxyNotiMng = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{iNotiMngClz}, new InvocationHandler() {

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.e("HookActivity", "Method  is " + method.toString());
                    if (args != null && args.length > 0) {
                        for (Object arg : args) {
//                            Log.e("HookActivity", "object arg is " + arg.toString());
                        }
                    }
                    if (intercepte) {
                        return null;
                    } else {
                        return method.invoke(sService, args);
                    }
                    // 操作交由 sService 处理，不拦截通知
                    // return method.invoke(sService, args);
                    // 拦截通知，什么也不做
//                    return null;
                    // 或者是根据通知的 Tag 和 ID 进行筛选
                }
            });
            // 替换 sService
            Field sServiceField = NotificationManager.class.getDeclaredField("sService");
            sServiceField.setAccessible(true);
            sServiceField.set(notificationManager, proxyNotiMng);
        } catch (Exception e) {
            Log.e("HookActivity", "Hook NotificationManager failed!");
        }
    }
}
