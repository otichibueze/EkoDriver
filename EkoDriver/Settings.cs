using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using System.IO.IsolatedStorage;
using System.Xml.Serialization;
using System.IO;

namespace EkoDriver
{
   public class Settings
    {
         const string filename = "settings.xml";
        
       // Application settings
       // put get and set for all data you want to save on your game

        //public float SmallCircleX { set; get; }

        //public float SmallCircleY { set; get; }

        public bool Paused { set; get; }

        public bool SoundEnabled { set; get; } 

        public int playercarType { set; get; }

        public bool loadData { set; get; }

        public string Enemycar { set; get; }

        public string Smoke { set; get; }

        public float playercarSpeed { set; get; }

        public float playercarX { set; get; }

        public float playercarY { set; get; }

        public float screenY { set; get; }

        public int scoreValue { set; get; }

        public int Defaultpad { set; get; }

        public int lifes { set; get; }

        public int gamestate { set; get; }

        public string[] highscore { set; get; }
 
        public string[] highscoreName { set; get; }

        public Settings()
        {
            Paused = false;

            Enemycar = " ";

            Smoke = " ";

            screenY = 800;

            scoreValue = 0;

            SoundEnabled = true;

            lifes = 5;

            gamestate = 0;

            Defaultpad = 1;

            highscore = new string[10];
            highscoreName = new string[10];

            highscoreName[0] = "Winston"; highscore[0] = "13500";

            highscoreName[1] = "Garvey"; highscore[1] = "12500";

            highscoreName[2] = "Mr Lee"; highscore[2] = "7500";

            highscoreName[3] = "Ebele"; highscore[3] = "7200";

            highscoreName[4] = "Yinkus"; highscore[4] = "6900";

            highscoreName[5] = "Mr Tee"; highscore[5] = "5100";

            highscoreName[6] = "chinwe"; highscore[6] = "4950";

            highscoreName[7] = "Onochie"; highscore[7] = "3645";

            highscoreName[8] = "Ope"; highscore[8] = "2995";

            highscoreName[9] = "Nicole"; highscore[9] = "2200";
        }


        public void Save()
        {
            IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication();
            IsolatedStorageFileStream stream = storage.CreateFile(filename);
            XmlSerializer xml = new XmlSerializer(GetType());
            xml.Serialize(stream, this); 
            stream.Close(); 
            stream.Dispose();
        }

       public static Settings Load()
        { 
           IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication(); Settings settings;
             if (storage.FileExists(filename))
             {
                
                     IsolatedStorageFileStream stream = storage.OpenFile("settings.xml", FileMode.Open);
                     XmlSerializer xml = new XmlSerializer(typeof(Settings));
                     settings = xml.Deserialize(stream) as Settings;
                     stream.Close();
                     stream.Dispose();
              }
              else
                 {
                 settings = new Settings();
                 }
                   return settings;
                 }

    }
}
