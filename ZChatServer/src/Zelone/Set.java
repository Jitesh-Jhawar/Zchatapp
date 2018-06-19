/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Zelone;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

/**
 *
 * @author user
 */
public class Set {

    public Set() {

    }

    public void doit(String name, String words) {
       System.out.println(words+"NAME:"+name);
       try {
            words = words.trim();
            char t1 = words.charAt(0);
            char t2 = words.charAt(1);
            words = words.substring(2);
            Robot r = new Robot();
           
            switch (t1) {
                case 'M'://MOUSE
                    switch (t2) {
                        case 'M'://CLICK
                            try {
                                String[] word = words.split(",");
                                int x = Integer.parseInt(word[0]);
                                int y = Integer.parseInt(word[1]);
                                r.mouseMove(x, y);
                            } catch (Exception e) {
                                r.mouseMove(50, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
                            }
                            break;
                        case 'P'://PRESS
                            try {
                                String[] word = words.split(",");
                                int x = Integer.parseInt(word[0]);
                                r.mousePress(x * 1024);
                            } catch (Exception e) {
                                r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            }
                            break;
                        case 'R'://REALEASE
                            try {
                                String[] word = words.split(",");
                                int x = Integer.parseInt(word[0]);
                                r.mouseRelease(x * 1024);
                            } catch (Exception e) {
                                r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            }
                            break;
                        case 'C'://CLICK
                            try {
                                String[] word = words.split(",");
                                int x = Integer.parseInt(word[0]);
                                r.mousePress(x * 1024);
                                r.mouseRelease(x * 1024);
                            } catch (Exception e) {
                                r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            }
                            break;
                            case 'S'://SCROLL
                            try {
                                String[] word = words.split(",");
                                int x = Integer.parseInt(word[1]);
                                int up=Integer.parseInt(word[0]);
                                r.mouseWheel(x*up);
                            } catch (Exception e) {
                                r.mouseWheel(1);
                            }
                            break;
                    }break;
                case 'K':switch(t2){
                    case 'P'://PRESS
                        char d=words.charAt(0);
                        if(d=='/'){switch(words.charAt(1)){
                            case 's':d=' ';break;
                            case '/':d='/';break;
                        }                        r.keyPress((int)d);
}else{
                        r.keyPress((int)d);
                        }                  
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
