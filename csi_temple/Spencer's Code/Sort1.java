/**
 *
 * @author snott
 */
public class Sort1 {
    Totaler total=new Totaler();
    
    public Sort1(){
        
    }
    
    public void sort(int start[]){
        for(int i=1;i<start.length;i++){
            int temp=start[i];
            int k=i-1;
            for(int j=i-1;j>=0 && start[j]>temp;j--){
                total.addComp();
                start[j+1]=start[j];
                total.addExch();
                k=j;
            }
            start[k+1]=temp;
        }
        
    }
    
    public void print(){
        total.print();
    }
}
