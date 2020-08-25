import java.io.*;
import java.util.*;

class InventoryAllocator {
  
  //name of warehouse
  private String wareHouseName;

  //inventory distribution
  private HashMap<String,Integer> inventory;

  //default constructor
  public InventoryAllocator(){
    this.wareHouseName="";
    this.inventory=new HashMap<String,Integer>();
  }

  //required getters and setters for private fields
  public String getWareHouseName(){
    return wareHouseName;
  }

  public void setWareHouseName(String wareHouseName){
    this.wareHouseName=wareHouseName;
  }

  public void addInventory(String item,int count){
    this.inventory.put(item,count);
  }
  
  //items -> a map of items that are being ordered and how many of them are ordered
  //wareHouses -> list of object with warehouse name and inventory amounts (inventory distribution) 
  //for these items
  //output -> cheapest shipment
  public static void allocateShipment(HashMap<String,Integer> items, 
    List<InventoryAllocator> wareHouses){
    //if input is null return
    if(items==null || wareHouses==null){
      System.out.println("[]");
      return;
    }

    //container for allocations
    Map<String,Map<String,Integer>> output=new HashMap<String,Map<String,Integer>>();    

    //loop through items - for every item check the warehouses
    for(Map.Entry mapElement : items.entrySet()){ 

      String key=(String)mapElement.getKey(); 
      int value=(int)mapElement.getValue();

      //loop through warehouses to check for item match
      for(int i=0;i<wareHouses.size();++i){
        String wareHouseName=wareHouses.get(i).wareHouseName;

        if(value<=0)
          continue;

        for(Map.Entry mapElement2: wareHouses.get(i).inventory.entrySet()){
          //if a match is found
          if(key.equals((String)mapElement2.getKey()) && (int)mapElement2.getValue()>0 && value>0){
            
            //check the amount of item that can be shipped from this warehouse
            int amountSubtracted=0;

            if((int)mapElement2.getValue()>=value){
              amountSubtracted=value;
              value=0;
            }
            else{
              value-=(int)mapElement2.getValue();
              amountSubtracted=(int)mapElement2.getValue();
            }

            //update output/allocation container
            if(output.containsKey(wareHouseName))
              output.get(wareHouseName).put((String)mapElement2.getKey(),amountSubtracted);
            else{
              HashMap<String,Integer> cur=new HashMap<String,Integer>();
              cur.put((String)mapElement2.getKey(),amountSubtracted);
              output.put(wareHouseName,cur);
            }
          }
        }
      }
    }

    //print all allocations
    if(output.size()==0)
      System.out.println("[]");
    else{
      String outputString="[";
      
      int counter=output.size();
      for(Map.Entry mapElement3 : output.entrySet()){ 
        outputString+="{ ";

        outputString+=(String)mapElement3.getKey()+": { "; 

        HashMap<String,Integer> outputMap=(HashMap<String,Integer>)mapElement3.getValue();

        int counter2=outputMap.size();

        for(Map.Entry mapElement4 : outputMap.entrySet()){
            outputString+=mapElement4.getKey()+": ";

            counter2--;
            if(counter2<=0)
              outputString+=mapElement4.getValue()+" }";

            else
              outputString+=mapElement4.getValue()+", ";
        }

        counter--;
        if(counter>0)
          outputString+="}, ";
        else
          outputString+=" }";
      }

      outputString+="]";

      System.out.println(outputString);
    }
  }

  public static void main(String[] args) {

    ////////
    //test 1 - Happy Case, exact inventory match!
    ////////
    HashMap<String,Integer> items1=new HashMap<String,Integer>();
    items1.put("apple",1);

    List<InventoryAllocator> wareHouses1=new ArrayList<InventoryAllocator>();

    InventoryAllocator first1=new InventoryAllocator();
    first1.setWareHouseName("owd");
    first1.addInventory("apple",1);
    wareHouses1.add(first1);

    System.out.println("Output for test 1:");
    //Function does not change arguments, only prints output
    allocateShipment(items1,wareHouses1);
    System.out.println("");


    ////////
    //test 2 - Not enough inventory -> no allocations!
    ////////
    HashMap<String,Integer> items2=new HashMap<String,Integer>();
    items2.put("apple",1);

    List<InventoryAllocator> wareHouses2=new ArrayList<InventoryAllocator>();

    InventoryAllocator first2=new InventoryAllocator();
    first2.setWareHouseName("owd");
    first2.addInventory("apple",0);
    wareHouses2.add(first2);

    System.out.println("Output for test 2:");
    allocateShipment(items2,wareHouses2);
    System.out.println("");

    ////////
    //test 3 - Should split an item across warehouses if that is the only way to completely ship an
    //         item
    ////////
    HashMap<String,Integer> items3=new HashMap<String,Integer>();
    items3.put("apple",10);

    List<InventoryAllocator> wareHouses3=new ArrayList<InventoryAllocator>();

    InventoryAllocator first3=new InventoryAllocator();
    first3.setWareHouseName("owd");
    first3.addInventory("apple",5);
    wareHouses3.add(first3);

    InventoryAllocator second3=new InventoryAllocator();
    second3.setWareHouseName("dm");
    second3.addInventory("apple",5);
    wareHouses3.add(second3);

    System.out.println("Output for test 3:");
    allocateShipment(items3,wareHouses3);
    System.out.println("");

    ////////
    //test 4 - Null/Invalid arguments
    ////////
    System.out.println("Output for test 4:");
    allocateShipment(null,null);
    System.out.println("");

    ////////
    //test 5 - Exact inventory match with second warehouse and not the first one (a modification of
    //         test 2)
    ////////
    HashMap<String,Integer> items5=new HashMap<String,Integer>();
    items5.put("apple",1);

    List<InventoryAllocator> wareHouses5=new ArrayList<InventoryAllocator>();

    InventoryAllocator first5=new InventoryAllocator();
    first5.setWareHouseName("owd");
    first5.addInventory("apple",0);
    wareHouses5.add(first5);

    InventoryAllocator second5=new InventoryAllocator();
    second5.setWareHouseName("dm");
    second5.addInventory("apple",1);
    wareHouses5.add(second5);

    System.out.println("Output for test 5:");
    allocateShipment(items5,wareHouses5);
    System.out.println("");

    ////////
    //test 6 - Exact inventory match with first warehouse and not the second one
    ////////
    HashMap<String,Integer> items6=new HashMap<String,Integer>();
    items6.put("apple",6);

    List<InventoryAllocator> wareHouses6=new ArrayList<InventoryAllocator>();

    InventoryAllocator first6=new InventoryAllocator();
    first6.setWareHouseName("owd");
    first6.addInventory("apple",6);
    wareHouses6.add(first6);

    InventoryAllocator second6=new InventoryAllocator();
    second6.setWareHouseName("dm");
    second6.addInventory("apple",6);
    wareHouses6.add(second6);

    System.out.println("Output for test 6:");
    allocateShipment(items6,wareHouses6);
    System.out.println("");

    ////////
    //test 7 - Multiple items across various inventories
    ////////
    HashMap<String,Integer> items7=new HashMap<String,Integer>();
    items7.put("apple",5);
    items7.put("banana",5);
    items7.put("orange",5);

    List<InventoryAllocator> wareHouses7=new ArrayList<InventoryAllocator>();

    InventoryAllocator first7=new InventoryAllocator();
    first7.setWareHouseName("owd");
    first7.addInventory("apple",5);
    first7.addInventory("orange",10);
    wareHouses7.add(first7);

    InventoryAllocator second7=new InventoryAllocator();
    second7.setWareHouseName("dm");
    second7.addInventory("banana",5);
    second7.addInventory("orange",10);
    wareHouses7.add(second7);

    System.out.println("Output for test 7:");
    allocateShipment(items7,wareHouses7);
  }
}