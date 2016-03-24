package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Created by sinless on 2/10/16.
 */
public class Deck<T> {
    private ArrayList<T> stack;
    private Bitmap image;

    public Deck() {
        this.stack = new ArrayList<T>();
    }

    public void add(T newT, String position){
        if (position.compareTo("top")==0){
            stack.add(0,newT);
        }
        if (position.compareTo("bottom")==0){
            stack.add(newT);
        }
    }

    public void create(Collection<T> deck){
        stack.addAll(deck);
    }

    public T[] peek(int x){
        int  i=0;
        x = Math.min(x, count());
        Object[] topX =new Object[x];
        while( i<x){
            topX[i] = stack.get(i);
        }
        return (T[])topX;
    }

    public T peek(){
        return stack.get(0);
    }

    public  T draw(){
        return stack.remove(0);
    }

    public T[] draw(int x){
        int  i=0;
         x = Math.min(x, count());
        Object[] topX =new  Object[x];
        while( i<x){
            topX[i] = stack.remove(0);
        }
        return (T[])topX;
    }

    public void shuffle(){
        Collections.shuffle(stack);
    }

    public int count(){
        return stack.size();
    }

    public Bitmap getBitmap(){
        return this.image;
    }


}


