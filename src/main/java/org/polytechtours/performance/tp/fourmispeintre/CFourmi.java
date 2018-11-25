package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v3;
// version : 4.0

import org.polytechtours.performance.tp.fourmispeintre.utils.MathsUtils;

public class CFourmi
{
    // Coordonnées de la fourmi
    private int x, y;

    // Taille de la trace de phéromones déposée par la fourmi
    private int mTaille;

    // Couleur déposée par la fourmi.
    private CCouleur mCouleurDeposee;

    // Couleur suivie par la fourmi.
    private CCouleur mCouleurSuivie;

    // Deplacement de la fourmi.
    private CDeplacement mDeplacement;

    // Objet graphique sur lequel les fourmis peuvent peindre.
    private CPainting mPainting;

    // l'Applet.
    private PaintingAnts mApplis;


    public CFourmi(CPainting pPainting, PaintingAnts pApplis)
    {
        mCouleurDeposee = new CCouleur((int) (Math.random() * 256),
                                        (int) (Math.random() * 256),
                                        (int) (Math.random() * 256));

        //x = (float) Math.random();
        //y = (float) Math.random();

        mTaille = (int) (Math.random() * 4);

        // Création du déplacement.

        char typeDeplacement = 'o';
        int currentDirection = (int) (Math.random() * 8);
        float[] probas = new float[4];

        if (Math.random() < 0.5f)
            typeDeplacement = 'd';

        probas[Directions.TOWARDS] = (float) Math.random();
        probas[Directions.LEFT] = (float) (Math.random() * (1.0 - probas[Directions.TOWARDS]));
        probas[Directions.RIGHT] = (float) (1.0 - (probas[Directions.TOWARDS] + probas[Directions.LEFT]));
        probas[Directions.FOLLOW] = (float) (0.5 + 0.5 * Math.random());

        mDeplacement = new CDeplacement(typeDeplacement, currentDirection, probas);

        mPainting = pPainting;

        mApplis = pApplis;
    }


    public CFourmi(CCouleur pCouleurDeposee, CCouleur pCouleurSuivie, CDeplacement pDeplacement,
                   CPainting pPainting, float pInit_x, float pInit_y, int pTaille, PaintingAnts pApplis)
    {
        mCouleurDeposee = pCouleurDeposee;
        mCouleurSuivie = pCouleurSuivie;

        mDeplacement = pDeplacement;

        //x = pInit_x;
        //y = pInit_y;

        mTaille = pTaille;

        mPainting = pPainting;

        mApplis = pApplis;
    }


    public void setmCouleurSuivie(CCouleur couleur)
    {
        mCouleurSuivie = couleur;
    }

    public CCouleur getmCouleurDeposee()
    {
        return mCouleurDeposee;
    }

    /*************************************************************************************************
     * Titre : void deplacer() Description : Fonction de deplacement de la fourmi
     *
     */
    public synchronized void deplacer()
    {
        int i, j;
        CCouleur lCouleur;

        // le tableau dir contient 0 si la direction concernée ne contient pas la
        // couleur
        // à suivre, et 1 sinon (dir[0]=gauche, dir[1]=tt_droit, dir[2]=droite)
        i = MathsUtils.modulo(x + CDeplacement.mIncDirection[MathsUtils.modulo(mDeplacement.getCurrentDirection() - mDeplacement.getDecalDirection(), 8)][0], mPainting.getLargeur());
        j = MathsUtils.modulo(y + CDeplacement.mIncDirection[MathsUtils.modulo(mDeplacement.getCurrentDirection() - mDeplacement.getDecalDirection(), 8)][1], mPainting.getHauteur());
        if (mApplis.mBaseImage != null) {
            lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
        } else {
            lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
        }
        if (mCouleurDeposee.testCouleur(lCouleur)) {
            mDeplacement.goTo(Directions.LEFT);
        }

        i = MathsUtils.modulo(x + CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][0], mPainting.getLargeur());
        j = MathsUtils.modulo(y + CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][1], mPainting.getHauteur());
        if (mApplis.mBaseImage != null) {
            lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
        } else {
            lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
        }
        if (mCouleurDeposee.testCouleur(lCouleur)) {
            mDeplacement.goTo(Directions.TOWARDS);
        }

        i = MathsUtils.modulo(x + CDeplacement.mIncDirection[MathsUtils.modulo(mDeplacement.getCurrentDirection() + mDeplacement.getDecalDirection(), 8)][0], mPainting.getLargeur());
        j = MathsUtils.modulo(y + CDeplacement.mIncDirection[MathsUtils.modulo(mDeplacement.getCurrentDirection() + mDeplacement.getDecalDirection(), 8)][1], mPainting.getHauteur());
        if (mApplis.mBaseImage != null) {
            lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
        } else {
            lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
        }
        if (mCouleurDeposee.testCouleur(lCouleur)) {
            mDeplacement.goTo(Directions.RIGHT);
        }

        mDeplacement.doDeplacement();


        x += CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][0];
        y += CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][1];

        x = MathsUtils.modulo(x, mPainting.getLargeur());
        y = MathsUtils.modulo(y, mPainting.getHauteur());

        // coloration de la nouvelle position de la fourmi
        mPainting.setCouleur(x, y, mCouleurDeposee, mTaille);

        mApplis.IncrementFpsCounter();
    }

    /*************************************************************************************************
     */
    public int getX() {
        return x;
    }

    /*************************************************************************************************
     */
    public int getY() {
        return y;
    }
}
