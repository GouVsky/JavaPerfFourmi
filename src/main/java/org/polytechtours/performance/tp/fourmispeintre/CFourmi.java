package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v3;
// version : 4.0

import java.awt.Color;
import java.util.Random;

public class CFourmi {
  // Tableau des incrémentations à effectuer sur la position des fourmis
  // en fonction de la direction du deplacement
  static private int[][] mIncDirection = new int[8][2];
  // le generateur aléatoire (Random est thread safe donc on la partage)
  private static Random GenerateurAleatoire = new Random();
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

    // initialisation du tableau des directions
    CFourmi.mIncDirection[0][0] = 0;
    CFourmi.mIncDirection[0][1] = -1;
    CFourmi.mIncDirection[1][0] = 1;
    CFourmi.mIncDirection[1][1] = -1;
    CFourmi.mIncDirection[2][0] = 1;
    CFourmi.mIncDirection[2][1] = 0;
    CFourmi.mIncDirection[3][0] = 1;
    CFourmi.mIncDirection[3][1] = 1;
    CFourmi.mIncDirection[4][0] = 0;
    CFourmi.mIncDirection[4][1] = 1;
    CFourmi.mIncDirection[5][0] = -1;
    CFourmi.mIncDirection[5][1] = 1;
    CFourmi.mIncDirection[6][0] = -1;
    CFourmi.mIncDirection[6][1] = 0;
    CFourmi.mIncDirection[7][0] = -1;
    CFourmi.mIncDirection[7][1] = -1;
  }

  /*************************************************************************************************
   * Titre : void deplacer() Description : Fonction de deplacement de la fourmi
   *
   */
  public synchronized void deplacer()
  {
    float tirage, prob1, prob2, prob3, total;
    int[] dir = new int[3];
    int i, j;
    CCouleur lCouleur;

    mDeplacement.doDeplacement();

    dir[0] = 0;
    dir[1] = 0;
    dir[2] = 0;

    /*// le tableau dir contient 0 si la direction concernée ne contient pas la
    // couleur
    // à suivre, et 1 sinon (dir[0]=gauche, dir[1]=tt_droit, dir[2]=droite)
    i = Utils.modulo(x + CFourmi.mIncDirection[Utils.modulo(mDirection - mDecalDir, 8)][0], mPainting.getLargeur());
    j = Utils.modulo(y + CFourmi.mIncDirection[Utils.modulo(mDirection - mDecalDir, 8)][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
    }
    if (mCouleurDeposee.testCouleur(lCouleur)) {
      dir[0] = 1;
    }*/

    i = Utils.modulo(x + CFourmi.mIncDirection[mDirection][0], mPainting.getLargeur());
    j = Utils.modulo(y + CFourmi.mIncDirection[mDirection][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
    }
    if (mCouleurDeposee.testCouleur(lCouleur)) {
      dir[1] = 1;
    }
    i = Utils.modulo(x + CFourmi.mIncDirection[Utils.modulo(mDirection + mDecalDir, 8)][0], mPainting.getLargeur());
    j = Utils.modulo(y + CFourmi.mIncDirection[Utils.modulo(mDirection + mDecalDir, 8)][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
    }
    if (mCouleurDeposee.testCouleur(lCouleur)) {
      dir[2] = 1;
    }

    // tirage d'un nombre aléatoire permettant de savoir si la fourmi va suivre
    // ou non la couleur
    tirage = GenerateurAleatoire.nextFloat();// Math.random();

    // la fourmi suit la couleur
    if (((tirage <= mProba[3]) && ((dir[0] + dir[1] + dir[2]) > 0)) || ((dir[0] + dir[1] + dir[2]) == 3)) {
      prob1 = (dir[0]) * mProba[0];
      prob2 = (dir[1]) * mProba[1];
      prob3 = (dir[2]) * mProba[2];
    }
    // la fourmi ne suit pas la couleur
    else {
      prob1 = (1 - dir[0]) * mProba[0];
      prob2 = (1 - dir[1]) * mProba[1];
      prob3 = (1 - dir[2]) * mProba[2];
    }
    total = prob1 + prob2 + prob3;
    prob1 = prob1 / total;
    prob2 = prob2 / total + prob1;
    prob3 = prob3 / total + prob2;

    // incrémentation de la direction de la fourmi selon la direction choisie
    tirage = GenerateurAleatoire.nextFloat();// Math.random();
    if (tirage < prob1) {
      mDirection = Utils.modulo(mDirection - mDecalDir, 8);
    } else {
      if (tirage < prob2) {
        /* rien, on va tout droit */
      } else {
        mDirection = Utils.modulo(mDirection + mDecalDir, 8);
      }
    }

    x += CFourmi.mIncDirection[mDirection][0];
    y += CFourmi.mIncDirection[mDirection][1];

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
