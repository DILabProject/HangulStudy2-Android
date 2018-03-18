package kr.ac.skuniv.di.hangulstudy.sharedmemory;

import android.graphics.Path;

import java.util.LinkedList;

import kr.ac.skuniv.di.hangulstudy.DrawLine;

/**
 * Created by namgiwon on 2017. 11. 17..
 */

public class SharedMemory {
    private static SharedMemory sharedMemory = null;

    public static synchronized SharedMemory getinstance(){
        if(sharedMemory == null){
            sharedMemory = new SharedMemory();
        }

     return sharedMemory;
    }

    DrawLine drawLine;

    public DrawLine getDrawLine() {
        return drawLine;
    }

    public void setDrawLine(DrawLine drawLine) {
        this.drawLine = drawLine;
    }
}
