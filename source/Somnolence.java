import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Somnolence extends PApplet {

//Things to save: name, row, column, Scroll,Inventory,safe,pobox,car,frameOpened,framed
//More things to save: nodirections, door variables, misc boolean variables
String input="";
String response="";
String loadname="";
String [] Scroll;
String [] lines;
String [] inventory;
String [][]Map;
String name="";
int row=2;
int col=2;
int scalecount=0;
int cribcount=0;
int red=0;
int green=0;
int blue=0;
int countdown=1;
int place=31;
boolean gamestart=false;
boolean entername=false;
boolean namecheck=false;
boolean savecheck=false;
boolean save=false;
boolean loadgame=false;
boolean loadcheck=false;
boolean exitcheck=false;
boolean noeast=false;
boolean nowest=false;
boolean nonorth=false;
boolean nosouth=false;
boolean firstdoor=false;
boolean gardendoor=false;
boolean playdoor=false;
boolean lastdoor=false;
boolean safeUnlocked=false;
boolean poboxOpened=false;
boolean carSmashed=false;
boolean frameOpened=false;
boolean barrelReady=false;
boolean endGame=false;
boolean endStart=false;
//name, if opened, contents(if any), room
Container dresser= new Container("dresser",false,"silver key",22);
Container closet= new Container("closet",false,"",22);
Container cupboard=new Container("cupboard",false,"bottle",14);
Container drawer=new Container("drawer",false,"",14);
Container fridge=new Container("fridge",false,"batteries",14);
Container bphotoalbum=new Container("blue photo album",false,"family photo",01);
Container gphotoalbum=new Container("grey photo album",false,"elderly photo",01);
Container wphotoalbum=new Container("white photo album",false,"baby photo",01);
Container sshelf=new Container("small shelf",true,"",21);
Container mshelf=new Container("medium shelf",true,"",21);
Container lshelf=new Container("large shelf",true,"gold book",21);
Container toolbox=new Container("toolbox",false,"wrench",03);
Container safe=new Container("safe",false,"cross",21);
Container trunk=new Container("trunk",false,"newspaper",03);
Container pobox=new Container("po box",false,"floral key",10);
Container parcel=new Container ("parcel",false,"flowers",24);
Container toybox=new Container("toy box",false,"doll",00);
//name,position in _Data.ini,used,solo,room,corresponding item, returning item, object description
Object door=new Object("door",0,false,false,33,"","","");
Object shower=new Object("shower",19,false,true,04,"","","It's a shower.#Maybe you could get the water going?");
Object ladder=new Object("ladder",20,false,true,21,"","","This ladder could help you reach higher places.");
Object sink=new Object("sink",21,false,false,04,"wrench","copper pipe","A porcelain sink attached to the wall with a copper pipe.#With the right tool, the pipe could be pried free.");
Object barrel=new Object("barrel",22,false,false,14,"copper pipe","","An oak barrel with a spigot hole.#Any pipe will fit in there though.");
Object scale=new Object("scale",24,false,false,23,"whiskey bottle","","Filling the scale's barrel with liquid might move it.");
Object remote=new Object("remote",26,false,false,01,"batteries","","You can use this to change the channel.#If it has batteries that is.");
Object rcar=new Object("red car",27,true,false,03,"crowbar","","A shiny red car.#The bumper is nothing but twisted metal.");
Object bcar=new Object("blue car",27,false,false,03,"crowbar","","A shiny blue car.#It looks like it can be broken with a blunt object.");
Object ycar=new Object("yellow car",27,false,false,03,"crowbar","","A shiny yellow car.#It looks like it can be broken with a blunt object.");
Object gcar=new Object("green car",27,false,false,03,"crowbar","","A shiny green car.#It looks like it can be broken with a blunt object.");
Object wcar=new Object("white car",27,false,false,03,"crowbar","playroom key","A shiny white car.#It looks like it can be broken with a blunt object.");
Object crib=new Object("crib",28,false,false,20,"","","It's a crib with a picture frame in it.#Try putting some items into it!");
Object frame=new Object("picture frame",29,false,false,20,"baby photo","front door key","A picture could go into this frame.");



Container [] containers={dresser,closet,drawer,cupboard,fridge,bphotoalbum,gphotoalbum,wphotoalbum,sshelf,mshelf,lshelf,toolbox,safe,trunk,pobox,parcel,toybox};
Object objects[]={door,shower,ladder,sink,barrel,scale,remote,rcar,wcar,bcar,gcar,ycar,crib,frame};
String [] puzzle={"cross","flowers","doll"};
String [] picture={"baby photo","family photo","elderly photo"};
String store="";
String framed="";
/*
*****
*****
*****
*/
public void setup()
{
 
 Scroll=new String[40];
inventory=new String[20];
for(int j=0;j<inventory.length;j++)
inventory[j]="";
 for(int i=0;i<Scroll.length;i++)
Scroll[i]="";
Map=new String[3][5];
lines=loadStrings("_Data.ini");
createMap(lines,Map);
noDir();
ScrollUp(Scroll,"Welcome to Somnolence!","Type 'new' to create a new game, or 'load' to continue an existing game.#Once you begin playing, you can type HELP or just H if you ever get stuck.");
}
public void draw()
{
  background(red,green,blue);
  if(endGame==false)
  fill(255);
  textSize(18);
  text(input,100,720);
  if(barrelReady==true)
  barrel.changeItem("bottle");
  
for(int i=0;i<Scroll.length;i++)
  text(Scroll[i],100,i*18);
  if (save==true)
  {
   saveGame();
   save=false;
  }
  if (endGame==true)
  endGame();
  
  
}

public void ScrollUp(String [] array, String input,String response)
{
  int linecount=0;
  String [] responses;
  responses=split(response,'#');
  linecount=responses.length-1;

for(int i=0;i<array.length-2-linecount;i++)
   array[i]=array[i+2+linecount];
for(int i=0;i<responses.length;i++)
  array[array.length-1-linecount+i]=responses[i];
  array[array.length-2-linecount]=input;
}

public void endScroll(String [] array, String input)
{
for(int i=0;i<array.length-1;i++)
 array[i]=array[i+1];
 array[array.length-1]=input;
}


public void createMap(String [] sArray,String [] [] xArray)
{
  int row=0;
  int col=0;
  for(int i=1;i<=15;i++)
  {
  row=Character.getNumericValue(sArray[i].charAt(0))-10;
  col=Character.getNumericValue(sArray[i].charAt(1))-10;
  xArray[row][col]=sArray[i].substring(3,sArray[i].length());
  }

}
public void saveGame()
{
  String [] saveState;
  saveState=new String [77];
  saveState[0]=name;
  saveState[1]=PApplet.parseChar(row+65)+" ";
  saveState[2]=PApplet.parseChar(col+65)+" ";
  for(int i=3;i<43;i++)
  saveState[i]=Scroll[i-3];
  saveState[43]=String.valueOf(firstdoor);
  saveState[44]=String.valueOf(gardendoor);
  saveState[45]=String.valueOf(playdoor);
  saveState[46]=String.valueOf(lastdoor);
  for(int i=47;i<67;i++)
  saveState[i]=inventory[i-47];
  saveState[67]=String.valueOf(nonorth);
  saveState[68]=String.valueOf(nosouth);
  saveState[69]=String.valueOf(noeast);
  saveState[70]=String.valueOf(nowest);
  
  saveState[71]=String.valueOf(safeUnlocked);
  saveState[72]=String.valueOf(poboxOpened);
  saveState[73]=String.valueOf(carSmashed);
  saveState[74]=String.valueOf(frameOpened);
  saveState[75]=String.valueOf(barrelReady);
  saveState[76]=framed;
  

  saveStrings(name.toLowerCase()+".ini",saveState);
}
public boolean fileExists(String input)
{
 File file= new File(input);
 boolean exists=file.exists();
 if(exists)
 return true;
 else
 return false;
}
public void loadGame(String filename)
{
  String [] load;
 load=loadStrings(filename);
 name=load[0];
row=Character.getNumericValue(load[1].charAt(0))-10;
 col=Character.getNumericValue(load[2].charAt(0))-10;
 for(int i=3;i<43;i++)
Scroll[i-3]=load[i];
 firstdoor=Boolean.valueOf(load[43]);
 gardendoor=Boolean.valueOf(load[44]);
 playdoor=Boolean.valueOf(load[45]);
 lastdoor=Boolean.valueOf(load[46]);
 for(int i=47;i<67;i++)
 inventory[i-47]=load[i];

 nonorth=Boolean.valueOf(load[67]);
 nosouth=Boolean.valueOf(load[68]);
 noeast=Boolean.valueOf(load[69]);
 nowest=Boolean.valueOf(load[70]);
 
 safeUnlocked=Boolean.valueOf(load[71]);
 poboxOpened=Boolean.valueOf(load[72]);
 carSmashed=Boolean.valueOf(load[73]);
 frameOpened=Boolean.valueOf(load[74]);
 barrelReady=Boolean.valueOf(load[75]);
 framed=load[76];
 
}

public boolean invSearch(String item)
{
 for(int i=0;i<inventory.length;i++)
 {
  if(inventory[i].equals(item))
   return true;
 }
  return false;
}

public void invRemove(String item)
{
 for(int i=0;i<inventory.length;i++)
{
 if(inventory[i].equals(item))
 {
   if(i==inventory.length)
   inventory[i]="";
   else
   {
     int safe=i;
    for(int j=i+1;j<inventory.length;j++)
   {
    inventory[j-1]=inventory[j];
   } 
     break;
     
   } 
 }
} 
}

public void endGame()
{
  if(endStart==false)
  {
  if(red<255)
  {
  red+=2;
  green+=2;
  blue+=2;
  }
  else
  {
    for(int i=0;i<Scroll.length;i++)
    Scroll[i]="";
    if(Scroll[Scroll.length-1].equals("") && Scroll[0].equals(""))
    {
    fill(0);
    endStart=true;
    }
  }
  }
  
  if(endStart==true)
  { 
    countdown--;
    if(countdown==0)
    countdown=300;
    
    if(countdown==50 && place==51)
    {
    for(int i=0;i<Scroll.length;i++)
    Scroll[i]="";
    }
    
    if (countdown==300 && place<=52)
    {
      ScrollUp(Scroll,"",lines[place]);
      place++;
      
    }
  

  }
}



public void keyTyped()
{
  if(endGame==false)
  {
  if ((key>='a' && key<='z') || (key>='A' && key<='Z') || key==' ' || key>='0' && key<='9')
  input=input+key;

  if(key==BACKSPACE || key==DELETE)
  {
  if(input.length()>1)
   input=input.substring(0,input.length()-1);
   else
   input="";
 
  }

  if(key==ENTER && input!="")
  {
  response=Response(input);
  ScrollUp(Scroll,input,response);
  input="";
  }
  }
}





































class Container
{
  String name;
  boolean opened;
  String contents;
  int room;

  public Container(String n, boolean x,String y,int z)
  {
   name=n;
   opened=x;
   contents=y;
   room=z;
  }

  public String open()
  {
  if(row==(room-(room%10))/10 && col==room%10)
  {
  if(name.equals("safe") && safeUnlocked==false)
  return "It won't open. You need to enter the combination.";
  
  if(name.equals("po box") && poboxOpened==false)
  {
    poboxOpened=false;
    return "Which one?";
  }
  
  opened=true;
  if(contents.equals("") || invSearch(contents)==true)
  return "You don't find anything of value.";
  return "You find "+contents+".";
   
  
  
  
  }
   else
   return "I can't see that";
  }

  public boolean ifOpened()
  {
  return opened;
  }
  public int getRoom()
  {
  return room;
  }
  public String getname()
  {
   return name;
  }
  public String getContents()
  {
   return contents;
  }
}

//Unlocking doors method
public String unlock()
{
   if(row==2 && col==2 && invSearch("silver key")==true)
   {
  firstdoor=true;
  nonorth=false;
   return "You successfully unlock and open the door.";
   }
   else if (row==2 && col==2 && invSearch("silver key")==false)
   return "It won't open.";
   
   if(row==2 && col==3 && invSearch("floral key")==true)
   {
    gardendoor=true;
    noeast=false;
    return "You successfully unlock and open the door."; 
   }
   else if(row==2 && col==3 && invSearch("floral key")==false)
   return "It won't open.";
   
   if(row==0 && col==1 && invSearch("playroom key")==true)
   {
   playdoor=true;
   nowest=false;
   return "You successfully unlock and open the door.";
   }
   else if (row==0 && col==1 && invSearch("playroom key")==false)
   return "It won't open.";
   
   if(row==0 && col==2 && invSearch("front door key")==true)
   {
     nonorth=false;
    lastdoor=true;
    return "You successfully unlock and open the door.";
   }
   else if (row==0 && col==2 && invSearch("front door key")==false)
   return "It won't open.";
   
   

   else
   return "There is no door to be opened.";
   
   
   
   
   
}





















public void noDir()
{
  if(row==2 && col==2)
  {
   if(firstdoor==false)
  nonorth=true;
  noeast=true;
  nosouth=true;
  nowest=true;
  }
 
  else if(row==2 && col==1 || row==2 && col==0)
  {
  noeast=true;
  nosouth=true;
  nowest=true;
  nonorth=false;
  }
 
  else if(row==2 && col==3)
  {
   if(gardendoor==false)
   noeast=true;
   else
   noeast=false;
   nosouth=true;
   nowest=true;
   nonorth=false;
  }
 
  else if(row==2 && col==4 || row==0 && col==3)
  {
   nonorth=true;
   nosouth=true;
   noeast=true;
   nowest=false;
  }
  else if (row==1 && col==0)
  {
   nowest=true;
   nonorth=true;
   noeast=false;
   nosouth=false;
  }
  else if(row==1 && col==3)
  {
  nonorth=true;
  nosouth=false;
  noeast=false;
  nowest=false;
  }
 
  else if(row==1 && col==4)
  {
   noeast=true;
   nosouth=true;
   nowest=false;
   nonorth=false;
  }
 
  else if(row==0 && col==4)
  {
   nonorth=true;
   noeast=true;
   nowest=true;
   nosouth=false;
  }
  else if(row==0 && col==2)
  {
  if(lastdoor==false)
  nonorth=true;
  else
  nonorth=false;
  nowest=true;
  noeast=false;
  nosouth=false;
  }
  else if(row==0 && col==1)
  {
   if (playdoor==false)
   nowest=true;
   else
   nowest=false;
   noeast=true;
   nonorth=true;
   nosouth=false;
  }
  else if(row==0 && col==0)
  {
   nonorth=true;
   nosouth=true;
   nowest=true;
   noeast=false;
  }
  else
  {
   nonorth=false;
   nosouth=false;
   noeast=false;
   nowest=false;
  }
}



























class Object
{
  String name;
  int pos;
  int room;
  boolean used;
  boolean solo;
  String item;
  String contents;
  String description;
 
  public Object(String n, int p, boolean u,boolean s,int r,String i,String c, String d)
  {
   name=n;
   used=u;
   solo=s;
   pos=p;
   room=r;
   item=i;
   contents=c;
   description=d;
  }
 
 
  public String getname(){
  return name;
  }
 
  public boolean getSolo(){
   return solo;
  }
 
  public int getPos()
  {
   return pos;
  }
 
  public boolean getUsed()
  {
   return used;
  }
  public int getRoom()
  {
   return room;
  }
  public String getItem()
  {
   return item;
  }
  public String getContents()
  {
   return contents;
  }
  public String getDes()
  {
   return description; 
  }
  public void changeItem(String i)
  {
    item=i;
  }
 
  public String action()
  {
  int safe=pos;
  String res=lines[safe];
  String hold[];
  hold=split(name," ");
   used=true;
  
  
  if(name.equals("sink"))
  {
    invRemove("wrench");
    res=res+"#The wrench shatters and is no longer usable.";
    
  }
  
   if(name.equals("barrel") && item.equals("bottle") && barrelReady==true)
   {
     pos=23;
  for(int i=0;i<inventory.length;i++)
  {
   if(inventory[i].equals("bottle"))
   inventory[i]="whiskey bottle";
  }
   used=false;
   
   }
  
  
    if(name.equals("barrel") && item.equals("copper pipe"))
   {
   
  item="bottle";
  used=false;
  pos=23;
  barrelReady=true;
  invRemove("copper pipe");
   }
  
   if(name.equals("scale"))
   { 
   for(int i=0;i<inventory.length;i++)
   {
    if(inventory[i].equals("whiskey bottle"))
    {
     inventory[i]="bottle";
     break;
    }
   }
   
   if(scalecount<2)
   {
  used=false;
  scalecount++;
  
  if(scalecount==2)
  pos=25;
   }
  
   else if(scalecount==2)
   {
   for(int i=0;i<inventory.length;i++)
  {
   if(inventory[i].equals(""))
   {
   inventory[i]="crowbar";
   break;
   }
  }
   }
   }
   
   if(hold.length>1)
   {
   if(hold[1].equals("car"))
   {
    if(carSmashed==false)
    carSmashed=true;
    else
    return "You try with all your might but the car won't break.#Since you've spent your energy, you feel that#leaving the room and trying again would help.";
   }
   }
   
   
   if(name.equals("crib"))
   {
     for(int i=0;i<puzzle.length;i++)
     {
     if(store.equals(puzzle[i]))
     {
       String keep=res;
       int l=res.length();
       res=res.substring(0,14)+store+keep.substring(15,l);
       cribcount++;
       
       if(cribcount==3)
       {
         frameOpened=true;
       res=res+"#The protective covering over the frame disappears";
       }
       invRemove(store);
       return res;
     }
     else if(i==puzzle.length-1 && !store.equals(puzzle[i]))
     return "That won't do anything useful.";
     }
   }
   
   if(name.equals("picture frame"))
   {
     for(int i=0;i<picture.length;i++)
     {
      if(store.equals(picture[i]) && invSearch(picture[i])==true)
     {
       if(frameOpened==true)
       {
         if(framed.equals(""))
         {
       String keep=res;
       int l=res.length();
       res=res.substring(0,14)+store+keep.substring(15,l);
       if(store.equals("baby photo"))
       {
       res=res+"#A latch opens up behind the frame and you find a key.#This key may lead you through the front door.";
       for(int j=0;j<inventory.length;j++)
       {
        if(inventory[j].equals(""))
        {
        inventory[j]="front door key";
        break;
        }
       }
       }
       else
       res=res+"#Nothing in particular happens.";
       
       invRemove(store);
       framed=store;
       return res;
         }
         else
         return "There is already a photo in the frame.";
       }
       else
       return "You try putting the photo in the frame,but the protective#covering prevents you from doing so.";
      
      
     }
     }
   }
   
   
  
  
  
  for(int i=0;i<lines[safe].length();i++)
  {
   if(res.substring(i,i+1).equals("*"))
  {
   String check=res;
   int l=res.length();
   res=res.substring(0,i)+name+check.substring(i+1,l);
  }  
  }
    if(res.substring(res.length()-1,res.length()).equals("^"))
  {
    String check="";
    
    if(contents.equals(""))
    check="#Nothing in particular happens.";
    else
    check="#Within the busted engine, you find a playroom key.#The crowbar shatters and is no longer usable.";
    invRemove("crowbar");
    res=res.substring(0,res.length()-1)+check;
  }
  
  
   return res;
  }
 
 
 
 
 
}







public String Response(String input)
{
  if (gamestart==true && savecheck==false)
  {
  //Moving East
  if(input.toLowerCase().equals("go east") || input.toLowerCase().equals("east") || input.toLowerCase().equals("e"))
  {
    noDir();
  if(noeast==false)
  {
  col++;
  return Map[row][col];
  }
  
  else if(noeast==true)
  {
    if(row==2 && col==3 && gardendoor==false)
    return "The door is locked tight";
    else
    return "You cannot go that way."; 
  }
  }
  //Moving West
  if(input.toLowerCase().equals("go west") || input.toLowerCase().equals("west") || input.toLowerCase().equals("w"))
  {
    noDir();
  if(nowest==false)
  {
  col--;
  carSmashed=false;
  return Map[row][col];
  }
  else if(nowest=true)
  {
    if(row==0 && col==1)
    return "The door is locked tight";  
    else
    return "You cannot go that way.";
  }
  }

  //Moving South
   if(input.toLowerCase().equals("go south") || input.toLowerCase().equals("south") || input.toLowerCase().equals("s"))
  {
    noDir();
  if(nosouth==false)
  {
  row++;
  return Map[row][col];
  }
  else
  return "You cannot go that way.";
  }

  //Moving North
   if(input.toLowerCase().equals("go north") || input.toLowerCase().equals("north") || input.toLowerCase().equals("n"))
  {
    noDir();
  if(nonorth==false)
  {
  
  if(row==0 && col==2 && lastdoor==true)
  {
    endGame=true;
    return "A blinding light comes through the open door."; 
  }
  else
  {
  row--;
  return Map[row][col];
  }
  }
 
 
  else if(nonorth==true)
  {
  if(row==2 && col==2 && firstdoor==false || row==0 && col==2 && lastdoor==false)
  return "The door is locked tight.";
  
  else
  return "You cannot go that way.";
  }
  }


  //Unlocking doors
  if(input.toLowerCase().equals("open door") || input.toLowerCase().equals("unlock door"))
  return unlock();



  //Looking around
  if(input.toLowerCase().equals("look") || input.toLowerCase().equals("look around"))
  {
  String res="";
  res=res+Map[row][col]+"#";
  for(int i=0;i<containers.length;i++)
  {
   if(row==(containers[i].getRoom()-(containers[i].getRoom()%10))/10 && col==containers[i].getRoom()%10)
   res=res+"You can see a "+containers[i].getname()+".#";
  }
 
  for(int j=0;j<objects.length;j++)
  {
  if(row==(objects[j].getRoom()-(objects[j].getRoom()%10))/10 && col==objects[j].getRoom()%10)
  {
    res=res+"You can see a "+objects[j].getname()+".#";
  }
  
  }
 
   return res;
  }

//Looking at containers
  for(int i=0;i<containers.length;i++)
  {
  if(input.toLowerCase().equals("look at "+containers[i].getname()))
  {
    if(row==(containers[i].getRoom()-(containers[i].getRoom()%10))/10 && col==containers[i].getRoom()%10)
    {
   String check="";
   if(containers[i].ifOpened()==true)
   {
   if(invSearch(containers[i].getContents())==true || containers[i].getContents().equals(""))
   check="open.#It has nothing in it.";
   else
   check="open.#It has "+containers[i].getContents()+" in it.";
   }
   else
   check="closed.";
   return "The "+containers[i].getname()+" is "+check;
    }
    else
    return "I can't see that.";
 
  }
  }
  
  //Looking at objects
  for(int i=0;i<objects.length;i++)
  {
    if(input.toLowerCase().equals("look at "+objects[i].getname()))
    {
      if(row==(objects[i].getRoom()-(objects[i].getRoom()%10))/10 && col==objects[i].getRoom()%10)
      return objects[i].getDes();
      else
      return "I can't see that.";
    }
  }








  //Opening containers
  for(int i=0;i<containers.length;i++)
  {
  if(input.toLowerCase().equals("open "+containers[i].getname()) || input.toLowerCase().equals("look in "+containers[i].getname()))
  {
  if(containers[i].ifOpened()==false)
  return containers[i].open();
  else
  return "That has already been opened";
  }
  }


  //Taking objects
  for(int i=0;i<containers.length;i++)
  {
   if(input.toLowerCase().equals("take "+containers[i].getContents()) && row*10+col!=20)
   {
     
     if(row==(containers[i].getRoom()-(containers[i].getRoom()%10))/10 && col==containers[i].getRoom()%10)
     {
   if(containers[i].ifOpened()==true)
   {
   
   if(containers[i].getname().equals("large shelf") && ladder.getUsed()==false)
   return "You can't reach it.";
   
   
   for(int j=0;j<inventory.length;j++)
   {
  if(inventory[j]==containers[i].getContents())
  return "You have already taken that.";
   }
   for(int k=0;k<inventory.length;k++)
   {
  if(inventory[k].equals(""))
  {
  inventory[k]=containers[i].getContents();
  return "Took the "+containers[i].getContents()+".";
  }
  }
   }
    else
   return "I can't see that.";
     }
       else
   return "I can't see that.";
   
   }
  }
  
  for(int i=0;i<picture.length;i++)
  {
    if(input.toLowerCase().equals("take "+picture[i]) && !framed.equals("") && row==2 && col==0)
    {
     for(int j=0;j<inventory.length;j++)
    {
     if(inventory[j].equals(""))
     {
       String pin=framed;
     inventory[j]=framed;
     framed="";
     return "Took the "+pin;
     }
    } 
      
    }
  }


  //Using inventory objects
  for(int i=0;i<inventory.length;i++)
  {
  if (inventory[i].equals(""))
  break;
  for(int j=0;j<objects.length;j++)
  {
  if(input.toLowerCase().equals("use "+inventory[i]+" on "+objects[j].getname()))
  {
    if(row==(objects[j].getRoom()-(objects[j].getRoom()%10))/10 && col==objects[j].getRoom()%10)
  return use(inventory[i],objects[j]);
  else
  return "I can't see that.";
  }
  else if (input.toLowerCase().equals("use "+objects[j].getname()))
  {
    if(row==(objects[j].getRoom()-(objects[j].getRoom()%10))/10 && col==objects[j].getRoom()%10)
  return useSolo(objects[j]);
  else
  return "I can't see that.";
  }
  }
  }
 
  //Reading inventory objects
 
  for(int i=0;i<inventory.length;i++)
  {
   if(input.toLowerCase().equals("read "+inventory[i]) ||  input.toLowerCase().equals("use "+inventory[i]))
   {
  if(inventory[i].equals("gold book"))
  return "The pages are blank, but the volume of the book reads: 874";
  
  else if(inventory[i].equals("newspaper"))
  return "The headline reads:#Car crash on April 21.#The picture shows two busted cars,#but you can't make out what colour they are.";
   
   }
  
  
  
  
  }
 
 
  //Entering numeric resposnses
  for(int i=0;i<=9;i++)
  {
  if(input.substring(0,1).equals(String.valueOf(i)))
  {
  if((input.toLowerCase().equals("15 45 23") || input.toLowerCase().equals("154523")) && row==2 && col==1)
  {
   safeUnlocked=true;
   return "You enter the right combination and unlock the safe.";
  }
  else if(!input.toLowerCase().equals("15 45 23") && !input.toLowerCase().equals("154523") && row==2 && col==1)
  return "That is the wrong combination";
 
  if((input.toLowerCase().equals("421") || input.toLowerCase().equals("4 2 1")) && row==0 && col==1&& remote.getUsed()==true)
   return "The TV shows two busted cars, one red and one white.";
  else if (!input.toLowerCase().equals("421") && !input.toLowerCase().equals("4 2 1") && row==0 && col==1 && remote.getUsed()==true)
  return "Nothing but static";
  else if(row==0 && col==1 && remote.getUsed()==false)
  return "The remote does not work.#Maybe it needs some batteries?";
  
  if((input.toLowerCase().equals("874") || input.toLowerCase().equals("8 7 4")) && row==1 && col==0)
  {
  poboxOpened=true;
  return pobox.open();
  }
  else if ((!input.toLowerCase().equals("874") && !input.toLowerCase().equals("8 7 4")) && row==1 && col==0)
  return "You look inside that PO box and find nothing of value.";
  }
  
  if(row==1 && col==0 && input.length()>=12)
  {
  if(input.substring(12,13).equals(String.valueOf(i)) && input.substring(0,12).equals("open po box "))
  {
   if(input.toLowerCase().equals("open po box 874") || input.toLowerCase().equals("open po box 8 7 4"))
  {
    poboxOpened=true;
    return pobox.open();
  }
  else
  return "You look inside that PO box and find nothing of value.";    
  }
  }
  }
  
  
  

  //Bringing up player inventory
  if(input.toLowerCase().equals("inventory") || input.toLowerCase().equals("inv") || input.toLowerCase().equals("i"))
  {
  String inv="Your inventory contains: ";
  for(int i=0;i<inventory.length;i++)
  {
    if(!inventory[i].equals(""))
    inv=inv+inventory[i]+",#";
  }
  return inv;
  }

  //Help Menu
  if(input.toLowerCase().equals("help") || input.toLowerCase().equals("h"))
  {
  return lines[17];
  }



  } // The end of all in-game responses.




  //Creating a New Game
  if(input.toLowerCase().equals("new") && loadgame==false)
  {
  entername=true;
  return "Enter your name to start a new game!#Type 'back' if you would rather load an existing game.";
  }

  //Cancelling a New Game
  if(input.toLowerCase().equals("back") && entername==true)
  {
   entername=false;
   return "Decided not to create a new game.";
  }

  //Entering a name for save file
  if(entername==true && !input.toLowerCase().equals("back"))
  {
   name=input;
 
   for(int i=0;i<name.length();i++)
   {
  if(!name.substring(i,i+1).equals(" "))
  {
  entername=false;
  namecheck=true;
  return "Are you sure you want "+name+" to be your name?";
  }
   else if(i==name.length()-1 && name.substring(i,i+1).equals(" "))
   return "Your name must have at least one character.";
  
   }
 }
  //Double checking your name
  if(namecheck==true)
  {
  if(input.toLowerCase().equals("no"))
  {
   name="";
   namecheck=false;
   entername=true;
   return "Enter your name to start a new game!#Type 'back' if you would rather load an existing game.";
  }
  if (input.toLowerCase().equals("yes"))
  {
   namecheck=false;
   gamestart=true;
   return lines[0];
  }
  }

  //Saving Progress:
  if(input.toLowerCase().equals("save") && gamestart==true)
  {
  savecheck=true;
  return "Are you sure you would like to save your game?";
  }

  if(savecheck==true)
  {
  if(input.toLowerCase().equals("no"))
  {
   savecheck=false;
   return "Decided not to save the game.";
  }
 
  if(input.toLowerCase().equals("yes"))
  {
  savecheck=false;
  save=true;
  return "The game has been saved!";
  }
  }

  // Loading Progress
  if(input.toLowerCase().equals("load") && entername==false)
 {
   loadgame=true;
   return "Enter the name of the game you would like to load.#Press 'back' if you would rather start a new game.";
  }

  if(loadgame==true && loadcheck==false)
  {
   if(fileExists(sketchPath(input.toLowerCase()+".ini"))==true && !input.toLowerCase().equals("back"))
   {
   loadgame=false;
   loadcheck=true;
   loadname=input.toLowerCase();
   return "Are you sure you want to load this game?";
   }
   else if(fileExists(sketchPath(input.toLowerCase()+".ini"))==false && !input.toLowerCase().equals("back"))
   return "There is no save file by this name.";
 
   if(input.toLowerCase().equals("back"))
   {
  loadgame=false;
  return "Decided not to load a game.";
   }
  }
  if (loadcheck==true)
  {
  if(input.toLowerCase().equals("no"))
  {
   loadcheck=false;
   loadname="";
   return "Decided not to load this game.";
  }
 
  if(input.toLowerCase().equals("yes"))
  {
  loadcheck=false;
  loadGame(loadname+".ini");
  loadname="";
  gamestart=true;
  return "Loaded successfully!";
  }
  }
  
  if(input.toLowerCase().equals("exit") && exitcheck==false)
  {
   exitcheck=true;
  return "Are you sure you want to quit the game?#All unsaved progress will be lost!"; 
  }
  
  if(exitcheck==true)
  {
   if(input.toLowerCase().equals("no"))
  {
   exitcheck=false;
   return "Decided not to quit the game.";
  } 
    if(input.toLowerCase().equals("yes"))
    exit();
  }
  
  return "I don't understand.";
}








public String use(String item, Object object)
{
  if(object.equals(door))
  return unlock();
  else if(object.equals(crib))
  {
  store=item;
  return crib.action();
  }
  else if (object.equals(frame))
  {
   store=item;
  return frame.action(); 
  }
  
  else
  {
   if(object.getSolo()==false)
   {
   if(item.equals(object.getItem()))
   {
   for(int i=0;i<inventory.length;i++)
   {
     if(inventory[i].equals("") && !object.getContents().equals(""))
     {
     if(!object.getname().equals("white car") || (object.getname().equals("white car") && carSmashed==false))
     inventory[i]=object.getContents();
     
     break;
     }
   }
   return object.action();
   }
   else
   return "You can't do that.";
   }
  else
  return "You don't need an item for that.";
  }
  //return "You can't do that.";
}

public String useSolo(Object object)
{
  if(object.getSolo()==true)
  return object.action();
 
  if(object.getname().equals("remote"))
  return"Which channel do you want?";
  return "You can't do that.";
}
  public void settings() {  size(800,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Somnolence" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
