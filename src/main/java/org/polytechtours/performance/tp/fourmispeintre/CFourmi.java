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
        CCouleur lCouleur;

        int currentDirection = mDeplacement.getCurrentDirection();
        int decalDirection = mDeplacement.getDecalDirection();

        int[] deplacementsLeft = CDeplacement.mIncDirection[MathsUtils.modulo(currentDirection - decalDirection, 8)];
        int[] deplacementsToward = CDeplacement.mIncDirection[currentDirection];
        int[] deplacementsRight = CDeplacement.mIncDirection[MathsUtils.modulo(currentDirection + decalDirection, 8)];


        lCouleur = choixCouleur(deplacementsLeft);

        if (mCouleurDeposee.testCouleur(lCouleur))
            mDeplacement.goTo(Directions.LEFT);


        lCouleur = choixCouleur(deplacementsToward);

        if (mCouleurDeposee.testCouleur(lCouleur))
            mDeplacement.goTo(Directions.TOWARDS);


        lCouleur = choixCouleur(deplacementsRight);

        if (mCouleurDeposee.testCouleur(lCouleur))
            mDeplacement.goTo(Directions.RIGHT);


        mDeplacement.doDeplacement();


        x += CDeplacement.mIncDirection[currentDirection][0];
        y += CDeplacement.mIncDirection[currentDirection][1];

        x = MathsUtils.modulo(x, mPainting.getLargeur());
        y = MathsUtils.modulo(y, mPainting.getHauteur());

        // Coloration de la nouvelle position de la fourmi.
        mPainting.setCouleur(x, y, mCouleurDeposee, mTaille);

        mApplis.IncrementFpsCounter();
    }

    private CCouleur choixCouleur(int[] deplacements)
    {
        int i = MathsUtils.modulo(x + deplacements[0], mPainting.getLargeur());
        int j = MathsUtils.modulo(y + deplacements[1], mPainting.getHauteur());

        if (mApplis.mBaseImage != null)
            return new CCouleur(mApplis.mBaseImage.getRGB(i, j));

        return new CCouleur(mPainting.getCouleur(i, j));
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
