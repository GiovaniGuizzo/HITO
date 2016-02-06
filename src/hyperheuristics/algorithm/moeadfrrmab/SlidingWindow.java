/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author vinicius
 */
public class SlidingWindow {
    private final int maxSize;
    private final ArrayList<String[]> slidewindow;
    

    public SlidingWindow(int maxSize) {
        this.maxSize = maxSize;
        this.slidewindow=new ArrayList<>();
    }

    public void addItem(String op, double value, HashMap<String, Integer> nt){
        String[] data=new String[2];
        data[0]=op;
        data[1]=String.valueOf(value);
        if(this.slidewindow.size()==maxSize){
            String[] oldop=this.slidewindow.remove(0);
            int oldopqd=nt.get(oldop[0]);
            nt.put(oldop[0], oldopqd-1);
        }
        nt.put(op, nt.get(op)+1);
        this.slidewindow.add(data);
    }
    
    public String getIndexOp(int pos){
        String[] data=this.getItemPos(pos);
        return data[0];
    }
    
    public double getFIR(int pos){
        String[] data=this.getItemPos(pos);
        return Double.valueOf(data[1]);
    }

    private String[] getItemPos(int pos){
        return this.slidewindow.get(pos);
    }
    
    public int getLength() {
        return this.slidewindow.size();
    }
    
    public ArrayList<String[]> getWindowforOp(String op){
        ArrayList<String[]> values=new ArrayList<>();
        for (int i = 0; i < this.getLength(); i++) {
            String ops = this.getIndexOp(i);
            if(ops.equals(op)){
                values.add(this.slidewindow.get(i));
            }
        }
        return values;
    }
}
