package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v3;
// version : 4.0

public class CFourmi
{
  // objet graphique sur lequel les fourmis peuvent peindre
  private CPainting mPainting;
  // Coordonées de la fourmi
  private int x, y;
  // Taille de la trace de phéromones déposée par la fourmi
  private int mTaille;
  // l'applet
  private PaintingAnts mApplis;

  // Couleur déposée par la fourmi.
  private CCouleur mCouleurDeposee;
  // Deplacement de la fourmi.
  private CDeplacement mDeplacement;


  /*************************************************************************************************
  */
  public CFourmi(CCouleur pCouleurDeposee, CCouleur pCouleurSuivie, CDeplacement pDeplacement,
                 CPainting pPainting, float pInit_x, float pInit_y, int pTaille, PaintingAnts pApplis)
  {
    mCouleurDeposee = pCouleurDeposee;

    mDeplacement = pDeplacement;


    mPainting = pPainting;
    mApplis = pApplis;

    // taille du trait
    mTaille = pTaille;
  }

  /*************************************************************************************************
   * Titre : void deplacer() Description : Fonction de deplacement de la fourmi
   *
   */
  public synchronized void deplacer()
  {
    float tirage, prob1, prob2, prob3, total;
    int i, j;
    CCouleur lCouleur;

    // le tableau dir contient 0 si la direction concernée ne contient pas la
    // couleur
    // à suivre, et 1 sinon (dir[0]=gauche, dir[1]=tt_droit, dir[2]=droite)
    i = Utils.modulo(x + CDeplacement.mIncDirection[Utils.modulo(mDeplacement.getCurrentDirection() - mDeplacement.getDecalDirection(), 8)][0], mPainting.getLargeur());
    j = Utils.modulo(y + CDeplacement.mIncDirection[Utils.modulo(mDeplacement.getCurrentDirection() - mDeplacement.getDecalDirection(), 8)][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
    }
    if (mCouleurDeposee.testCouleur(lCouleur)) {
      mDeplacement.goTo(Directions.LEFT);
    }

    i = Utils.modulo(x + CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][0], mPainting.getLargeur());
    j = Utils.modulo(y + CDeplacement.mIncDirection[mDeplacement.getCurrentDirection()][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
    }
    if (mCouleurDeposee.testCouleur(lCouleur)) {
      mDeplacement.goTo(Directions.TOWARDS);
    }

    i = Utils.modulo(x + CDeplacement.mIncDirection[Utils.modulo(mDeplacement.getCurrentDirection() + mDeplacement.getDecalDirection(), 8)][0], mPainting.getLargeur());
    j = Utils.modulo(y + CDeplacement.mIncDirection[Utils.modulo(mDeplacement.getCurrentDirection() + mDeplacement.getDecalDirection(), 8)][1], mPainting.getHauteur());
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

    x = Utils.modulo(x, mPainting.getLargeur());
    y = Utils.modulo(y, mPainting.getHauteur());

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
