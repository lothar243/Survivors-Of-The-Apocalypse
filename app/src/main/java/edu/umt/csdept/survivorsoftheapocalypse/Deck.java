package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by sinless on 2/10/16.
 */
public class Deck {
    private ArrayList<Card> stack;
    private Bitmap image;

    public Deck() {
        this.stack = new ArrayList<Card>();
    }

    public void add(Card newCard, String position){
        if (position.compareTo("top")==0){
            stack.add(0,newCard);
        }
        if (position.compareTo("bottom")==0){
            stack.add(newCard);
        }
    }

    public void create(Collection<Card> deck){
        stack.addAll(deck);
    }

    public Card[] peek(int x){
        int  i=0;
        x = Math.min(x, count());
        Card[] topX =new Card[x];
        while( i<x){
            topX[i] = stack.get(i);
        }
        return topX;
    }

    public Card peek(){
        return stack.get(0);
    }

    public  Card draw(){
        return stack.remove(0);
    }

    public Card[] draw(int x){
        int  i=0;
         x = Math.min(x, count());
        Card[] topX =new  Card[x];
        while( i<x){
            topX[i] = stack.remove(0);
        }
        return topX;
    }

    public int count(){
        return stack.size();
    }

    public Bitmap getBitmap(){
        return this.image;
    }
}


