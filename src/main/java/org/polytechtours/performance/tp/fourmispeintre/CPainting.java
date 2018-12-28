package org.polytechtours.performance.tp.fourmispeintre;

import org.polytechtours.performance.tp.fourmispeintre.utils.ColorUtils;

import java.awt.*;

// version : 2.0

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>
 * Titre : Painting Ants
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : Copyright (c) 2003
 * </p>
 * <p>
 * Société : Equipe Réseaux/TIC - Laboratoire d'Informatique de l'Université de
 * Tours
 * </p>
 *
 * @author Nicolas Monmarché
 * @version 1.0
 */

public class CPainting extends Canvas implements MouseListener {
    private static final long serialVersionUID = 1L;
    // matrice servant pour le produit de convolution
    static private int[][] mMatriceConv9 = new int[3][3];
    static private int[][] mMatriceConv25 = new int[5][5];
    static private int[][] mMatriceConv49 = new int[7][7];
    // Objet ne servant que pour les bloc synchronized pour la manipulation du
    // tableau des couleurs
    private Object mMutexCouleurs = new Object();
    // tableau des couleurs, il permert de conserver en memoire l'état de chaque
    // pixel du canvas, ce qui est necessaire au deplacemet des fourmi
    // il sert aussi pour la fonction paint du Canvas
    //private CCouleur[][] mCouleurs;

    private int[][] mCouleursInt;


    // dimensions
    private Dimension mDimension;

    private PaintingAnts mApplis;

    private boolean mSuspendu = false;

    /******************************************************************************
     * Titre : public CPainting() Description : Constructeur de la classe
     ******************************************************************************/
    public CPainting(Dimension pDimension, PaintingAnts pApplis)
    {
        addMouseListener(this);

        mApplis = pApplis;

        mDimension = pDimension;

        mCouleursInt = new int[mDimension.width][mDimension.height];

        setBounds(new Rectangle(0, 0, mDimension.width, mDimension.height));

        this.setBackground(new Color(255, 255, 255));
    }

    /******************************************************************************
     * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
     * d'une case
     ******************************************************************************/
    public int getCouleur(int x, int y) {
        synchronized (mMutexCouleurs) {
            return mCouleursInt[x][y];
        }
    }

    /******************************************************************************
     * Titre : Color getDimension Description : Cette fonction renvoie la
     * dimension de la peinture
     ******************************************************************************/
    public Dimension getDimension() {
        return mDimension;
    }

    /******************************************************************************
     * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getHauteur() {
        return mDimension.height;
    }

    /******************************************************************************
     * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getLargeur() {
        return mDimension.width;
    }

    /******************************************************************************
     * Titre : void init() Description : Initialise le fond a la couleur blanche
     * et initialise le tableau des couleurs avec la couleur blanche
     ******************************************************************************/
    public void init()
    {
        int white = 16777215;

        synchronized (mMutexCouleurs)
        {
            // Initialisation de la matrice des couleurs

            synchronized (mMutexCouleurs)
            {
                for (int i = 0; i < mDimension.width; i++)
                {
                    for (int j = 0; j < mDimension.height; j++)
                    {
                        PaintingAnts.mBaseImage.setRGB(i, j, white);

                        mCouleursInt[i][j] = white;
                    }
                }
            }
        }

        // initialisation de la matrice de convolution : lissage moyen sur 9
        // cases
        /*
         * 1 2 1
         * 2 4 2
         * 1 2 1
         */
        CPainting.mMatriceConv9[0][0] = 1;
        CPainting.mMatriceConv9[0][1] = 2;
        CPainting.mMatriceConv9[0][2] = 1;
        CPainting.mMatriceConv9[1][0] = 2;
        CPainting.mMatriceConv9[1][1] = 4;
        CPainting.mMatriceConv9[1][2] = 2;
        CPainting.mMatriceConv9[2][0] = 1;
        CPainting.mMatriceConv9[2][1] = 2;
        CPainting.mMatriceConv9[2][2] = 1;

        // initialisation de la matrice de convolution : lissage moyen sur 25
        // cases
        /*
         * 1 1 2 1 1
         * 1 2 3 2 1
         * 2 3 4 3 2
         * 1 2 3 2 1
         * 1 1 2 1 1
         */
        CPainting.mMatriceConv25[0][0] = 1;
        CPainting.mMatriceConv25[0][1] = 1;
        CPainting.mMatriceConv25[0][2] = 2;
        CPainting.mMatriceConv25[0][3] = 1;
        CPainting.mMatriceConv25[0][4] = 1;
        CPainting.mMatriceConv25[1][0] = 1;
        CPainting.mMatriceConv25[1][1] = 2;
        CPainting.mMatriceConv25[1][2] = 3;
        CPainting.mMatriceConv25[1][3] = 2;
        CPainting.mMatriceConv25[1][4] = 1;
        CPainting.mMatriceConv25[2][0] = 2;
        CPainting.mMatriceConv25[2][1] = 3;
        CPainting.mMatriceConv25[2][2] = 4;
        CPainting.mMatriceConv25[2][3] = 3;
        CPainting.mMatriceConv25[2][4] = 2;
        CPainting.mMatriceConv25[3][0] = 1;
        CPainting.mMatriceConv25[3][1] = 2;
        CPainting.mMatriceConv25[3][2] = 3;
        CPainting.mMatriceConv25[3][3] = 2;
        CPainting.mMatriceConv25[3][4] = 1;
        CPainting.mMatriceConv25[4][0] = 1;
        CPainting.mMatriceConv25[4][1] = 1;
        CPainting.mMatriceConv25[4][2] = 2;
        CPainting.mMatriceConv25[4][3] = 1;
        CPainting.mMatriceConv25[4][4] = 1;

        // initialisation de la matrice de convolution : lissage moyen sur 49
        // cases
        /*
         * 1 1 2 2 2 1 1
         * 1 2 3 4 3 2 1
         * 2 3 4 5 4 3 2
         * 2 4 5 8 5 4 2
         * 2 3 4 5 4 3 2
         * 1 2 3 4 3 2 1
         * 1 1 2 2 2 1 1
         */
        CPainting.mMatriceConv49[0][0] = 1;
        CPainting.mMatriceConv49[0][1] = 1;
        CPainting.mMatriceConv49[0][2] = 2;
        CPainting.mMatriceConv49[0][3] = 2;
        CPainting.mMatriceConv49[0][4] = 2;
        CPainting.mMatriceConv49[0][5] = 1;
        CPainting.mMatriceConv49[0][6] = 1;

        CPainting.mMatriceConv49[1][0] = 1;
        CPainting.mMatriceConv49[1][1] = 2;
        CPainting.mMatriceConv49[1][2] = 3;
        CPainting.mMatriceConv49[1][3] = 4;
        CPainting.mMatriceConv49[1][4] = 3;
        CPainting.mMatriceConv49[1][5] = 2;
        CPainting.mMatriceConv49[1][6] = 1;

        CPainting.mMatriceConv49[2][0] = 2;
        CPainting.mMatriceConv49[2][1] = 3;
        CPainting.mMatriceConv49[2][2] = 4;
        CPainting.mMatriceConv49[2][3] = 5;
        CPainting.mMatriceConv49[2][4] = 4;
        CPainting.mMatriceConv49[2][5] = 3;
        CPainting.mMatriceConv49[2][6] = 2;

        CPainting.mMatriceConv49[3][0] = 2;
        CPainting.mMatriceConv49[3][1] = 4;
        CPainting.mMatriceConv49[3][2] = 5;
        CPainting.mMatriceConv49[3][3] = 8;
        CPainting.mMatriceConv49[3][4] = 5;
        CPainting.mMatriceConv49[3][5] = 4;
        CPainting.mMatriceConv49[3][6] = 2;

        CPainting.mMatriceConv49[4][0] = 2;
        CPainting.mMatriceConv49[4][1] = 3;
        CPainting.mMatriceConv49[4][2] = 4;
        CPainting.mMatriceConv49[4][3] = 5;
        CPainting.mMatriceConv49[4][4] = 4;
        CPainting.mMatriceConv49[4][5] = 3;
        CPainting.mMatriceConv49[4][6] = 2;

        CPainting.mMatriceConv49[5][0] = 1;
        CPainting.mMatriceConv49[5][1] = 2;
        CPainting.mMatriceConv49[5][2] = 3;
        CPainting.mMatriceConv49[5][3] = 4;
        CPainting.mMatriceConv49[5][4] = 3;
        CPainting.mMatriceConv49[5][5] = 2;
        CPainting.mMatriceConv49[5][6] = 1;

        CPainting.mMatriceConv49[6][0] = 1;
        CPainting.mMatriceConv49[6][1] = 1;
        CPainting.mMatriceConv49[6][2] = 2;
        CPainting.mMatriceConv49[6][3] = 2;
        CPainting.mMatriceConv49[6][4] = 2;
        CPainting.mMatriceConv49[6][5] = 1;
        CPainting.mMatriceConv49[6][6] = 1;

        mSuspendu = false;
    }

    /****************************************************************************/
    public void mouseClicked(MouseEvent pMouseEvent) {
        pMouseEvent.consume();
        if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
            // double clic sur le bouton gauche = effacer et recommencer
            if (pMouseEvent.getClickCount() == 2) {
                init();
            }
            // simple clic = suspendre les calculs et l'affichage
            mApplis.pause();
        } else {
            // bouton du milieu (roulette) = suspendre l'affichage mais
            // continuer les calculs
            if (pMouseEvent.getButton() == MouseEvent.BUTTON2) {
                suspendre();
            } else {
                // clic bouton droit = effacer et recommencer
                // case pMouseEvent.BUTTON3:
                init();
            }
        }
    }

    /****************************************************************************/
    public void mouseEntered(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mouseExited(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mousePressed(MouseEvent pMouseEvent) {

    }

    /****************************************************************************/
    public void mouseReleased(MouseEvent pMouseEvent) {
    }

    /******************************************************************************
     * Titre : void paint(Graphics g) Description : Surcharge de la fonction qui
     * est appelé lorsque le composant doit être redessiné
     ******************************************************************************/
    @Override
    public void paint(Graphics pGraphics) {
        int i, j;

        synchronized (mMutexCouleurs) {
            for (i = 0; i < mDimension.width; i++)
            {
                for (j = 0; j < mDimension.height; j++)
                {
                    PaintingAnts.mBaseImage.setRGB(i, j, mCouleursInt[i][j]);
                }
            }
        }

        pGraphics.drawImage(PaintingAnts.mBaseImage, 0, 0, null);
    }

    /******************************************************************************
     * Titre : void colorer_case(int x, int y, Color c) Description : Cette
     * fonction va colorer le pixel correspondant et mettre a jour le tabmleau des
     * couleurs
     ******************************************************************************/
    public void setCouleur(int x, int y, int c, int pTaille)
    {
        synchronized (mMutexCouleurs)
        {
            if (!mSuspendu)
            {
                // On colorie la case sur laquelle se trouve la fourmi.

                PaintingAnts.mBaseImage.setRGB(x, y, mCouleursInt[x][y]);
            }

            mCouleursInt[x][y] = c;

            // On fait diffuser la couleur :

            switch (pTaille)
            {
                case 1:
                    calculConvolution(x, y, CPainting.mMatriceConv9);
                    break;
                case 2:
                    calculConvolution(x, y, CPainting.mMatriceConv25);
                    break;
                case 3:
                    calculConvolution(x, y, CPainting.mMatriceConv49);
                    break;
            }
        }

        repaint();
    }


    private void calculConvolution(int x, int y, int[][] matrice)
    {
        int size = matrice.length;


        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                int m, n;
                int R = 0;
                int G = 0;
                int B = 0;

                for (int k = 0; k < size; k++)
                {
                    for (int l = 0; l < size; l++)
                    {
                        m = (x + i + k - 2 * (size / 2) + mDimension.width) % mDimension.width;
                        n = (y + j + l - 2 * (size / 2) + mDimension.height) % mDimension.height;

                        R += matrice[k][l] * ((mCouleursInt[m][n] >> 16) & 0xFF);
                        G += matrice[k][l] * ((mCouleursInt[m][n] >> 8) & 0xFF);
                        B += matrice[k][l] * (mCouleursInt[m][n] & 0xFF);
                    }
                }

                if (size == 3)
                {
                    R /= 16f;
                    G /= 16f;
                    B /= 16f;
                }

                else if (size == 5)
                {
                    R /= 44f;
                    G /= 44f;
                    B /= 44f;
                }

                else if (size == 7)
                {
                    R /= 128f;
                    G /= 128f;
                    B /= 128f;
                }

                m = (x + i - size / 2 + mDimension.width) % mDimension.width;
                n = (y + j - size / 2 + mDimension.height) % mDimension.height;

                mCouleursInt[m][n] = ColorUtils.getColor(R, G, B);

                if (!mSuspendu)
                {
                    PaintingAnts.mBaseImage.setRGB(m, n, mCouleursInt[m][n]);
                }
            }
        }
    }

    /******************************************************************************
     * Titre : setSupendu Description : Cette fonction change l'état de suspension
     ******************************************************************************/

    public void suspendre()
    {
        mSuspendu = !mSuspendu;
        if (!mSuspendu)
        {
            repaint();
        }
    }
}
