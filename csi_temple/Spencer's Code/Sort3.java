/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snott
 */
public class Sort3 {
    Totaler total=new Totaler();
    private int skip;
    
    public Sort3(){
        
    }

    public void sort(int start[]){
        skip=start.length/2;
        while(skip>0){
            for(int i=skip;i<start.length;i++){
                int j=i;
                int temp=start[i];
                total.addComp();
                while(j>=skip&&start[j-skip]>temp){
                    start[j]=start[j-skip];
                    j=j-skip;
                    total.addExch();
                }
                start[j]=temp;
            }
            if(skip==2){
                skip=1;
            }else{
                skip/=2.2;
            }
        }
    }
}
