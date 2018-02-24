package meraya.ua.com.grouper;

import android.app.Application;
import android.content.Intent;

import meraya.ua.com.grouper.service.MyService;


public class Grouper extends Application {

    private static Grouper grouperInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        initGrouperInstance();
    }

    private static void initGrouperInstance(){
        if (grouperInstance == null){
            grouperInstance = new Grouper();
        }
    }

    public static Grouper getGrouperInstance(){
        return grouperInstance;
    }
}
