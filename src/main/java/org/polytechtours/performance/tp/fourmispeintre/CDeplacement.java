package org.polytechtours.performance.tp.fourmispeintre;

import java.util.Random;

public class CDeplacement
{
    private char typeDeplacement;

    // Numéro de la direction dans laquelle la fourmi regarde
    private int currentDirection;

    // Pas d'incrémentation des directions suivant le nombre de directions alloué à la fourmi.
    private int decalDirection = 1;

    // le tableau contient 0 si la direction concernée ne contient pas la couleur à suivre,
    // et 1 sinon (dir[0] = gauche, dir[1] = tt_droit, dir[2] = droite).
    int[] dir = new int[3];

    // Probabilité d'aller a gauche, en face, à droite, de suivre la couleur.
    private float[] theoricalProbas = new float[4];

    // Probabilité d'aller a gauche, en face, à droite, de suivre la couleur.
    private float[] realProbas = new float[4];

    // Tableau des incrémentations à effectuer sur la position des fourmis
    // en fonction de la direction du deplacement
    static public int[][] mIncDirection = new int[8][2];


    public CDeplacement(char typeDeplacement, int currentDirection, float[] probas)
    {
        this.typeDeplacement = typeDeplacement;
        this.currentDirection = currentDirection;

        if (this.typeDeplacement == 'd')
            decalDirection = 2;

        this.theoricalProbas[0] = probas[0];
        this.theoricalProbas[1] = probas[1];
        this.theoricalProbas[2] = probas[2];
        this.theoricalProbas[3] = probas[3];

        // initialisation du tableau des directions
        mIncDirection[0][0] = 0;
        mIncDirection[0][1] = -1;
        mIncDirection[1][0] = 1;
        mIncDirection[1][1] = -1;
        mIncDirection[2][0] = 1;
        mIncDirection[2][1] = 0;
        mIncDirection[3][0] = 1;
        mIncDirection[3][1] = 1;
        mIncDirection[4][0] = 0;
        mIncDirection[4][1] = 1;
        mIncDirection[5][0] = -1;
        mIncDirection[5][1] = 1;
        mIncDirection[6][0] = -1;
        mIncDirection[6][1] = 0;
        mIncDirection[7][0] = -1;
        mIncDirection[7][1] = -1;
    }

    public void goTo(int direction)
    {
        dir[direction] = 1;
    }

    public void doDeplacement()
    {
        calculateDirections();

        float tirage = new Random().nextFloat();

        if (tirage < realProbas[0])
            currentDirection = Utils.modulo(currentDirection - decalDirection, 8);

        else if (tirage >= realProbas[1])
            currentDirection = Utils.modulo(currentDirection + decalDirection, 8);
    }

    private void calculateDirections()
    {
        float tirage = new Random().nextFloat();

        // La fourmi suit la couleur.
        if (tirage <= theoricalProbas[3] && dir[Directions.LEFT] + dir[Directions.TOWARDS] + dir[Directions.RIGHT] > 0)
        {
            realProbas[0] = dir[Directions.LEFT] * theoricalProbas[Directions.LEFT];
            realProbas[1] = dir[Directions.TOWARDS] * theoricalProbas[Directions.TOWARDS];
            realProbas[2] = dir[Directions.RIGHT] * theoricalProbas[Directions.RIGHT];
        }

        // la fourmi ne suit pas la couleur.
        else
        {
            realProbas[0] = (1 - dir[Directions.LEFT]) * theoricalProbas[Directions.LEFT];
            realProbas[1] = (1 - dir[Directions.TOWARDS]) * theoricalProbas[Directions.TOWARDS];
            realProbas[2] = (1 - dir[Directions.RIGHT]) * theoricalProbas[Directions.LEFT];
        }

        float total = realProbas[0] + realProbas[1] + realProbas[2];

        realProbas[0] = realProbas[0] / total;
        realProbas[1] = realProbas[1] / total + realProbas[0];
    }

    public int getCurrentDirection()
    {
        return currentDirection;
    }

    public int getDecalDirection()
    {
        return decalDirection;
    }
}
