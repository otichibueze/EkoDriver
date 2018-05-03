using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;




namespace EkoDriver
{
   public class Car
    {
       public int type;
      // public Color color;
       public float speed;
       public float x;
       public float y;

       public Car(int type,float speed,float x, float y)
       {
           this.type = type;
           //this.color = color;
           this.speed = speed;
           this.x = x;
           this.y = y;
       }

       public const int AudiA2 = 0,AudiA6 = 1,AudiR8 = 2,AudiS5 = 3, BmwX6 = 4 ,Camaro = 5,Crossfire = 6,
                       Crvblack = 7,Crvgreen= 8, accordblack = 9, Danfo = 10,Dogde = 11,F250 = 12,accordblue = 13,
                       Fj = 14, accordbrown = 15,accorddarkblue = 16,mazda6 = 17,mazdaR8 = 18,seat = 19,
                       Tunderred = 20,accordgreen = 21,Molelue = 22,accordpink = 23,accordred = 24,accordwhite = 25,
                       accordyellow = 26, corrolablue = 27, corrolalightblue = 28, corrolapink = 29, corrolayellow = 30,
                       Truckbrown = 31, Truckred = 32, Truckblue = 33, Getlife = 34,  BRTRed = 35, BRTBlue = 36;
                                     
    }
}
