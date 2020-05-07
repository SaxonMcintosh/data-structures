/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
/**
 *
 * @author snott
 */
public class Totaler {
    private int comp;
    private int exch;
	
    public Totaler(){
       this.comp=0;
       this.exch=0;
    }
	
    void addExch(){
        exch++;
    }
	
    void addComp(){
        comp++;
    }
    
    int getComp(){
        Map<String, Integer> map = new HashMap<>();
		
	map.put("Comparisons", comp);
        return comp;
    }
    
    int getExch(){
        Map<String, Integer> map = new HashMap<>();
		
	map.put("Exchanges", exch);
        return exch;
    }
    void print(){
        System.out.println("Comparisons: "+comp+", Exchanges: ");
    }
}
