using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Input.Touch;
using Microsoft.Xna.Framework.Media;
using Microsoft.Phone.Shell;
using Microsoft.Devices.Sensors;
using Microsoft.Advertising.Mobile.Xna;



namespace EkoDriver
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;


        Settings settings;

        int viewportX, viewportY;

        const int gameLoading = 0, gameStarting = 1, gameinplay = 2, gameOver = 3, gameScores = 4, gameOptions = 5, gameSelectCar = 6,
                  gameController = 7, gameCredits = 9, gameSounds = 10, gameHelp = 11;
        const int easy = 1, meduim = 2, hard = 3, harder = 4, hardest = 5, ganstar = 6;
        const int easyDrive = 1200, mediumDrive = 1100, hardDrive = 1000, HarderDrive = 900, hardestDrive = 800, ganstarDrive = 600;
        const int spad = 1, apad = 2;

        Texture2D prestart, prestart1,start, pausedbackground;

        Texture2D background;

        Texture2D[] GameCar;

        Car playersCar;
        List<Car>[] enemyCars = new List<Car>[7];

        List<SmokenFire> smokenFire = new List<SmokenFire>();

        float screenY = 800;
        int Defaultpad = 1;

        Vector2 backgroundposition1;

        Vector2 car8position;


        Texture2D[] lifes;


        ///screen Controller vector
        Vector2 bCirclePosition, sCirclePosition;//leftPosition, rightPosition;
        Texture2D bigcircle, smallcircle;//left, right; 


        //accelerometer
        Vector3 accelerometerVector;
        object accelerometerVectorLock = new object();

        //Advert Control
        static AdGameComponent adGameComponent;
        static DrawableAd bannerAd;
        string adUnitID;

        Texture2D scoreimage,speedimage; //Score 
        string score = "0";

        SpriteFont myfont, highfont;
        int myscore = 0;
        int scorevalue = 0;
        string CSpeed = " ";
        float Pspeed;

        int gameState = gameLoading;
        bool Carhit = false, removelife = false;
        int Hitspace = 0;
        int life = 5;
        bool gamepaused, newhighscore, nonewhighscore;

        //Smoke n Fire
        int SmokeCount = 0, NextSmoke = 0;
        Vector2 smokenFirePosition;
        Texture2D[] smoking;


        ///<HighScore Implementation>
        Vector2 highScorePosition, highScorePosition2;

        string newhighscorename; int scoredetails;
        ///</End>
        //Last vectors for add implementation
        Vector2 iScore, iDscore, ilife, iSpeed, iDspeed, iPaused;
        bool loadData = false; 
        ///<Characters Used for all Inputs On eko driver>
        
        //Home Screen Enter
       // Texture2D Enter; Vector2 enterPosition;

        ///Start screen
        Texture2D StartText; Vector2 StartTextPosition;
        Texture2D HighScoreText; Vector2 HighScoreTextPosition;
        Texture2D loadGame; Vector2 loadGameTextPosition;
        Texture2D Options ; Vector2 optionsTextPosition;
        Texture2D Credits; Vector2 creditTextPosition;


        ///HighScore Screen
        Texture2D lblhighscore;

        ///Game Paused
        Texture2D lblPaused;
        Texture2D resume; Vector2 resumeTextPosition;
        Texture2D restart; Vector2 restartTextPosition;
        Texture2D SaveExit; Vector2 SaveExitTextPosition;


        ///Game Over
        Texture2D lblgameover;
        Texture2D retry; Vector2 retryPosition;
        Texture2D Main; Vector2 MainPosition;
        ///End
        ///</End Input>

        //Game Options
        Texture2D lblgameoptions;
        Texture2D Controller; Vector2 ControllerTextPosition;
        Texture2D Help ; Vector2 HelpTextPosition;
        Texture2D Sound; Vector2 SoundPosition;

        //Controller
        Texture2D lblcontroller; Vector2 padPosition;
        Texture2D iControllerA, iControllerB;

        //GameSound
        Texture2D lblsounds; Vector2 bSoundPosition;
        Texture2D btnSoundEnabled, btnSoundDisabled;
        SoundEffect soundEffect; bool played = true, soundEnabled = true, mediaEnabled = true;
        SoundEffect Crash;
        Song song;

        //GameCredit
        Texture2D[] lblcredits;

        //GameTips
        Texture2D[] lblhelp;

        Random value;


        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
            // graphics.SupportedOrientations = DisplayOrientation.LandscapeLeft | DisplayOrientation.Portrait | DisplayOrientation.LandscapeRight;

            graphics.SupportedOrientations = DisplayOrientation.Portrait;
            graphics.PreferredBackBufferWidth = 480;
            graphics.PreferredBackBufferHeight = 800;
            // Frame rate is 30 fps by default for Windows Phone.

            //Advert Control
            AdGameComponent.Initialize(this, "5dc465b9-6ba9-4ff9-841f-eced56db0529");
            adGameComponent = AdGameComponent.Current;
            Components.Add(adGameComponent);


            TargetElapsedTime = TimeSpan.FromTicks(333333);

            // Extend battery life under lock.
            InactiveSleepTime = TimeSpan.FromSeconds(1);


            PhoneApplicationService appService = PhoneApplicationService.Current;
            appService.Launching += OnAppServiceLaunching;
            appService.Activated += OnAppServiceActivated;
            appService.Deactivated += OnAppServiceDeactivated;
            appService.Closing += OnAppServiceClosing;
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here


           // Accerometer
            Accelerometer accelerometer = new Accelerometer();
            accelerometer.ReadingChanged += OnAccelerometerReadingChanged;
            try
            {
                accelerometer.Start();
            }
            catch
            {
            }
            
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            viewportX = GraphicsDevice.Viewport.Width;
            viewportY = GraphicsDevice.Viewport.Height;

            myfont = Content.Load<SpriteFont>("myfont");
            highfont = Content.Load<SpriteFont>("Highfont");
            pausedbackground = Content.Load<Texture2D>("paused");

            TouchPanel.EnabledGestures = GestureType.FreeDrag | GestureType.DragComplete;

            soundEffect = Content.Load<SoundEffect>("RaceCar");
            Crash = Content.Load<SoundEffect>("crash");
            song = Content.Load<Song>("poppintough");
          
            //My random
            value = new Random();

            int id = value.Next(1, 4);

            if (id == 1) adUnitID = "82064";
            else if (id == 2) adUnitID = "82659";
            else if (id == 3) adUnitID = "82660";
            else adUnitID = "82064";

            //const int Ego = 82064, Ego1 = 82659, Ego2 = 82660;

            iDscore = new Vector2(8, 12);
            iDspeed = new Vector2(8, 56);

            iScore = new Vector2(112, 4);
            iSpeed = new Vector2(112, 49);

            ilife = new Vector2(346, 12);
            iPaused = new Vector2(148, 22);
            //Page that shows before game starts
            if (gameState == gameLoading)
            {
                prestart = Content.Load<Texture2D>("Startpage");
                prestart1 = Content.Load<Texture2D>("Loadpage");
                //Enter = Content.Load<Texture2D>("enter");
                //enterPosition = new Vector2(97, 719);
            }

            //When Start Screen is showing
            if (gameState == gameStarting)
            {
               
                start = Content.Load<Texture2D>("Startimage");

                StartText = Content.Load<Texture2D>("btnstart");
                HighScoreText = Content.Load<Texture2D>("btnhighscores");
                Options = Content.Load<Texture2D>("btnoptions");
                loadGame = Content.Load<Texture2D>("btnload");
                Credits = Content.Load<Texture2D>("btncredits");

                StartTextPosition = new Vector2(486, 338);
                loadGameTextPosition = new Vector2(486, 426);
                optionsTextPosition = new Vector2(486, 514);
                HighScoreTextPosition = new Vector2(486, 602);
                creditTextPosition = new Vector2(486, 690);
                
                
                played = true;
            }


            //When game is playing
            if (gameState == gameinplay)
            {
                // ad Control
                bannerAd = adGameComponent.CreateAd(adUnitID, new Rectangle(0, 0, GraphicsDevice.Viewport.Bounds.Width, 80));
                bannerAd.AdRefreshed += new EventHandler(bannerAd_AdRefreshed);
                bannerAd.ErrorOccurred += new EventHandler<Microsoft.Advertising.AdErrorEventArgs>(bannerAd_ErrorOccurred);
               
                resume = Content.Load<Texture2D>("btnresume");
                restart = Content.Load<Texture2D>("btnrestart");
                SaveExit = Content.Load<Texture2D>("btnsaveexit");
                lblPaused = Content.Load<Texture2D>("lblpaused");

               
                resumeTextPosition = new Vector2(486,200);
                restartTextPosition = new Vector2(486,300);
                SaveExitTextPosition = new Vector2(486,400);

                
                lifes = new Texture2D[8];
                lifes[0] = Content.Load<Texture2D>("life1");
                lifes[1] = Content.Load<Texture2D>("life2");
                lifes[2] = Content.Load<Texture2D>("life3");
                lifes[3] = Content.Load<Texture2D>("life4");
                lifes[4] = Content.Load<Texture2D>("life5");
                lifes[5] = Content.Load<Texture2D>("life6");
                lifes[6] = Content.Load<Texture2D>("life7");
                lifes[7] = Content.Load<Texture2D>("life8");

                scoreimage = Content.Load<Texture2D>("score");
                speedimage = Content.Load<Texture2D>("speed");

                background = Content.Load<Texture2D>("newracebackground");

                GameCar = new Texture2D[37];
                GameCar[0] = Content.Load<Texture2D>("Audi-A2"); GameCar[15] = Content.Load<Texture2D>("accord-brown");
                GameCar[1] = Content.Load<Texture2D>("Audi-A-6"); GameCar[16] = Content.Load<Texture2D>("accord-darkblue");
                GameCar[2] = Content.Load<Texture2D>("Audi-R8"); GameCar[17] = Content.Load<Texture2D>("mazda-6");
                GameCar[3] = Content.Load<Texture2D>("Audi-S5"); GameCar[18] = Content.Load<Texture2D>("mazda-R8");
                GameCar[4] = Content.Load<Texture2D>("Bmw-X6"); GameCar[19] = Content.Load<Texture2D>("seat");
                GameCar[5] = Content.Load<Texture2D>("Camaro"); GameCar[20] = Content.Load<Texture2D>("Tunder-red");
                GameCar[6] = Content.Load<Texture2D>("Crossfire"); GameCar[21] = Content.Load<Texture2D>("accord-green");
                GameCar[7] = Content.Load<Texture2D>("Crv-black"); GameCar[22] = Content.Load<Texture2D>("Molelue");
                GameCar[8] = Content.Load<Texture2D>("Crv-green"); GameCar[23] = Content.Load<Texture2D>("accord-pink");
                GameCar[9] = Content.Load<Texture2D>("accord-black"); GameCar[24] = Content.Load<Texture2D>("accord-red");
                GameCar[10] = Content.Load<Texture2D>("Danfo"); GameCar[25] = Content.Load<Texture2D>("accord-white");
                GameCar[11] = Content.Load<Texture2D>("Dogde");
                GameCar[12] = Content.Load<Texture2D>("F250");
                GameCar[13] = Content.Load<Texture2D>("accord-blue");
                GameCar[14] = Content.Load<Texture2D>("Fj"); 

                GameCar[26] = Content.Load<Texture2D>("accord-yellow");
                GameCar[27] = Content.Load<Texture2D>("corrola-blue");
                GameCar[28] = Content.Load<Texture2D>("corrola-lightblue");
               GameCar[29] = Content.Load<Texture2D>("corrola-pink");
               GameCar[30] = Content.Load<Texture2D>("corrola-yellow");
                GameCar[31] = Content.Load<Texture2D>("Truck-brown"); GameCar[32] = Content.Load<Texture2D>("Truck-red");
                GameCar[33] = Content.Load<Texture2D>("Truck-blue"); GameCar[34] = Content.Load<Texture2D>("getlife");
                GameCar[35] = Content.Load<Texture2D>("BRT-Red");
                GameCar[36] = Content.Load<Texture2D>("BRT-Blue");

                smoking = new Texture2D[16];

                smoking[0] = Content.Load<Texture2D>("littleSmoke"); smoking[1] = Content.Load<Texture2D>("littleSmoke1");
                smoking[2] = Content.Load<Texture2D>("littleSmoke2"); smoking[3] = Content.Load<Texture2D>("littleSmoke3");
                smoking[4] = Content.Load<Texture2D>("littleSmoke4"); smoking[5] = Content.Load<Texture2D>("littleSmoke5");
                smoking[6] = Content.Load<Texture2D>("littleSmoke6"); smoking[7] = Content.Load<Texture2D>("littleSmoke7");
                smoking[8] = Content.Load<Texture2D>("mediumSmoke8"); smoking[9] = Content.Load<Texture2D>("mediumSmoke9");
                smoking[10] = Content.Load<Texture2D>("mediumSmoke10"); smoking[11] = Content.Load<Texture2D>("mediumSmoke11");
                smoking[12] = Content.Load<Texture2D>("mediumSmoke12"); smoking[13] = Content.Load<Texture2D>("mediumSmoke13");
                smoking[14] = Content.Load<Texture2D>("mediumSmoke14"); smoking[15] = Content.Load<Texture2D>("mediumSmoke15");
         

                bigcircle = Content.Load<Texture2D>("bigCircle");
                smallcircle = Content.Load<Texture2D>("smallCircle");


                backgroundposition1.X = 0;
                backgroundposition1.Y = 0 - background.Height;


                car8position.X = 220; car8position.Y = 810;


               // leftPosition.X = 13; leftPosition.Y = 646;
               // rightPosition.X = 393; rightPosition.Y = 646;
                bCirclePosition = new Vector2(2, 614);
                sCirclePosition = new Vector2(bCirclePosition.X + (bigcircle.Bounds.Width / 2 - smallcircle.Bounds.Width / 2),
                    bCirclePosition.Y + (bigcircle.Bounds.Height / 2 - smallcircle.Bounds.Height / 2));

                played = true;
            }

            //When game is showing game over
            if (gameState == gameOver)
            {
                // settings = new Settings();
                // newhighscore = true;

                start = Content.Load<Texture2D>("Startimage");

                lblgameover = Content.Load<Texture2D>("lblgameover");
                retry = Content.Load<Texture2D>("gOretry");
                Main = Content.Load<Texture2D>("gOmenu");

                scoreimage = Content.Load<Texture2D>("score");

                retryPosition = new Vector2(486, 462);
                MainPosition = new Vector2(486, 562);

                played = true;
            }

            //when game is showing highscore
            if (gameState == gameScores)
            {
                start = Content.Load<Texture2D>("Startimage");
                lblhighscore = Content.Load<Texture2D>("lblhighscores");
            }

            if (gameState == gameOptions)
            {
                start = Content.Load<Texture2D>("Startimage");
                lblgameoptions = Content.Load<Texture2D>("lbloptions");

                Controller = Content.Load<Texture2D>("btncontroller");
                Sound = Content.Load<Texture2D>("btnsounds");
                Help = Content.Load<Texture2D>("btnhelpnabout");

                ControllerTextPosition = new Vector2(486, 415);
                SoundPosition = new Vector2(486, 515);
                HelpTextPosition = new Vector2(486, 615);

                played = true;
            }

            if (gameState == gameController)
            {
                start = Content.Load<Texture2D>("Startimage");
                lblcontroller = Content.Load<Texture2D>("lblcontroller");

                iControllerA = Content.Load<Texture2D>("btncontrollerenabled");
                iControllerB = Content.Load<Texture2D>("btncontrollerdisabled");
                padPosition = new Vector2(486, 469);

                played = true;
            }

            if (gameState == gameSounds)
            {
                start = Content.Load<Texture2D>("Startimage");
                lblsounds = Content.Load<Texture2D>("lblsounds");

                btnSoundEnabled = Content.Load<Texture2D>("btnsoundenabled");
                btnSoundDisabled = Content.Load<Texture2D>("btnsounddisabled");
                bSoundPosition = new Vector2(486, 469);

                played = true;
            }

            if (gameState == gameCredits)
            {
                 lblcredits  = new Texture2D[7];

                start = Content.Load<Texture2D>("Startimage");
               
                lblcredits[0] = Content.Load<Texture2D>("lblcredits");
                lblcredits[1] = Content.Load<Texture2D>("producer");
                lblcredits[2] = Content.Load<Texture2D>("progammer");
                lblcredits[3] = Content.Load<Texture2D>("physic");
                lblcredits[4] = Content.Load<Texture2D>("maya");
                lblcredits[5] = Content.Load<Texture2D>("chibusoft");
                lblcredits[6] = Content.Load<Texture2D>("contact");
            }

            if(gameState == gameHelp)
            {
                lblhelp = new Texture2D[3]; 
                start = Content.Load<Texture2D>("Startimage");

                lblhelp[0] = Content.Load<Texture2D>("lblhelpnabout");
                lblhelp[1] = Content.Load<Texture2D>("helpstory");
                lblhelp[2] = Content.Load<Texture2D>("chibusoft");


            }
            // TODO: use this.Content to load your game content here
        }

        void bannerAd_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {

            //throw new NotImplementedException();
            iDscore = new Vector2(8, 12);
            iDspeed = new Vector2(8, 56);

            iScore = new Vector2(112, 4);
            iSpeed = new Vector2(112, 49);

            ilife = new Vector2(346, 12);
            iPaused = new Vector2(148, 22);

            //bannerAd = adGameComponent.CreateAd("Image480_80", new Rectangle(0, 0, GraphicsDevice.Viewport.Bounds.Width, 80));
            
        }

        void bannerAd_AdRefreshed(object sender, EventArgs e)
        {
            //throw new NotImplementedException();
                      
                iDscore = new Vector2(8, 85);
                iDspeed = new Vector2(8, 129);

                iScore = new Vector2(112, 79);
                iSpeed = new Vector2(112, 122);

                ilife = new Vector2(346, 85);
                iPaused = new Vector2(148, 92);
          
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            try
            {
                // Allows the game to exit
                if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                {
                    if (gameState == gameinplay)
                    {
                        if (gamepaused == true)
                        {
                            gamepaused = false;
                            resumeTextPosition = new Vector2(486, 200);
                            restartTextPosition = new Vector2(486, 300);
                            SaveExitTextPosition = new Vector2(486, 400);
                            played = true;
                            return;
                        }
                        if (gamepaused == false) { gamepaused = true; return; }
                        //this.Exit();
                    }
                    if (gameState == gameStarting)
                    {
                        // gameState = gameLoading;
                        gamepaused = false;
                        gameState = gameStarting;
                        gameState = gameLoading;
                        this.Exit();
                    }
                    if (gameState == gameOver)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameScores)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameOptions)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameController)
                    {
                        gameState = gameOptions;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameSounds)
                    {
                        gameState = gameOptions;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameCredits)
                    {
                        gameState = gameStarting;
                        LoadContent();
                        return;
                    }
                    if (gameState == gameHelp)
                    {
                        gameState = gameOptions;
                        LoadContent();
                        return;
                    }
                }

                GameTouch();
                PlaySound();

                if (gameState == gameinplay && gamepaused == false)
                {

                    updatebackground(ref backgroundposition1, gameTime);

                    //Updating players car
                    updateCar(gameTime, ref playersCar);

                    //Updating enemy car 
                    if (scorevalue <= 5000) updateEnemyCars(gameTime, easy, easyDrive);
                    if (scorevalue > 5000 && scorevalue <= 14000) updateEnemyCars(gameTime, meduim, mediumDrive);
                    if (scorevalue > 14000 && scorevalue <= 27000) updateEnemyCars(gameTime, hard, hardDrive);
                    if (scorevalue > 27000 && scorevalue <= 35000) updateEnemyCars(gameTime, harder, HarderDrive);
                    if (scorevalue > 35000 && scorevalue <= 53000) updateEnemyCars(gameTime, hardest, hardestDrive);
                    if (scorevalue > 53000) updateEnemyCars(gameTime, ganstar, ganstarDrive);


                    //Update Smoke n fire
                    if (life <= 4) UpdateSmoke(gameTime, playersCar);


                    //Looping to check for collusion
                    for (int i = 0; i < enemyCars.Length; ++i)
                    {
                        for (int j = 0; j < enemyCars[i].Count; ++j)
                        {
                            CollusionDetect(enemyCars[i][j], ref playersCar);
                            if (removelife == true) { enemyCars[i].RemoveAt(j); removelife = false; }
                            
                        }
                    }



                    // TODO: Add your update logic here
                    if (screenY - playersCar.y < 500)
                    {

                        if (life > 0) UpdateScore();
                        CarSpeed(ref playersCar.speed);
                    }
                    if (Defaultpad == 1) steeringControl(ref playersCar.x); //LeftRight(ref playersCar.x);
                    if (Defaultpad == 2)
                    {
                        myaccelerometer(ref playersCar.x);
                    }
                }



                base.Update(gameTime);
            }
            catch (Exception e)
            {
                Guide.BeginShowMessageBox("Opps Error", "Unhandle Exception Has occured on your device, game restarting", new List<string> { "Ok" },
                    0, MessageBoxIcon.Warning,new AsyncCallback(ErrorRestart), null);
            }
        }

        void ErrorRestart(IAsyncResult e)
        {
            settings = new Settings();
            gameState = gameLoading;
            LoadContent();
        }

        void updateCar(GameTime gameTime, ref Car car)
        {
            //Used to update individual car speed using gametime and car speed to calculate
            car.y += ((float)(gameTime.ElapsedGameTime.TotalSeconds * car.speed));
        }

        void smokeSpeed(GameTime gameTime, ref SmokenFire smoke)
        {

            smoke.y += ((float)(gameTime.ElapsedGameTime.TotalSeconds * smoke.speed));
            NextSmoke += 1;
            smoke.speed -= 4;
            if (NextSmoke == 7)
            {
                if (smoke.type != 0) smoke.type -= 1;
                NextSmoke = 0;
            }
        }

        void updatebackground(ref Vector2 spritePosition, GameTime gameTime)
        {
            float speed = playersCar.speed;
            //Used to reduce speed of background to make car move faster until its Y psoition is 250
            if (screenY - playersCar.y > 500) { speed -= 100; }
            float distance = (float)(gameTime.ElapsedGameTime.TotalSeconds * speed);
            screenY += distance;
            spritePosition.Y += distance;
            if (spritePosition.Y > 0)
            {
                spritePosition.Y = spritePosition.Y - background.Height;
            }

        }

        void updateEnemyCars(GameTime gameTime, int level, int Drive)
        {
            for (int i = 0; i < enemyCars.Length; ++i)
            {
                //Using to determine the number of cars to be placed in one lane and generated car properties randomly
                while (enemyCars[i].Count < level)
                {
                    int tStart = Car.AudiA2;
                    int tStop = Car.BRTRed;
                    if (i == enemyCars.Length - 1)
                    {
                        tStart = Car.BRTRed;
                        tStop = Car.BRTBlue + 1;
                    }
                    int type = value.Next(tStart, tStop);
                    //Color color = Color.White;
                    int varSpeed = 200;
                    //for (int k = 0; k < enemyCars[i].Count; ++k)
                    //    if (enemyCars[i][k].speed > varSpeed && enemyCars[i][k].y > screenY) varSpeed = enemyCars[i][k].speed + value.Next(0,50);
                    //if (varSpeed > 450) varSpeed = 450;

                    //looping to generate random speed for each enemy car created
                    int speed = (varSpeed) + value.Next(0, 300);
                    //int varX = 0;
                    //if (type == Car.SEDAN) varX = 20;
                    int varX = 5;
                    if (type == Car.BRTBlue) varX = 0;
                    if (type == Car.BRTRed) varX = 0;
                    if(type == Car.Getlife && life >= 8) type = value.Next(tStart, tStop);

                    float x = varX + (70 * i);
                    float varY = screenY;


                    //looping to set them @ speed as same vanishing point (varY) and place them  200 + random value above
                    for (int k = 0; k < enemyCars[i].Count; ++k)
                        if (enemyCars[i][k].y > varY) varY = enemyCars[i][k].y;
                    float y = (varY + 200) + value.Next(200, Drive);
                    enemyCars[i].Add(new Car(type, speed, x, y));
                }

                //Looping to check for collusion if the may collide it set them at same speed
                for (int j = 0; j < enemyCars[i].Count; ++j)
                {
                    for (int k = 0; k < enemyCars[i].Count; ++k)
                    {
                        if (j == k) continue;
                        if (enemyCars[i][j].y > enemyCars[i][k].y && enemyCars[i][j].y - enemyCars[i][k].y <= 250)
                        {
                            enemyCars[i][k].speed = enemyCars[i][j].speed;
                        }
                    }
                }

                //looping to update there speed and remove the one more than 500 from players car
                for (int j = 0; j < enemyCars[i].Count; ++j)
                {
                    Car car = enemyCars[i][j];
                    updateCar(gameTime, ref car);
                    enemyCars[i][j] = car;
                    if (playersCar.y - car.y > 500) enemyCars[i].RemoveAt(j);
                }
            }
        }

        void UpdateSmoke(GameTime gameTime,Car players)
        {
            if(life < 4 )SmokeCount += 1;

            float speed = players.speed;
            float x = players.x;
            float y = (playersCar.y - 3);

            if (life == 1 && SmokeCount == 20)
            {
                int type = SmokenFire.mediumSmoke15;

                        smokenFire.Add(new SmokenFire(type, speed, x, y));
                        SmokeCount = 0;
             }
            else if (life == 2 && SmokeCount == 25)
            {
                int type = SmokenFire.mediumSmoke12;

                smokenFire.Add(new SmokenFire(type, speed, x, y));
                SmokeCount = 0;
            }
            else if (life == 3 && SmokeCount == 35)
            {
                int type = SmokenFire.littleSmoke7;

                smokenFire.Add(new SmokenFire(type, speed, x, y));
                SmokeCount = 0;
            }
           

            for (int j = 0; j < smokenFire.Count; ++j)
            {
                SmokenFire smoke = smokenFire[j];
                smokeSpeed(gameTime, ref smoke);
                if (smoke.type == 0) smokenFire.RemoveAt(j);
            }
            }
        
        void PlaySound()
        {
            if (gameState == gameStarting && played == true && soundEnabled == true)
            {
                soundEffect.Play();
                played = false;
            }

            else if (gameState == gameOptions && played == true && soundEnabled == true)
            {
                soundEffect.Play();
                played = false;
            }

            else if (gameState == gameController && played == true && soundEnabled == true)
            {
                soundEffect.Play();
                played = false;
            }

            else if (gameState == gameSounds && played == true && soundEnabled == true)
            {
                soundEffect.Play();
                played = false;
            }

            else if (gamepaused == true && played == true && soundEnabled == true )
            {
                soundEffect.Play();
                played = false;
            }

            if (gameState != gameLoading && mediaEnabled == true)
            {
                if (soundEnabled == true)
                {
                    MediaPlayer.Volume = 0.1f;
                    MediaPlayer.IsRepeating = true;
                    MediaPlayer.Play(song);
                    mediaEnabled = false;
                }
            }

            if (soundEnabled == false) { MediaPlayer.Stop(); mediaEnabled = true; }
           
        }

        void RestartGame()
        {

            screenY = 800;
            playersCar = new Car(Car.Camaro, 550, 220, 0);
            enemyCars = new List<Car>[7];
            for (int i = 0; i < enemyCars.Length; ++i) enemyCars[i] = new List<Car>();

            smokenFire = new List<SmokenFire>();

           
            Hitspace = 0;
            Carhit = false;
            newhighscore = true;
            scorevalue = 0;
            nonewhighscore = false;
        }

        void drawSmoke(SmokenFire smoke)
        {
            smokenFirePosition.X = smoke.x - 18;
            smokenFirePosition.Y = screenY - smoke.y;
            Texture2D smokenFireTexture;

            if (smoke.type == SmokenFire.littleSmoke) smokenFireTexture = smoking[0];
            else if (smoke.type == SmokenFire.littleSmoke1) smokenFireTexture = smoking[1];
            else if (smoke.type == SmokenFire.littleSmoke2) smokenFireTexture = smoking[2];
            else if (smoke.type == SmokenFire.littleSmoke3) smokenFireTexture = smoking[3];
            else if (smoke.type == SmokenFire.littleSmoke4) smokenFireTexture = smoking[4];
            else if (smoke.type == SmokenFire.littleSmoke5) smokenFireTexture = smoking[5];
            else if (smoke.type == SmokenFire.littleSmoke6) smokenFireTexture = smoking[6];
            else if (smoke.type == SmokenFire.littleSmoke7) smokenFireTexture = smoking[7];
            else if (smoke.type == SmokenFire.mediumSmoke8) smokenFireTexture = smoking[8];
            else if (smoke.type == SmokenFire.mediumSmoke9) smokenFireTexture = smoking[9];
            else if (smoke.type == SmokenFire.mediumSmoke10) smokenFireTexture = smoking[10];
            else if (smoke.type == SmokenFire.mediumSmoke11) smokenFireTexture = smoking[11];
            else if (smoke.type == SmokenFire.mediumSmoke12) smokenFireTexture = smoking[12];
            else if (smoke.type == SmokenFire.mediumSmoke13) smokenFireTexture = smoking[13];
            else if (smoke.type == SmokenFire.mediumSmoke14) smokenFireTexture = smoking[14];
            else if (smoke.type == SmokenFire.mediumSmoke15) smokenFireTexture = smoking[15];
            else smokenFireTexture = null;

            if (smokenFireTexture != null)
            {
                spriteBatch.Begin();
                spriteBatch.Draw(smokenFireTexture, smokenFirePosition, Color.White);
                spriteBatch.End();
            }
        }

        void drawCar(Car car)
        {
            //Used to update vector before drawing individual car
            car8position.X = car.x;
            //Getting diference from view vanishing point to position vector.Y
            car8position.Y = screenY - car.y;


            if (car8position.Y < -220) return;
            Texture2D carTexture;
            if (car.type == Car.AudiA2)
            {
                carTexture = GameCar[0];
            }
            else if (car.type == Car.AudiA6)
            {
                carTexture = GameCar[1];
            }

            else if (car.type == Car.AudiR8)
            {
                carTexture = GameCar[2];
            }

            else if (car.type == Car.AudiS5)
            {
                carTexture = GameCar[3];
            }

            else if (car.type == Car.BmwX6)
            {
                carTexture = GameCar[4];
            }
            else if (car.type == Car.Camaro)
            {
                carTexture = GameCar[5];
            }
            else if (car.type == Car.Crossfire)
            {
                carTexture = GameCar[6];
            }

            else if (car.type == Car.Crvblack)
            {
                carTexture = GameCar[7];
            }

            else if (car.type == Car.Crvgreen)
            {
                carTexture = GameCar[8];
            }

            else if (car.type == Car.accordblack)
            {
                carTexture = GameCar[9];
            }
            else if (car.type == Car.Danfo)
            {
                carTexture = GameCar[10];
            }
            else if (car.type == Car.Dogde)
            {
                carTexture = GameCar[11];
            }

            else if (car.type == Car.F250)
            {
                carTexture = GameCar[12];
            }

            else if (car.type == Car.accordblue)
            {
                carTexture = GameCar[13];
            }
            else if (car.type == Car.Fj)
            {
                carTexture = GameCar[14];
            }
            else if (car.type == Car.accordbrown)
            {
                carTexture = GameCar[15];
            }
            else if (car.type == Car.accorddarkblue)
            {
                carTexture = GameCar[16];
            }
            else if (car.type == Car.mazda6)
            {
                carTexture = GameCar[17];
            }

            else if (car.type == Car.mazdaR8)
            {
                carTexture = GameCar[18];
            }

            else if (car.type == Car.seat)
            {
                carTexture = GameCar[19];
            }

            else if (car.type == Car.Tunderred)
            {
                carTexture = GameCar[20];
            }
            else if (car.type == Car.accordgreen)
            {
                carTexture = GameCar[21];
            }
            else if (car.type == Car.Molelue)
            {
                carTexture = GameCar[22];
            }
            else if (car.type == Car.accordpink)
            {
                carTexture = GameCar[23];
            }
            else if (car.type == Car.accordred)
            {
                carTexture = GameCar[24];
            }
            else if (car.type == Car.accordwhite)
            {
                carTexture = GameCar[25];
            }

            else if (car.type == Car.accordyellow)
            {
                carTexture = GameCar[26];
            } 
            else if (car.type == Car.corrolablue)
            {
                carTexture = GameCar[27];
            }
            else if (car.type == Car.corrolalightblue)
            {
                carTexture = GameCar[28];
            }
            else if (car.type == Car.corrolapink)
            {
                carTexture = GameCar[29];
            }
            else if (car.type == Car.corrolayellow)
            {
                carTexture = GameCar[30];
            }
            else if (car.type == Car.Truckbrown)
            {
                carTexture = GameCar[31];
            }
            else if (car.type == Car.Truckred)
            {
                carTexture = GameCar[32];
            }

            else if (car.type == Car.Truckblue)
            {
                carTexture = GameCar[33];
            }

            else if (car.type == Car.Getlife)
            {
                carTexture = GameCar[34];
            }
            else if (car.type == Car.BRTRed)
            {
                carTexture = GameCar[35];
            }
            else if (car.type == Car.BRTBlue)
            {
                carTexture = GameCar[36];
            }
            else 
            {
                carTexture = GameCar[5];
            }


            spriteBatch.Begin();
            spriteBatch.Draw(carTexture, car8position, Color.White);
            spriteBatch.End();
        }
        
        protected override void Draw(GameTime gameTime)
        {
            try
            {
                GraphicsDevice.Clear(Color.Transparent);

                // TODO: Add your drawing code here
                if (gameState == gameLoading)
                {
                    PrestartingDraw();
                }

                if (gameState == gameStarting)
                {
                    spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.End();

                    if (loadData == true)
                    {
                        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                        spriteBatch.Draw(StartText, StartTextPosition, Color.White);
                        spriteBatch.Draw(loadGame, loadGameTextPosition, Color.White);
                        spriteBatch.Draw(Options, optionsTextPosition, Color.White);
                        spriteBatch.Draw(HighScoreText, HighScoreTextPosition, Color.White);
                        spriteBatch.Draw(Credits, creditTextPosition, Color.White);
                        spriteBatch.End();

                        if (StartTextPosition.X > 27) StartTextPosition.X -= 18;
                        if (loadGameTextPosition.X > 27) loadGameTextPosition.X -= 15;
                        if (optionsTextPosition.X > 27) optionsTextPosition.X -= 12;
                        if (HighScoreTextPosition.X > 27) HighScoreTextPosition.X -= 9;
                        if (creditTextPosition.X > 29) creditTextPosition.X -= 8;

                    }
                    else if (loadData == false)
                    {
                        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                        spriteBatch.Draw(StartText, StartTextPosition, Color.White);
                        // spriteBatch.Draw(loadGame, loadGameTextPosition, Color.White);
                        spriteBatch.Draw(Options, loadGameTextPosition, Color.White);
                        spriteBatch.Draw(HighScoreText, optionsTextPosition, Color.White);
                        spriteBatch.Draw(Credits, HighScoreTextPosition, Color.White);
                        spriteBatch.End();


                        if (StartTextPosition.X > 27) StartTextPosition.X -= 18;
                        if (loadGameTextPosition.X > 27) loadGameTextPosition.X -= 15;
                        if (optionsTextPosition.X > 27) optionsTextPosition.X -= 12;
                        if (HighScoreTextPosition.X > 27) HighScoreTextPosition.X -= 9;
                        // if (HighScoreTextPosition.X > 27) HighScoreTextPosition.X -= 9;
                    }
                }


                if (gameState == gameinplay)
                {
                    spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
                    spriteBatch.Draw(background, backgroundposition1, Color.White);
                    spriteBatch.End();

                    backgroundposition1.Y += background.Height;
                    spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
                    spriteBatch.Draw(background, backgroundposition1, Color.White);
                    spriteBatch.End();
                    backgroundposition1.Y -= background.Height;


                    for (int i = 0; i < enemyCars.Length; ++i)
                    {
                        for (int j = 0; j < enemyCars[i].Count; ++j)
                        {
                            drawCar(enemyCars[i][j]);
                        }
                    }


                    if (Carhit == true && gamepaused == false)
                    {
                        CarIntersected();
                    }
                    else
                    {
                        drawCar(playersCar);

                    }

                    if (life < 4)
                    {
                        for (int j = 0; j < smokenFire.Count; ++j)
                        {
                            drawSmoke(smokenFire[j]);
                        }

                    }

                    if (Defaultpad == 1)
                    {
                        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                        //spriteBatch.Draw(left, leftPosition, Color.White * 0.5f);
                        //spriteBatch.Draw(right, rightPosition, Color.White * 0.5f);
                        spriteBatch.Draw(bigcircle, bCirclePosition, Color.White * 0.2f);
                        spriteBatch.End();

                        spriteBatch.Begin();
                        spriteBatch.Draw(smallcircle, sCirclePosition, Color.Yellow * 0.8f);
                        spriteBatch.End();
                    }

                    spriteBatch.Begin();
                    spriteBatch.Draw(scoreimage, iDscore, Color.White);//12
                    spriteBatch.Draw(speedimage, iDspeed, Color.White);//56
                    spriteBatch.End();

                    spriteBatch.Begin();
                    spriteBatch.DrawString(myfont, score, iScore, Color.Orange);//4
                    spriteBatch.DrawString(myfont, CSpeed, iSpeed, Color.Yellow);//49
                    spriteBatch.End();

                    DrawLifes();

                    if (gamepaused == true)
                    {
                        spriteBatch.Begin();
                        spriteBatch.Draw(pausedbackground, new Rectangle(0, 0, viewportX, viewportY), Color.White * 0.8f);
                        spriteBatch.Draw(lblPaused, iPaused, Color.White);
                        spriteBatch.End();

                        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                        spriteBatch.Draw(resume, resumeTextPosition, Color.White);
                        spriteBatch.Draw(restart, restartTextPosition, Color.White);
                        spriteBatch.Draw(SaveExit, SaveExitTextPosition, Color.White);
                        spriteBatch.End();

                        if (resumeTextPosition.X > 75) resumeTextPosition.X -= 18;
                        if (restartTextPosition.X > 75) restartTextPosition.X -= 15;
                        if (SaveExitTextPosition.X > 75) SaveExitTextPosition.X -= 12;

                    }
                }

                if (gameState == gameOver)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.End();

                    spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                    spriteBatch.Draw(lblgameover, new Vector2(87, 337), Color.White);
                    spriteBatch.Draw(scoreimage, new Vector2(124, 414), Color.White);
                    spriteBatch.End();

                    spriteBatch.Begin();
                    spriteBatch.DrawString(myfont, score, new Vector2(240, 407), Color.Black);
                    spriteBatch.End();

                    spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
                    spriteBatch.Draw(retry, retryPosition, Color.White);
                    spriteBatch.Draw(Main, MainPosition, Color.White);
                    spriteBatch.End();

                    if (retryPosition.X > 64) retryPosition.X -= 18;
                    if (MainPosition.X > 64) MainPosition.X -= 15;
                }

                if (gameState == gameScores)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblhighscore, new Vector2(80, 279), Color.White);
                    spriteBatch.End();

                    spriteBatch.Begin();
                    highScorePosition.X = 340; highScorePosition.Y = 343; highScorePosition2.X = 18; highScorePosition2.Y = 343;
                    spriteBatch.DrawString(highfont, settings.highscore[0], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 1." + settings.highscoreName[0], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 1)); highScorePosition2.Y = (343 + (45 * 1));
                    spriteBatch.DrawString(highfont, settings.highscore[1], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 2." + settings.highscoreName[1], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 2)); highScorePosition2.Y = (343 + (45 * 2));
                    spriteBatch.DrawString(highfont, settings.highscore[2], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 3." + settings.highscoreName[2], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 3)); highScorePosition2.Y = (343 + (45 * 3));
                    spriteBatch.DrawString(highfont, settings.highscore[3], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 4." + settings.highscoreName[3], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 4)); highScorePosition2.Y = (343 + (45 * 4));
                    spriteBatch.DrawString(highfont, settings.highscore[4], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 5." + settings.highscoreName[4], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 5)); highScorePosition2.Y = (343 + (45 * 5));
                    spriteBatch.DrawString(highfont, settings.highscore[5], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 6." + settings.highscoreName[5], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 6)); highScorePosition2.Y = (343 + (45 * 6));
                    spriteBatch.DrawString(highfont, settings.highscore[6], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 7." + settings.highscoreName[6], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 7)); highScorePosition2.Y = (343 + (45 * 7));
                    spriteBatch.DrawString(highfont, settings.highscore[7], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 8." + settings.highscoreName[7], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 8)); highScorePosition2.Y = (343 + (45 * 8));
                    spriteBatch.DrawString(highfont, settings.highscore[8], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, " 9." + settings.highscoreName[8], highScorePosition2, Color.Black);

                    highScorePosition.Y = (343 + (45 * 9)); highScorePosition2.Y = (343 + (45 * 9));
                    spriteBatch.DrawString(highfont, settings.highscore[9], highScorePosition, Color.Black);
                    spriteBatch.DrawString(highfont, "10." + settings.highscoreName[9], highScorePosition2, Color.Black);

                    spriteBatch.End();

                }

                if (gameState == gameOptions)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblgameoptions, new Vector2(148, 316), Color.White);
                    spriteBatch.End();

                    spriteBatch.Begin();
                    spriteBatch.Draw(Controller, ControllerTextPosition, Color.White);
                    spriteBatch.Draw(Help, HelpTextPosition, Color.White);
                    spriteBatch.Draw(Sound, SoundPosition, Color.White);
                    spriteBatch.End();

                    if (ControllerTextPosition.X > 12) ControllerTextPosition.X -= 18;
                    if (SoundPosition.X > 12) SoundPosition.X -= 15;
                    if (HelpTextPosition.X > 12) HelpTextPosition.X -= 12;
                }

                if (gameState == gameController)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblcontroller, new Vector2(96, 307), Color.White);
                    spriteBatch.End();

                    if (Defaultpad == apad)
                    {
                        spriteBatch.Begin();
                        spriteBatch.Draw(iControllerA, padPosition, Color.White);
                        spriteBatch.End();
                    }
                    else if (Defaultpad == spad)
                    {
                        spriteBatch.Begin();
                        spriteBatch.Draw(iControllerB, padPosition, Color.White);
                        spriteBatch.End();
                    }
                    if (padPosition.X > 12) padPosition.X -= 15;
                }

                if (gameState == gameSounds)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblsounds, new Vector2(146, 316), Color.White);
                    spriteBatch.End();

                    if (soundEnabled == true)
                    {
                        spriteBatch.Begin();
                        spriteBatch.Draw(btnSoundEnabled, bSoundPosition, Color.White);
                        spriteBatch.End();
                    }
                    else if (soundEnabled == false)
                    {
                        spriteBatch.Begin();
                        spriteBatch.Draw(btnSoundDisabled, bSoundPosition, Color.White);
                        spriteBatch.End();
                    }
                    if (bSoundPosition.X > 72) bSoundPosition.X -= 15;

                }

                if (gameState == gameCredits)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblcredits[0], new Vector2(146, 284), Color.White);
                    spriteBatch.Draw(lblcredits[1], new Vector2(194, 354), Color.White);
                    spriteBatch.Draw(lblcredits[2], new Vector2(181, 431), Color.White);
                    spriteBatch.Draw(lblcredits[3], new Vector2(181, 508), Color.White);
                    spriteBatch.Draw(lblcredits[4], new Vector2(181, 585), Color.White);
                    spriteBatch.Draw(lblcredits[5], new Vector2(70, 599), Color.White);
                    spriteBatch.Draw(lblcredits[6], new Vector2(42, 754), Color.White);
                    spriteBatch.End();
                }

                if (gameState == gameHelp)
                {
                    spriteBatch.Begin();
                    spriteBatch.Draw(start, new Rectangle(-3, 0, viewportX, viewportY), Color.White);
                    spriteBatch.Draw(lblhelp[0], new Vector2(68, 300), Color.White);
                    spriteBatch.Draw(lblhelp[1], new Vector2(40, 375), Color.White);
                    spriteBatch.Draw(lblhelp[2], new Vector2(96, 663), Color.White);
                    spriteBatch.End();
                }
                base.Draw(gameTime);
            }
            catch (Exception e)
            {
                Guide.BeginShowMessageBox("Opps Error", "Unhandle Exception Has occured on your device, game restarting", new List<string> { "Ok" },
                  0, MessageBoxIcon.Warning, new AsyncCallback(ErrorRestart), null);
            }
        }

        //void LeftRight(ref float playerCarX)
        //{
        //    TouchCollection touchLocations = TouchPanel.GetState();
        //    foreach (TouchLocation touchLocation in touchLocations)
        //    {
        //        if (touchLocation.State == TouchLocationState.Moved)
        //        {
        //            Vector2 touchPosition = touchLocation.Position;
        //            if (touchPosition.X >= leftPosition.X &&
        //             touchPosition.X < leftPosition.X + left.Bounds.Width &&
        //             touchPosition.Y >= leftPosition.Y &&
        //             touchPosition.Y < leftPosition.Y + left.Bounds.Height && (playerCarX - 14) > 0)
        //            {
        //                playerCarX -= 5;
        //            }

        //            if (touchPosition.X >= rightPosition.X &&
        //            touchPosition.X < rightPosition.X + right.Bounds.Width &&
        //            touchPosition.Y >= rightPosition.Y &&
        //            touchPosition.Y < rightPosition.Y + right.Bounds.Height &&
        //            (playerCarX + 52) < graphics.GraphicsDevice.Viewport.Width)
        //            {
        //                playerCarX += 5;
        //            }
        //        }
        //    }
        //}

        void steeringControl(ref float playerCarX)
        {
            float xstart = playerCarX;

            while (TouchPanel.IsGestureAvailable)
            {
                GestureSample gestureSample = TouchPanel.ReadGesture();
                if (gestureSample.GestureType == GestureType.FreeDrag)
                {

                    Vector2 touchPosition = gestureSample.Position; ;
                    if (touchPosition.X >= bCirclePosition.X &&
                     touchPosition.X < bCirclePosition.X + bigcircle.Bounds.Width - (smallcircle.Bounds.Width / 1.4) &&
                     touchPosition.Y >= bCirclePosition.Y &&
                     touchPosition.Y < bCirclePosition.Y + bigcircle.Bounds.Height - (smallcircle.Bounds.Height / 1.3))
                    {
                        sCirclePosition.X = touchPosition.X;
                        sCirclePosition.Y = touchPosition.Y;

                        //xstart = sCirclePosition.X + (sCirclePosition.X * 3 + (viewportX / bigcircle.Bounds.Width));
                        //if (xstart + 48 < graphics.GraphicsDevice.Viewport.Width && (xstart - 8) > 0) playerCarX = xstart;

                    }
                }

              if (gestureSample.GestureType == GestureType.DragComplete) sCirclePosition = new Vector2(41, 644);
            }

            if (sCirclePosition.X < 15) xstart -= 7.1f;
            else if (sCirclePosition.X >= 15 && sCirclePosition.X < 18) xstart -= 6.9f;//
            else if (sCirclePosition.X >= 18 && sCirclePosition.X < 21) xstart -= 5.8f;//
            else if (sCirclePosition.X >= 21 && sCirclePosition.X < 24) xstart -= 4.7f;//
            else if (sCirclePosition.X >= 24 && sCirclePosition.X < 27) xstart -= 3.6f;//
            else if (sCirclePosition.X >= 27 && sCirclePosition.X < 30) xstart -= 2.5f;//
            else if (sCirclePosition.X >= 30 && sCirclePosition.X < 33) xstart -= 1;//
           // else if (sCirclePosition.X >= 33 && sCirclePosition.X < 36) xstart -= 2.5f;
           // else if (sCirclePosition.X >= 36 && sCirclePosition.X < 37) xstart -= 1;
           // else if (sCirclePosition.X >= 45 && sCirclePosition.X < 46) xstart += 1;
           // else if (sCirclePosition.X >= 46 && sCirclePosition.X < 49) xstart += 2.5f;
            else if (sCirclePosition.X >= 49 && sCirclePosition.X < 51) xstart += 1;//3.6f
            else if (sCirclePosition.X >= 51 && sCirclePosition.X < 54) xstart += 2.5f;//4.7f
            else if (sCirclePosition.X >= 54 && sCirclePosition.X < 57) xstart += 3.6f;//6.8f
            else if (sCirclePosition.X >= 57 && sCirclePosition.X < 60) xstart += 4.7f;//7.9f
            else if (sCirclePosition.X >= 60 && sCirclePosition.X < 63) xstart += 5.8f;//8.1f
            else if (sCirclePosition.X >= 63 && sCirclePosition.X < 64) xstart += 6.9f;//9.2f
            else if (sCirclePosition.X >= 64) xstart += 7.1f;

            //if (sCirclePosition.X < 9) xstart -= 10.4f;
            //else if (sCirclePosition.X >= 9 && sCirclePosition.X < 12) xstart -= 9.2f;
            //else if (sCirclePosition.X >= 12 && sCirclePosition.X < 15) xstart -= 8.1f;
            //else if (sCirclePosition.X >= 15 && sCirclePosition.X < 18) xstart -= 7.9f;
            //else if (sCirclePosition.X >= 18 && sCirclePosition.X < 21) xstart -= 6.8f;
            //else if (sCirclePosition.X >= 21 && sCirclePosition.X < 24) xstart -= 4.7f;
            //else if (sCirclePosition.X >= 24 && sCirclePosition.X < 27) xstart -= 3.6f;
            //else if (sCirclePosition.X >= 27 && sCirclePosition.X < 30) xstart -= 2.5f;
            //else if (sCirclePosition.X >= 30 && sCirclePosition.X < 33) xstart -= 1;
            //else if (sCirclePosition.X >= 49 && sCirclePosition.X < 52) xstart += 1;
            //else if (sCirclePosition.X >= 52 && sCirclePosition.X < 55) xstart += 2.5f;
            //else if (sCirclePosition.X >= 55 && sCirclePosition.X < 58) xstart += 3.6f;
            //else if (sCirclePosition.X >= 58 && sCirclePosition.X < 61) xstart += 4.7f;
            //else if (sCirclePosition.X >= 61 && sCirclePosition.X < 64) xstart += 6.8f;
            //else if (sCirclePosition.X >= 64 && sCirclePosition.X < 67) xstart += 7.9f;
            //else if (sCirclePosition.X >= 67 && sCirclePosition.X < 70) xstart += 8.1f;
            //else if (sCirclePosition.X >= 70 && sCirclePosition.X < 73) xstart += 9.2f;
            //else if (sCirclePosition.X >= 73) xstart += 10.4f;

            if (xstart + 48 < graphics.GraphicsDevice.Viewport.Width && (xstart - 8) > 0) playerCarX = xstart;
           
        }

        void CollusionDetect(Car car, ref Car playerscar)
        {
            //if (Carhit == false)
            //{
               
                car8position.X = car.x;
                car8position.Y = screenY - car.y;
                if (car8position.Y < -220) return;
                Texture2D carTexture;
                if (car.type == Car.AudiA2)
                {
                    carTexture = GameCar[0];
                }
                else if (car.type == Car.AudiA6)
                {
                    carTexture = GameCar[1];
                }

                else if (car.type == Car.AudiR8)
                {
                    carTexture = GameCar[2];
                }

                else if (car.type == Car.AudiS5)
                {
                    carTexture = GameCar[3];
                }

                else if (car.type == Car.BmwX6)
                {
                    carTexture = GameCar[4];
                }
                else if (car.type == Car.Camaro)
                {
                    carTexture = GameCar[5];
                }
                else if (car.type == Car.Crossfire)
                {
                    carTexture = GameCar[6];
                }

                else if (car.type == Car.Crvblack)
                {
                    carTexture = GameCar[7];
                }

                else if (car.type == Car.Crvgreen)
                {
                    carTexture = GameCar[8];
                }

                else if (car.type == Car.accordblack)
                {
                    carTexture = GameCar[9];
                }
                else if (car.type == Car.Danfo)
                {
                    carTexture = GameCar[10];
                }
                else if (car.type == Car.Dogde)
                {
                    carTexture = GameCar[11];
                }

                else if (car.type == Car.F250)
                {
                    carTexture = GameCar[12];
                }

                else if (car.type == Car.accordblue)
                {
                    carTexture = GameCar[13];
                }
                else if (car.type == Car.Fj)
                {
                    carTexture = GameCar[14];
                }
                else if (car.type == Car.accordbrown)
                {
                    carTexture = GameCar[15];
                }
                else if (car.type == Car.accorddarkblue)
                {
                    carTexture = GameCar[16];
                }
                else if (car.type == Car.mazda6)
                {
                    carTexture = GameCar[17];
                }

                else if (car.type == Car.mazdaR8)
                {
                    carTexture = GameCar[18];
                }

                else if (car.type == Car.seat)
                {
                    carTexture = GameCar[19];
                }

                else if (car.type == Car.Tunderred)
                {
                    carTexture = GameCar[20];
                }
                else if (car.type == Car.accordgreen)
                {
                    carTexture = GameCar[21];
                }
                else if (car.type == Car.Molelue)
                {
                    carTexture = GameCar[22];
                }
                else if (car.type == Car.accordpink)
                {
                    carTexture = GameCar[23];
                }
                else if (car.type == Car.accordred)
                {
                    carTexture = GameCar[24];
                }
                else if (car.type == Car.accordwhite)
                {
                    carTexture = GameCar[25];
                }

                else if (car.type == Car.accordyellow)
                {
                    carTexture = GameCar[26];
                }
                else if (car.type == Car.corrolablue)
                {
                    carTexture = GameCar[27];
                }
                else if (car.type == Car.corrolalightblue)
                {
                    carTexture = GameCar[28];
                }
                else if (car.type == Car.corrolapink)
                {
                    carTexture = GameCar[29];
                }
                else if (car.type == Car.corrolayellow)
                {
                    carTexture = GameCar[30];
                }
                else if (car.type == Car.Truckbrown)
                {
                    carTexture = GameCar[31];
                }
                else if (car.type == Car.Truckred)
                {
                    carTexture = GameCar[32];
                }

                else if (car.type == Car.Truckblue)
                {
                    carTexture = GameCar[33];
                }
                else if (car.type == Car.Getlife)
                {
                    carTexture = GameCar[34];
                }

                else if (car.type == Car.BRTRed)
                {
                    carTexture = GameCar[35];
                }
                else if (car.type == Car.BRTBlue)
                {
                    carTexture = GameCar[36];
                }
                else
                {
                    carTexture = GameCar[5];
                }


                spriteBatch.Begin();
                spriteBatch.Draw(carTexture, car8position, Color.White);
                spriteBatch.End();
               
                

                //Get box bounding of players car passed
                BoundingBox player = new BoundingBox(new Vector3(playerscar.x - (GameCar[5].Bounds.Width / 2),
                playerscar.y - (GameCar[5].Bounds.Height / 2), 0),
                new Vector3(playerscar.x + (GameCar[5].Bounds.Width / 2), playerscar.y + (GameCar[5].Bounds.Height / 2), 0));


                //Get box bounding of enemy's car passed
                BoundingBox enemy = new BoundingBox(new Vector3(car.x - (carTexture.Bounds.Width / 2),
                car.y - (carTexture.Bounds.Height / 2), 0),
                new Vector3(car.x + (carTexture.Bounds.Width / 2), car.y + (carTexture.Bounds.Height / 2), 0));

                if (player.Intersects(enemy))
                {
                    if (car.type == Car.Getlife && life < 8) { life += 1; removelife = true; }
                    else if(Carhit == false)
                    {
                        Carhit = true;
                        //  if (soundEnabled == true) Crash.Play();
                        smokenFire = new List<SmokenFire>();
                        Pspeed = playerscar.speed;
                        playerscar.speed = car.speed - 70;
                        life -= 1;
                        SmokeCount = 0;
                    }
                }

                if (life == 0 )
                {
                    if (newhighscore == true)
                    {
                        HighScoreChecker();
                    }

                    if (nonewhighscore == true) { gameState = gameScores; }
                    if (nonewhighscore == false) { gameState = gameOver; }

                    LoadContent();

                    newhighscore = false;
                    loadData = false;
                    settings.loadData = false;
                }
            //}
        }

        void UpdateScore()
        {
            myscore += 1;
            if (playersCar.speed > 500 && playersCar.speed < 550)
            {
                if (myscore == 10)
                {
                    if (playersCar.x < 415) scorevalue += 15;
                    else scorevalue += 20;
                    myscore = 0;
                    score = scorevalue.ToString();
                }
            }
            else if (playersCar.speed > 550 && playersCar.speed < 600)
            {
                if (myscore == 10)
                {
                    if (playersCar.x < 415) scorevalue += 20;
                    else scorevalue += 25;
                    myscore = 0;
                    score = scorevalue.ToString();
                }
            }
            else if (playersCar.speed > 600 &&  playersCar.speed < 650)
            {
                if (myscore == 10)
                {
                    if (playersCar.x < 415) scorevalue += 25;
                    else scorevalue += 30;
                    myscore = 0;
                    score = scorevalue.ToString();
                }
            }
            else if (playersCar.speed > 650)
            {
                if (myscore == 10)
                {
                    if (playersCar.x < 415) scorevalue += 30;
                    else scorevalue += 35;
                    myscore = 0;
                    score = scorevalue.ToString();
                }
            }
            else
            {
                if (myscore == 10)
                {
                    if (playersCar.x < 415) scorevalue += 1;
                    else scorevalue += 10;
                    myscore = 0;
                    score = scorevalue.ToString();
                }
            }
        }

        void CarSpeed(ref float carSpeed)
        {
            if (sCirclePosition.Y < 620 && carSpeed < 700 && Carhit == false) carSpeed += 2;
            else if (sCirclePosition.Y > 670 && carSpeed > 450 && Carhit == false) carSpeed -= 3;

            float speed = 0.2f * carSpeed;
            string checkspeed = speed.ToString();
            if (checkspeed.Length > 5) checkspeed = checkspeed.Substring(0, 5);
            CSpeed = checkspeed + "KM";
        }

        void PrestartingDraw()
        {
            Hitspace += 1;

            if (Hitspace > 0 && Hitspace < 50)
            {
                spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
                spriteBatch.Draw(prestart, new Rectangle(1, 0, viewportX, viewportY), Color.White);
                spriteBatch.End();
            }

            if (Hitspace >= 50 && Hitspace < 140)
            {
                spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
                spriteBatch.Draw(prestart1, new Rectangle(1, 8, viewportX, viewportY), Color.White);
                spriteBatch.End();
            }

            //if (Hitspace >= 50 && Hitspace < 65)
            //{
            //    spriteBatch.Begin();
            //    spriteBatch.Draw(Enter, enterPosition, Color.White);
            //    spriteBatch.End();
            //}

            //if (Hitspace >= 70 && Hitspace < 95)
            //{
            //    spriteBatch.Begin();
            //    spriteBatch.Draw(Enter, enterPosition, Color.White);
            //    spriteBatch.End();
            //}
            //if (Hitspace == 100) Hitspace = 51;
            if (Hitspace > 140)
            {
                Hitspace = 0;
                gameState = gameStarting;
                LoadContent();
            }
           
        }

        void GameTouch()
        {

            TouchCollection touchLocations = TouchPanel.GetState();
            foreach (TouchLocation touchLocation in touchLocations)
            {
                if (touchLocation.State == TouchLocationState.Pressed)
                {
                    Vector2 touchPosition = touchLocation.Position;

                    //When game in start screen)
                    if (gameState == gameStarting)
                    {
                        //When start is pressed
                        if (touchPosition.X >= StartTextPosition.X &&
                         touchPosition.X < StartTextPosition.X + StartText.Bounds.Width &&
                         touchPosition.Y >= StartTextPosition.Y &&
                         touchPosition.Y < StartTextPosition.Y + StartText.Bounds.Height)
                        {
                            gameState = gameinplay;
                            life = 5;
                            scorevalue = 0;
                            score = "0";
                            CSpeed = "";
                            Hitspace = 0;
                            Carhit = false;
                            newhighscore = true;
                            nonewhighscore = false;
                            RestartGame();
                            LoadContent();
                        }

                        //when Highscore button is click on the screen
                        if (touchPosition.X >= HighScoreTextPosition.X &&
                        touchPosition.X < HighScoreTextPosition.X + HighScoreText.Bounds.Width &&
                        touchPosition.Y >= HighScoreTextPosition.Y &&
                        touchPosition.Y < HighScoreTextPosition.Y + HighScoreText.Bounds.Height)
                        {
                            if (loadData == true)
                            {
                                gameState = gameScores;
                                LoadContent();
                            }
                            else
                            {
                                gameState = gameCredits;
                                LoadContent();
                            }
                        }

                        //when Options button is click on the screen
                        if (touchPosition.X >= optionsTextPosition.X &&
                        touchPosition.X < optionsTextPosition.X + Options.Bounds.Width &&
                        touchPosition.Y >= optionsTextPosition.Y &&
                        touchPosition.Y < optionsTextPosition.Y + Options.Bounds.Height)
                        {
                            if (loadData == true)
                            {
                                gameState = gameOptions;
                                LoadContent();
                            }
                            else
                            {
                                gameState = gameScores;
                                LoadContent();
                            }
                        }

                        ////Code runs when load button is pressed on the screen
                        if (touchPosition.X >= loadGameTextPosition.X &&
                         touchPosition.X < loadGameTextPosition.X + loadGame.Bounds.Width &&
                         touchPosition.Y >= loadGameTextPosition.Y &&
                         touchPosition.Y < loadGameTextPosition.Y + loadGame.Bounds.Height)
                        {
                            //gameState = gameStarting;
                            if (loadData == true)
                            {
                                LoadGame();
                            }
                            else
                            {
                                gameState = gameOptions;
                                LoadContent();
                            }
                            
                            //gamepaused = false;
                       
                        }

                        //when Highscore button is click on the screen
                        if (touchPosition.X >= creditTextPosition.X &&
                        touchPosition.X < creditTextPosition.X + Credits.Bounds.Width &&
                        touchPosition.Y >= creditTextPosition.Y &&
                        touchPosition.Y < creditTextPosition.Y + Credits.Bounds.Height)
                        {
                            if (loadData == true)
                            {
                                gameState = gameCredits;
                                LoadContent();
                            }
                            else
                            {
                                gameState = gameScores;
                                LoadContent();
                            }
                        }
                    }


                    //When Game screen in GameOver
                    if (gameState == gameOver)
                    {
                        //When Retry is clicked
                        if (touchPosition.X >= retryPosition.X &&
                          touchPosition.X < retryPosition.X + retry.Bounds.Width &&
                          touchPosition.Y >= retryPosition.Y &&
                          touchPosition.Y < retryPosition.Y + retry.Bounds.Height)
                        {
                            gameState = gameinplay;
                            life = 5;
                            scorevalue = 0;
                            score = "0";
                            CSpeed = "";
                            Hitspace = 0;
                            Carhit = false;
                            newhighscore = true;
                            nonewhighscore = false;
                            RestartGame();
                            LoadContent();
                        }

                        //When Main menu Button is clicked 
                        if (touchPosition.X >= MainPosition.X &&
                      touchPosition.X < MainPosition.X + Main.Bounds.Width &&
                      touchPosition.Y >= MainPosition.Y &&
                      touchPosition.Y < MainPosition.Y + Main.Bounds.Height)
                        {
                            gameState = gameStarting;
                            scorevalue = 0;
                            score = "0";
                            CSpeed = "";
                            life = 5;
                            Carhit = false;
                            Hitspace = 0;
                            gamepaused = false;
                            RestartGame();
                            LoadContent();
                        }

                    }


                    //When Game is in Paused Screen
                    if (gameState == gameinplay && gamepaused == true)
                    {
                        //when paused code below on touch or resume is pressed
                        if (touchPosition.X >= resumeTextPosition.X &&
                       touchPosition.X < resumeTextPosition.X + resume.Bounds.Width &&
                       touchPosition.Y >= resumeTextPosition.Y &&
                       touchPosition.Y < resumeTextPosition.Y + resume.Bounds.Height)
                        {
                            gamepaused = false;
                        }

                        //When Retart Button is clicked 
                        if (touchPosition.X >= restartTextPosition.X &&
                      touchPosition.X < restartTextPosition.X + restart.Bounds.Width &&
                      touchPosition.Y >= restartTextPosition.Y &&
                      touchPosition.Y < restartTextPosition.Y + restart.Bounds.Height)
                        {
                            gameState = gameinplay;
                            scorevalue = 0;
                            score = "0";
                            CSpeed = "";
                            life = 5;
                            Carhit = false;
                            Hitspace = 0;
                            gamepaused = false;
                            newhighscore = true;
                            nonewhighscore = false;
                            RestartGame();
                            LoadContent();
                        }

                        //When Save n Exit Button is clicked 
                        if (touchPosition.X >= SaveExitTextPosition.X &&
                      touchPosition.X < SaveExitTextPosition.X + SaveExit.Bounds.Width &&
                      touchPosition.Y >= SaveExitTextPosition.Y &&
                      touchPosition.Y < SaveExitTextPosition.Y + SaveExit.Bounds.Height)
                        {
                            //scorevalue = 0;
                            //score = "0";
                            //CSpeed = "";
                            //life = 5;
                            //Carhit = false;
                            Hitspace = 0;
                            gamepaused = false;
                            newhighscore = true;
                            nonewhighscore = false;
                            SaveGame();
                            gameState = gameStarting;
                            LoadContent();
                            loadData = true;
                        }
                    }


                    //When Game is in Options screen
                    if (gameState == gameOptions)
                    {
                        //Controller button
                        if (touchPosition.X >= ControllerTextPosition.X &&
                     touchPosition.X < ControllerTextPosition.X + Controller.Bounds.Width &&
                     touchPosition.Y >= ControllerTextPosition.Y &&
                     touchPosition.Y < ControllerTextPosition.Y + Controller.Bounds.Height)
                        {
                            gameState = gameController;
                            LoadContent();
                        }

                        //Sounds button
                        if (touchPosition.X >= SoundPosition.X &&
                     touchPosition.X < SoundPosition.X + Sound.Bounds.Width &&
                     touchPosition.Y >= SoundPosition.Y &&
                     touchPosition.Y < SoundPosition.Y + Sound.Bounds.Height)
                        {
                            gameState = gameSounds;
                            LoadContent();
                        }

                        //Credits button
                        if (touchPosition.X >= HelpTextPosition.X &&
                     touchPosition.X < HelpTextPosition.X + Help.Bounds.Width &&
                     touchPosition.Y >= HelpTextPosition.Y &&
                     touchPosition.Y < HelpTextPosition.Y + Help.Bounds.Height)
                        {
                            gameState = gameHelp;
                            LoadContent();
                        }

                    }


                    //Game is in Controller State
                    if (gameState == gameController)
                    {

                        //Onscreen Controller pad
                        if (touchPosition.X >= padPosition.X &&
                    touchPosition.X < padPosition.X + iControllerA.Bounds.Width &&
                    touchPosition.Y >= padPosition.Y &&
                    touchPosition.Y < padPosition.Y + iControllerA.Bounds.Height)
                        {
                            if (Defaultpad == spad)
                            {
                                Defaultpad = apad;
                                    return;
                            }
                            if (Defaultpad == apad)
                            {
                                Defaultpad = spad;
                                return;
                            }
                        }
                    }

                    if (gameState == gameSounds)
                    {
                        if (touchPosition.X >= bSoundPosition.X &&
                    touchPosition.X < bSoundPosition.X + btnSoundEnabled.Bounds.Width &&
                    touchPosition.Y >= bSoundPosition.Y &&
                    touchPosition.Y < bSoundPosition.Y + btnSoundEnabled.Bounds.Height)
                        {
                            if (soundEnabled == true)
                            {
                                soundEnabled = false;
                                return;
                            }
                            if (soundEnabled == false)
                            {
                                soundEnabled = true;
                                return;
                            }
                        }
                    }
                   
                }
            }
        }

        void CarIntersected()
        {
            Hitspace += 1;
            if (Hitspace >= 10 && Hitspace < 20)
            {
                if (playersCar.speed <= 500) playersCar.speed += 1;
                drawCar(playersCar);
            }
            if (Hitspace >= 30 && Hitspace < 40)
            {
                if (playersCar.speed <= 500) playersCar.speed += 1;
                drawCar(playersCar);
            }
            if (Hitspace >= 50 && Hitspace < 70)
            {
                if (playersCar.speed <= 500) playersCar.speed += 2;
                drawCar(playersCar);
            }
            if (Hitspace >= 80 && Hitspace < 90)
            {
                if (playersCar.speed <= 500) playersCar.speed += 2;
                drawCar(playersCar);
            }
            if (Hitspace >= 80 && Hitspace < 90)
            {
                if (playersCar.speed <= 500) playersCar.speed += 3;
                drawCar(playersCar);
            }
            if (Hitspace >= 100 && Hitspace < 110)
            {
                if (playersCar.speed <= 500) playersCar.speed += 3;
                drawCar(playersCar);
            }
            if (Hitspace >= 120 && Hitspace < 130)
            {
                if (playersCar.speed <= 500) playersCar.speed += 3;
                drawCar(playersCar);
            }
            if (Hitspace >= 140 && Hitspace < 150)
            {
                if (playersCar.speed <= 500) playersCar.speed += 3;
                drawCar(playersCar);
            }
            if (Hitspace >= 160 && Hitspace < 170)
            {
                if (playersCar.speed <= 500) playersCar.speed += 3;
                drawCar(playersCar);
            }
            if (Hitspace == 180)
            {
                playersCar.speed = 500;
                Hitspace = 0;
                Carhit = false;
            }
        }

        void DrawLifes()
        {
            Texture2D Gamelife;
            if (life == 8) { Gamelife = lifes[7]; }
            else if (life == 7) { Gamelife = lifes[6]; }
            else if (life == 6) { Gamelife = lifes[5]; }
            else if (life == 5) { Gamelife = lifes[4]; }
            else if (life == 4) { Gamelife = lifes[3]; }
            else if (life == 3) { Gamelife = lifes[2]; }
            else if (life == 2) { Gamelife = lifes[1]; }
            else if (life == 1) { Gamelife = lifes[0]; }
            else { Gamelife = Gamelife = lifes[0]; }
           
            spriteBatch.Begin();
            spriteBatch.Draw(Gamelife, ilife, Color.White);
            spriteBatch.End();
        }

        //Checks for new highscore
        void HighScoreChecker()
        {
            int i, j; int k = 0;
            string name = "Player One";
            string name2;

            for (i = 0; i < settings.highscore.Length; i++)
            {
                j = int.Parse(settings.highscore[i]);
                name2 = settings.highscoreName[i];

                if (scorevalue > int.Parse(settings.highscore[i]))
                {
                    if (newhighscore == true)
                    {
                        GetName();
                        settings.highscoreName[i] = name;
                        settings.highscore[i] = Convert.ToString(scorevalue);
                        scorevalue = j;
                        name = name2;
                        newhighscore = false;
                        scoredetails = i;
                        nonewhighscore = true;
                    }


                    if (newhighscore == false && k == 1)
                    {
                        settings.highscoreName[i] = name;
                        settings.highscore[i] = Convert.ToString(scorevalue);
                        scorevalue = j;
                        name = name2;
                    }

                    k = 1;

                }
            }
        }

        //Method that invokes keyborad
        void GetName()
        {
            Guide.BeginShowKeyboardInput(
                          PlayerIndex.One,
                          "NEW HIGHSCORE!!!",
                          "Insert your name (maximum characters 12)",
                          "",
                          new AsyncCallback(OnEndShowKeyboardInput),
                          null);

        }

        //Method called after keyboard get input
        void OnEndShowKeyboardInput(IAsyncResult result)
        {
            newhighscorename = Guide.EndShowKeyboardInput(result);

            if (!string.IsNullOrEmpty(newhighscorename))
            {
                if (newhighscorename.Length > 12)
                {
                    newhighscorename = newhighscorename.Substring(0, 12);
                    settings.highscoreName[scoredetails] = newhighscorename;
                }
                else
                {
                    settings.highscoreName[scoredetails] = newhighscorename;
                }
            }
        }
    
        void OnAccelerometerReadingChanged(object sender, AccelerometerReadingEventArgs args)
        {
            lock (accelerometerVectorLock)
            {
                accelerometerVector = new Vector3((float)args.X, (float)args.Y,
                (float)args.Z);
            }
        }

        void myaccelerometer(ref float playerCarX)
        {

            Vector3 accVector; float YSprite, XSprite,ZSprite, xstart = playerCarX;

            lock (accelerometerVectorLock)
            {
                accVector = accelerometerVector;
            }
            //Move car left or right
            XSprite = (100 * accVector.X);
            if (XSprite < -27) xstart -= 10.4f;
            else if (XSprite >= -27 && XSprite < -24) xstart -= 9.2f;
            else if (XSprite >= -24 && XSprite < -21) xstart -= 8.1f;
            else if (XSprite >= -21 && XSprite < -18) xstart -= 7.9f;
            else if (XSprite >= -18 && XSprite < -15) xstart -= 6.8f;
            else if (XSprite >= -15 && XSprite < -12) xstart -= 4.7f;
            else if (XSprite >= -12 && XSprite < -9) xstart -= 3.6f;
            else if (XSprite >= -9 && XSprite < -6) xstart -= 2.5f;
            else if (XSprite >= -6 && XSprite < -3) xstart -= 1;
            else if (XSprite >= 3 && XSprite < 6 ) xstart += 1;
            else if (XSprite >= 6 && XSprite < 9) xstart += 2.5f;
            else if (XSprite >= 9 && XSprite < 12) xstart += 3.6f;
            else if (XSprite >= 12 && XSprite < 15) xstart += 4.7f;
            else if (XSprite >= 15 && XSprite < 18) xstart += 6.8f;
            else if (XSprite >= 18 && XSprite < 21) xstart += 7.9f;
            else if (XSprite >= 21 && XSprite < 24) xstart += 8.1f;
            else if (XSprite >= 24 && XSprite < 27) xstart += 9.2f;
            else if (XSprite >= 27) xstart += 10.4f;

            if (xstart + 48 < graphics.GraphicsDevice.Viewport.Width && (xstart - 8) > 0) playerCarX = xstart; 

            //Increase and reduce car speed
            YSprite = (100 * accVector.Y);
            ZSprite = (100 * accVector.Z);

            if (YSprite > 10 && playersCar.speed < 700 && Carhit == false) playersCar.speed += 2;
            else if (YSprite > -50 && YSprite < -5 && playersCar.speed > 450 && Carhit == false) playersCar.speed -= 3;
            else if (ZSprite < -15 && ZSprite > -50 && playersCar.speed < 700 && Carhit == false) playersCar.speed += 2;
            else if (ZSprite > 8 && playersCar.speed > 450 && Carhit == false) playersCar.speed -= 3;
        }

        void OnAppServiceLaunching(object sender, LaunchingEventArgs args) { settings = Settings.Load(); }

        void OnAppServiceActivated(object sender, ActivatedEventArgs args) { settings = Settings.Load(); }

        void OnAppServiceDeactivated(object sender, DeactivatedEventArgs args) { settings.Save(); }

        void OnAppServiceClosing(object sender, ClosingEventArgs args) { settings.Save(); }

        //When activating
        protected override void OnActivated(object sender, EventArgs args)
        {
            // numTaps = settings.Count;
            //if (PhoneApplicationService.Current.State.ContainsKey("numTaps"))
            //    numTaps = (int)PhoneApplicationService.Current.State["numTaps"];

            gameState = settings.gamestate;

            loadData = settings.loadData;

            Defaultpad = settings.Defaultpad;

            soundEnabled = settings.SoundEnabled;

            if (gameState == gameinplay)
            {
                LoadGame();
            }
            else
            {
                LoadContent();
            }

            base.OnActivated(sender, args);

            //use code below to load things you save when your game saved in the background
            //if (PhoneApplicationService.Current.State.ContainsKey("numTaps"))
            //    numTaps = (int)PhoneApplicationService.Current.State["numTaps"];
        }

        //When deativatiing
        protected override void OnDeactivated(object sender, EventArgs args)
        {

            settings.gamestate = gameState;

            if (gameState == gameinplay)
            {
                settings.Enemycar = " ";

                for (int i = 0; i < enemyCars.Length; ++i)
                {
                    for (int k = 0; k < enemyCars[i].Count; k++)
                    {
                        if (settings.Enemycar == " ")
                        { settings.Enemycar = "L" + i; }

                        if (settings.Enemycar != " " && settings.Enemycar.EndsWith("&") == true)
                        { settings.Enemycar += "L" + i; }

                        if(settings.Enemycar != string.Empty)
                        {
                            settings.Enemycar += "," + enemyCars[i][k].type.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].speed.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].x.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].y.ToString();
                        }
                    
                    }
                   if(i < enemyCars.Length - 1) settings.Enemycar += "&";
                }


                if (smokenFire.Count != 0)
                {
                    settings.Smoke = " ";

                    for (int j = 0; j < smokenFire.Count; j++)
                    {
                        if (settings.Smoke == " ")
                        {
                            settings.Smoke = smokenFire[j].type.ToString();
                            settings.Smoke += "," + smokenFire[j].speed.ToString();
                            settings.Smoke += "," + smokenFire[j].x.ToString();
                            settings.Smoke += "," + smokenFire[j].y.ToString();
                        }
                        else if (settings.Smoke.EndsWith("&") == true)
                        {
                            settings.Smoke += smokenFire[j].type.ToString();
                            settings.Smoke += "," + smokenFire[j].speed.ToString();
                            settings.Smoke += "," + smokenFire[j].x.ToString();
                            settings.Smoke += "," + smokenFire[j].y.ToString();
                        }
                        if (j < smokenFire.Count - 1) settings.Smoke += "&";

                    }
                }

               
                settings.playercarSpeed = playersCar.speed;
                settings.playercarType = playersCar.type;
                settings.playercarX = playersCar.x;
                settings.playercarY = playersCar.y;

                settings.screenY = screenY;

                settings.scoreValue = scorevalue;

                settings.lifes = life;

                settings.loadData = true;

                //settings.SmallCircleX = sCirclePosition.X;

                //settings.SmallCircleY = sCirclePosition.Y;

                settings.Paused = true;
            }

            settings.Defaultpad = Defaultpad;

            settings.SoundEnabled = soundEnabled;

            base.OnDeactivated(sender, args);

            //Use code below for things you want to save if your game doesnt but running on the background
            //PhoneApplicationService.Current.State["numTaps"] = settings.Count;
        }

        void LoadGame()
        {
            settings = Settings.Load();

                gameState = gameinplay;

                LoadContent();

                int type = settings.playercarType;
        
                float x = settings.playercarX;
                float y = settings.playercarY;
                float speed = settings.playercarSpeed;
                playersCar = new Car(type, speed, x, y);

                CarSpeed(ref speed);

                screenY = settings.screenY;

               // Defaultpad = settings.Defaultpad;

                scorevalue = settings.scoreValue;

                score = scorevalue.ToString();

                life = settings.lifes;

                //sCirclePosition.X = settings.SmallCircleX;

                //sCirclePosition.Y = settings.SmallCircleY;

                gamepaused = true;

                enemyCars = new List<Car>[7];
                for (int i = 0; i < enemyCars.Length; ++i) enemyCars[i] = new List<Car>();

                smokenFire = new List<SmokenFire>();


                if (settings.Enemycar != "")
                {
                    string[] lanes = settings.Enemycar.Split('&');
                    for (int i = 0; i < lanes.Length; ++i)
                    {
                        string[] Presentlane = lanes[i].Split(',');
                        string addlane = Presentlane[0];
                        addlane = addlane.Substring(1, 1);
                        // enemyCars = Presentlane
                        for (int k = 1; k < Presentlane.Length - 1; ++k)
                        {
                            int enemytype = int.Parse(Presentlane[k]);
                            float enemyspeed = float.Parse(Presentlane[k + 1]);
                            float enemyX = float.Parse(Presentlane[k + 2]);
                            float enemyY = float.Parse(Presentlane[k + 3]);

                            enemyCars[int.Parse(addlane)].Add(new Car(enemytype, enemyspeed, enemyX, enemyY));
                            if (k + 3 < Presentlane.Length) k += 3;
                           // if (Presentlane[k + 4] == "") k = Presentlane.Length;
                        }
                    }

                }

                if (settings.Smoke != "")
                {
                    string[] smoking = settings.Smoke.Split('&');
                    for (int j = 0; j < smoking.Length; ++j)
                    {
                      string[] smokeLane = smoking[j].Split(',');

                      int smoketype = int.Parse(smokeLane[0]);
                      float smokespeed = float.Parse(smokeLane[1]);
                      float smokex = float.Parse(smokeLane[2]);
                      float smokey = float.Parse(smokeLane[3]); 

                        smokenFire.Add(new SmokenFire(smoketype, smokespeed, smokex, smokey));
                    }

                }
        }

        void SaveGame()
        {
            if (gameState == gameinplay)
            {
                settings.Enemycar = " ";

                for (int i = 0; i < enemyCars.Length; ++i)
                {
                    for (int k = 0; k < enemyCars[i].Count; k++)
                    {
                        if (settings.Enemycar == " ")
                        { settings.Enemycar = "L" + i; }

                        if (settings.Enemycar != " " && settings.Enemycar.EndsWith("&") == true)
                        { settings.Enemycar += "L" + i; }

                        if(settings.Enemycar != string.Empty)
                        {
                            settings.Enemycar += "," + enemyCars[i][k].type.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].speed.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].x.ToString();
                            settings.Enemycar += "," + enemyCars[i][k].y.ToString();
                        }
                    
                    }
                   if(i < enemyCars.Length - 1) settings.Enemycar += "&";

                }

                if (smokenFire.Count != 0)
                {
                    settings.Smoke = " ";

                    for (int j = 0; j < smokenFire.Count; j++)
                    {
                        if (settings.Smoke == " ")
                        {
                            settings.Smoke = smokenFire[j].type.ToString();
                            settings.Smoke += "," + smokenFire[j].speed.ToString();
                            settings.Smoke += "," + smokenFire[j].x.ToString();
                            settings.Smoke += "," + smokenFire[j].y.ToString();
                        }
                        else if (settings.Smoke.EndsWith("&") == true)
                        {
                            settings.Smoke += smokenFire[j].type.ToString();
                            settings.Smoke += "," + smokenFire[j].speed.ToString();
                            settings.Smoke += "," + smokenFire[j].x.ToString();
                            settings.Smoke += "," + smokenFire[j].y.ToString();
                        }
                        if (j < smokenFire.Count - 1) settings.Smoke += "&";

                    }
                }


                settings.playercarSpeed = playersCar.speed;
                settings.playercarType = playersCar.type;
                settings.playercarX = playersCar.x;
                settings.playercarY = playersCar.y;

                settings.screenY = screenY;

                settings.scoreValue = scorevalue;

                settings.lifes = life;

                settings.Defaultpad = Defaultpad;

                settings.SoundEnabled = soundEnabled;

                settings.loadData = true;

                //settings.SmallCircleX = sCirclePosition.X;

                //settings.SmallCircleY = sCirclePosition.Y;

                settings.Save();
            }
        }
    }
}

