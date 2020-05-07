/**
 *
 * @author snott
 */
public class Sort2 {
    Totaler total=new Totaler();
    
    public Sort2(){
        
    }
    
    public void sort(int arr[]){
        sortie(0,arr.length-1,arr);
    }
    
    public void sortie(int min,int max,int start[]){
        if(min<max){
            total.addComp();
            int index=partition(min,max,start);
            sortie(min,index-1,start);
            sortie(index+1,max,start);
        }
    }
    
    public int partition(int min,int max,int start[]){
        int piv=start[max];
        int i=min-1;
        
        for(int j=min;j<max;j++){
            total.addComp();
            
            if(start[j]<piv){
                i++;
                int temp=start[i];
                total.addExch();
                start[i]=start[j];
                total.addExch();
                start[j]=temp;
            }
        }
        int temp=start[i+1];
        total.addExch();
        start[i+1]=start[max];
        total.addExch();
        start[max]=temp;
        total.addExch();
        return i+1;
    }

    
    public void print(){
        total.print();
    }
}
