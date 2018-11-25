package org.polytechtours.performance.tp.fourmispeintre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.StringTokenizer;
import java.util.Vector;

public class PaintingAnts extends java.applet.Applet implements Runnable {
  private static final long serialVersionUID = 1L;
  // parametres
  private int mLargeur;
  private int mHauteur;

  // l'objet graphique lui meme
  private CPainting mPainting;

  // les fourmis
  private Vector<CFourmi> mColonie = new Vector<>();
  private CColonie mColony;

  private Thread mApplis, mThreadColony;

  private Dimension mDimension;
  private boolean mPause = false;

  public BufferedImage mBaseImage;

  private StatisticsHandler statisticsHandler;

  /****************************************************************************/
  /**
   * Détruire l'applet
   *
   */
  @Override
  public void destroy() {
    // System.out.println(this.getName()+ ":destroy()");

    if (mApplis != null) {
      mApplis = null;
    }
  }

  /****************************************************************************/
  /**
   * Obtenir l'information Applet
   *
   */
  @Override
  public String getAppletInfo() {
    return "Painting Ants";
  }

  /****************************************************************************/
  /**
   * Obtenir l'information Applet
   *
   */

  @Override
  public String[][] getParameterInfo() {
    String[][] lInfo = { { "SeuilLuminance", "string", "Seuil de luminance" }, { "Img", "string", "Image" },
        { "NbFourmis", "string", "Nombre de fourmis" }, { "Fourmis", "string",
            "Paramètres des fourmis (RGB_déposée)(RGB_suivie)(x,y,direction,taille)(TypeDeplacement,ProbaG,ProbaTD,ProbaD,ProbaSuivre);...;" } };
    return lInfo;
  }

  /****************************************************************************/
  /**
   * Obtenir l'état de pause
   *
   */
  public boolean getPause() {
    return mPause;
  }

  public synchronized void IncrementFpsCounter() {
    statisticsHandler.incrementFpsCounter();
  }

  /****************************************************************************/
  /**
   * Initialisation de l'applet
   *
   */
  @Override
  public void init() {
    URL lFileName;
    URLClassLoader urlLoader = (URLClassLoader) this.getClass().getClassLoader();

    // lecture des parametres de l'applet

    mDimension = getSize();
    mLargeur = mDimension.width;
    mHauteur = mDimension.height;

    mPainting = new CPainting(mDimension, this);
    add(mPainting);

    // lecture de l'image
    lFileName = urlLoader.findResource("images/" + getParameter("Img"));
    try {
      if (lFileName != null) {
        mBaseImage = javax.imageio.ImageIO.read(lFileName);
      }
    } catch (java.io.IOException ex) {
    }

    if (mBaseImage != null) {
      mLargeur = mBaseImage.getWidth();
      mHauteur = mBaseImage.getHeight();
      mDimension.setSize(mLargeur, mHauteur);
      resize(mDimension);
    }

    readParameterFourmis();

    setLayout(null);

    statisticsHandler = new StatisticsHandler();
  }

  /****************************************************************************/
  /**
   * Paint the image and all active highlights.
   */
  @Override
  public void paint(Graphics g) {

    if (mBaseImage == null) {
      return;
    }
    g.drawImage(mBaseImage, 0, 0, this);
  }

  /****************************************************************************/
  /**
   * Mettre en pause
   *
   */
  public void pause() {
    mPause = !mPause;
    // if (!mPause)
    // {
    // notify();
    // }
  }

  // =========================================================================
  // lecture des paramètres de l'applet
  private void readParameterFourmis() {
    String lChaine;
    int R, G, B;
    CCouleur lCouleurDeposee, lCouleurSuivie;
    CFourmi lFourmi;
    float lProbaTD, lProbaG, lProbaD, lProbaSuivre;
    char lTypeDeplacement = ' ';
    int lInitDirection, lTaille;
    float lInit_x, lInit_y;
    int lNbFourmis = -1;

    // Lecture des paramètres des fourmis

    // Lecture du seuil de luminance
    // <PARAM NAME="SeuilLuminance" VALUE="N">
    // N : seuil de luminance : -1 = random(2..60), x..y = random(x..y)
    lChaine = getParameter("SeuilLuminance");
    if (lChaine != null) {
      CCouleur.seuilLuminance = Utils.readFloatParameter(lChaine);
    } else {
      // si seuil de luminance n'est pas défini
      CCouleur.seuilLuminance = 40f;
    }
    System.out.println("Seuil de luminance:" + CCouleur.seuilLuminance);

    // Lecture du nombre de fourmis :
    // <PARAM NAME="NbFourmis" VALUE="N">
    // N : nombre de fourmis : -1 = random(2..6), x..y = random(x..y)
    lChaine = getParameter("NbFourmis");
    if (lChaine != null) {
      lNbFourmis = Utils.readIntParameter(lChaine);
    } else {
      // si le parametre NbFourmis n'est pas défini
      lNbFourmis = -1;
    }
    // si le parametre NbFourmis n'est pas défini ou alors s'il vaut -1 :
    if (lNbFourmis == -1) {
      // Le nombre de fourmis est aléatoire entre 2 et 6 !
      lNbFourmis = (int) (Math.random() * 5) + 2;
    }

    // <PARAM NAME="Fourmis"
    // VALUE="(255,0,0)(255,255,255)(20,40,1)([d|o],0.2,0.6,0.2,0.8)">
    // (R,G,B) de la couleur déposée : -1 = random(0...255); x:y = random(x...y)
    // (R,G,B) de la couleur suivie : -1 = random(0...255); x:y = random(x...y)
    // (x,y,d,t) position , direction initiale et taille du trait
    // x,y = 0.0 ... 1.0 : -1 = random(0.0 ... 1.0); x:y = random(x...y)
    // d = 7 0 1
    // 6 X 2
    // 5 4 3 : -1 = random(0...7); x:y = random(x...y)
    // t = 0, 1, 2, 3 : -1 = random(0...3); x:y = random(x...y)
    //
    // (type deplacement,proba gauche,proba tout droit,proba droite,proba
    // suivre)
    // type deplacement = o/d : -1 = random(o/d)
    // probas : -1 = random(0.0 ... 1.0); x:y = random(x...y)

    lChaine = getParameter("Fourmis");
    if (lChaine != null) {
      // on affiche la chaine de parametres
      System.out.println("Paramètres:" + lChaine);

      // on va compter le nombre de fourmis dans la chaine de parametres :
      lNbFourmis = 0;
      // chaine de paramètres pour une fourmi
      StringTokenizer lSTFourmi = new StringTokenizer(lChaine, ";");
      while (lSTFourmi.hasMoreTokens()) {
        // chaine de parametres de couleur et proba
        StringTokenizer lSTParam = new StringTokenizer(lSTFourmi.nextToken(), "()");
        // lecture de la couleur déposée
        StringTokenizer lSTCouleurDéposée = new StringTokenizer(lSTParam.nextToken(), ",");
        R = Utils.readIntParameter(lSTCouleurDéposée.nextToken());
        if (R == -1) {
          R = (int) (Math.random() * 256);
        }

        G = Utils.readIntParameter(lSTCouleurDéposée.nextToken());
        if (G == -1) {
          G = (int) (Math.random() * 256);
        }
        B = Utils.readIntParameter(lSTCouleurDéposée.nextToken());
        if (B == -1) {
          B = (int) (Math.random() * 256);
        }
        lCouleurDeposee = new CCouleur(R, G, B);
        System.out.print("Parametres de la fourmi " + lNbFourmis + ":(" + R + "," + G + "," + B + ")");

        // lecture de la couleur suivie
        StringTokenizer lSTCouleurSuivi = new StringTokenizer(lSTParam.nextToken(), ",");
        R = Utils.readIntParameter(lSTCouleurSuivi.nextToken());
        G = Utils.readIntParameter(lSTCouleurSuivi.nextToken());
        B = Utils.readIntParameter(lSTCouleurSuivi.nextToken());
        lCouleurSuivie = new CCouleur(R, G, B);
        System.out.print("(" + R + "," + G + "," + B + ")");

        // lecture de la position de la direction de départ et de la taille de
        // la trace
        StringTokenizer lSTDéplacement = new StringTokenizer(lSTParam.nextToken(), ",");
        lInit_x = Utils.readFloatParameter(lSTDéplacement.nextToken());
        if (lInit_x < 0.0 || lInit_x > 1.0) {
          lInit_x = (float) Math.random();
        }
        lInit_y = Utils.readFloatParameter(lSTDéplacement.nextToken());
        if (lInit_y < 0.0 || lInit_y > 1.0) {
          lInit_y = (float) Math.random();
        }
        lInitDirection = Utils.readIntParameter(lSTDéplacement.nextToken());
        if (lInitDirection < 0 || lInitDirection > 7) {
          lInitDirection = (int) (Math.random() * 8);
        }
        lTaille = Utils.readIntParameter(lSTDéplacement.nextToken());
        if (lTaille < 0 || lTaille > 3) {
          lTaille = (int) (Math.random() * 4);
        }
        System.out.print("(" + lInit_x + "," + lInit_y + "," + lInitDirection + "," + lTaille + ")");

        // lecture des probas
        StringTokenizer lSTProbas = new StringTokenizer(lSTParam.nextToken(), ",");
        lTypeDeplacement = lSTProbas.nextToken().charAt(0);
        // System.out.println(" lTypeDeplacement:"+lTypeDeplacement);

        if (lTypeDeplacement != 'o' && lTypeDeplacement != 'd') {
          if (Math.random() < 0.5) {
            lTypeDeplacement = 'o';
          } else {
            lTypeDeplacement = 'd';
          }
        }

        lProbaG = Utils.readFloatParameter(lSTProbas.nextToken());
        lProbaTD = Utils.readFloatParameter(lSTProbas.nextToken());
        lProbaD = Utils.readFloatParameter(lSTProbas.nextToken());
        lProbaSuivre = Utils.readFloatParameter(lSTProbas.nextToken());
        // on normalise au cas ou
        float lSomme = lProbaG + lProbaTD + lProbaD;
        lProbaG /= lSomme;
        lProbaTD /= lSomme;
        lProbaD /= lSomme;

        System.out.println(
            "(" + lTypeDeplacement + "," + lProbaG + "," + lProbaTD + "," + lProbaD + "," + lProbaSuivre + ");");

        // Création du déplacement de la fourmi.

        float[] deplacements = new float[4];
        deplacements[0] = lProbaG;
        deplacements[1] = lProbaTD;
        deplacements[2] = lProbaD;
        deplacements[3] = lProbaSuivre;

        CDeplacement lDeplacement = new CDeplacement(lTypeDeplacement, lInitDirection, deplacements);

        // création de la fourmi
        lFourmi = new CFourmi(lCouleurDeposee, lCouleurSuivie, lDeplacement, mPainting,
            lInit_x, lInit_y, lTaille, this);
        mColonie.addElement(lFourmi);
        lNbFourmis++;
      }
    } else // initialisation aléatoire des fourmis
    {

      int i;
      CCouleur lTabColor[] = new CCouleur[lNbFourmis];
      int lColor;

      // initialisation aléatoire de la couleur de chaque fourmi
      for (i = 0; i < lNbFourmis; i++) {
        R = (int) (Math.random() * 256);
        G = (int) (Math.random() * 256);
        B = (int) (Math.random() * 256);
        lTabColor[i] = new CCouleur(R, G, B);
      }

      // construction des fourmis
      for (i = 0; i < lNbFourmis; i++) {
        // la couleur suivie est la couleur d'une autre fourmi
        lColor = (int) (Math.random() * lNbFourmis);
        if (i == lColor) {
          lColor = (lColor + 1) % lNbFourmis;
        }

        // une chance sur deux d'avoir un déplacement perpendiculaire
        if ((float) Math.random() < 0.5f) {
          lTypeDeplacement = 'd';
        } else {
          lTypeDeplacement = 'o';
        }

        // position initiale
        lInit_x = (float) (Math.random()); // *mPainting.getLargeur()
        lInit_y = (float) (Math.random()); // *mPainting.getHauteur()

        // direction initiale
        lInitDirection = (int) (Math.random() * 8);

        // taille du trait
        lTaille = (int) (Math.random() * 4);

        // proba de déplacement :
        lProbaTD = (float) (Math.random());
        lProbaG = (float) (Math.random() * (1.0 - lProbaTD));
        lProbaD = (float) (1.0 - (lProbaTD + lProbaG));
        lProbaSuivre = (float) (0.5 + 0.5 * Math.random());

        System.out.print(
            "Random:(" + lTabColor[i].getRed() + "," + lTabColor[i].getGreen() + "," + lTabColor[i].getBlue() + ")");
        System.out.print("(" + lTabColor[lColor].getRed() + "," + lTabColor[lColor].getGreen() + ","
            + lTabColor[lColor].getBlue() + ")");
        System.out.print("(" + lInit_x + "," + lInit_y + "," + lInitDirection + "," + lTaille + ")");
        System.out.println(
            "(" + lTypeDeplacement + "," + lProbaG + "," + lProbaTD + "," + lProbaD + "," + lProbaSuivre + ");");


        // Création du déplacement de la fourmi.

        float[] deplacements = new float[4];
        deplacements[0] = lProbaG;
        deplacements[1] = lProbaTD;
        deplacements[2] = lProbaD;
        deplacements[3] = lProbaSuivre;

        CDeplacement lDeplacement = new CDeplacement(lTypeDeplacement, lInitDirection, deplacements);

        // création et ajout de la fourmi dans la colonie
        lFourmi = new CFourmi(lTabColor[i], lTabColor[lColor], lDeplacement, mPainting,
                lInit_x, lInit_y, lTaille, this);
        mColonie.addElement(lFourmi);
      }
    }
    // on affiche le nombre de fourmis
    // System.out.println("Nombre de Fourmis:"+lNbFourmis);
  }

  @Override
  public void run() {
    int i;
    String lMessage;

    mPainting.init();

    Thread currentThread = Thread.currentThread();

    /*
     * for ( i=0 ; i<mColonie.size() ; i++ ) {
     * ((CFourmi)mColonie.elementAt(i)).start(); }
     */

    mThreadColony.start();

    while (mApplis == currentThread) {
      if (mPause) {
        lMessage = "pause";
      } else {
        lMessage = "running (" + statisticsHandler.getLastFPS() + ") ";
      }
      showStatus(lMessage);

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        showStatus(e.toString());
      }
    }
  }

  /****************************************************************************/
  /**
   * Lancer l'applet
   *
   */
  @Override
  public void start() {
    mColony = new CColonie(mColonie, this);
    mThreadColony = new Thread(mColony);
    mThreadColony.setPriority(Thread.MIN_PRIORITY);

    showStatus("starting...");
    statisticsHandler.start();

    // Create the thread.
    mApplis = new Thread(this);
    // and let it start running
    mApplis.setPriority(Thread.MIN_PRIORITY);
    mApplis.start();
  }

  /****************************************************************************/
  /**
   * Arrêter l'applet
   *
   */
  @Override
  public void stop() {
    showStatus("stopped...");

    // On demande au Thread Colony de s'arreter et on attend qu'il s'arrete
    mColony.pleaseStop();
    try {
      mThreadColony.join();
    } catch (Exception e) {
    }

    statisticsHandler.stop();

    mThreadColony = null;
    mApplis = null;
  }
}
