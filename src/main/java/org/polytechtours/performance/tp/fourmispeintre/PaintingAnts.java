package org.polytechtours.performance.tp.fourmispeintre;

import org.polytechtours.performance.tp.fourmispeintre.utils.HTMLReader;

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
     */
    @Override
    public String getAppletInfo() {
        return "Painting Ants";
    }

    /****************************************************************************/
    /**
     * Obtenir l'information Applet
     */

    @Override
    public String[][] getParameterInfo() {
        String[][] lInfo = {{"SeuilLuminance", "string", "Seuil de luminance"}, {"Img", "string", "Image"},
                {"NbFourmis", "string", "Nombre de fourmis"}, {"Fourmis", "string",
                "Paramètres des fourmis (RGB_déposée)(RGB_suivie)(x,y,direction,taille)(TypeDeplacement,ProbaG,ProbaTD,ProbaD,ProbaSuivre);...;"}};
        return lInfo;
    }

    /****************************************************************************/
    /**
     * Obtenir l'état de pause
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
    private void readParameterFourmis()
    {
        // Luminance.
        CCouleur.seuilLuminance = HTMLReader.readLuminance(this);
        //System.out.println("Seuil de luminance:" + CCouleur.seuilLuminance);

        // Nombre de fourmis.
        int lNbFourmis = HTMLReader.readNombreFourmis(this);


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

        String lChaine = getParameter("Fourmis");

        if (lChaine != null)
        {
            char lTypeDeplacement;
            float lInit_x, lInit_y;
            int lInitDirection, lTaille;
            CCouleur lCouleurDeposee, lCouleurSuivie;

            //System.out.println("Paramètres:" + lChaine);

            // Chaine de paramètres pour une fourmi.
            StringTokenizer lSTFourmi = new StringTokenizer(lChaine, ";");

            while (lSTFourmi.hasMoreTokens())
            {
                StringTokenizer lSTParam = new StringTokenizer(lSTFourmi.nextToken(), "()");

                // Couleur déposée.
                lCouleurDeposee = HTMLReader.readCouleur(lSTParam);

                // Couleur suivie.
                lCouleurSuivie = HTMLReader.readCouleur(lSTParam);


                StringTokenizer lSTDéplacement = new StringTokenizer(lSTParam.nextToken(), ",");

                // Lecture des coordonnées.
                float[] coordonnees = HTMLReader.readCoordonnees(lSTDéplacement);

                lInit_x = coordonnees[0];
                lInit_y = coordonnees[1];

                // Lecture de la direction de départ.
                lInitDirection = HTMLReader.readDirection(lSTDéplacement);

                // Lecture de la taille.
                lTaille = HTMLReader.readTaille(lSTDéplacement);


                // Lecture des paramètres de déplacements.
                StringTokenizer lSTProbas = new StringTokenizer(lSTParam.nextToken(), ",");

                lTypeDeplacement = HTMLReader.readTypeDeplacement(lSTProbas);

                float[] probas = HTMLReader.readProbas(lSTProbas);

                CDeplacement lDeplacement = new CDeplacement(lTypeDeplacement, lInitDirection, probas);


                // Ajout de la fourmi.
                mColonie.addElement(new CFourmi(lCouleurDeposee, lCouleurSuivie, lDeplacement,
                                                mPainting, lInit_x, lInit_y, lTaille, this));
            }
        }

        // Initialisation aléatoire des fourmis.
        else
        {
            for (int i = 0; i < lNbFourmis; i++)
                mColonie.addElement(new CFourmi(mPainting, this));

            // La couleur suivie est la couleur d'une autre fourmi.
            for (int i = 0; i < lNbFourmis; i++)
            {
                int lColor = (int) (Math.random() * lNbFourmis);

                if (i == lColor)
                    mColonie.get(i).setmCouleurSuivie(mColonie.get((lColor + 1) % lNbFourmis).getmCouleurDeposee());
            }
        }

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
