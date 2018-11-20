package org.polytechtours.performance.tp.fourmispeintre;

public class CDeplacement
{
    private char typeDeplacement;

    // Numéro de la direction dans laquelle la fourmi regarde
    private int currentDirection;

    private int numberDeplacements;

    // Pas d'incrémentation des directions suivant le nombre de directions alloué à la fourmi.
    private int decalDirection = 1;

    // Probabilité d'aller a gauche, en face, à droite, de suivre la couleur.
    private float[] probas = new float[4];


    public CDeplacement(char typeDeplacement, int currentDirection, float[] probas)
    {
        this.typeDeplacement = typeDeplacement;
        this.currentDirection = currentDirection;

        if (this.typeDeplacement == 'd')
            decalDirection = 2;

        this.probas[0] = probas[0];
        this.probas[1] = probas[1];
        this.probas[2] = probas[2];
        this.probas[3] = probas[3];
    }

    public void doDeplacement()
    {
        numberDeplacements++;
    }

    public void checkLeft(int x, int y)
    {
        float value = Utils.modulo(currentDirection - decalDirection, 8);

        int i = Utils.modulo(x + CFourmi.mIncDirection[value][0], mPainting.getLargeur());
        int j = Utils.modulo(y + CFourmi.mIncDirection[value][1], mPainting.getHauteur());

        if (mApplis.mBaseImage != null)
        {
            lCouleur = new CCouleur(mApplis.mBaseImage.getRGB(i, j));
        }

        else
        {
            lCouleur = new CCouleur(mPainting.getCouleur(i, j).getRGB());
        }

        if (mCouleurDeposee.testCouleur(lCouleur))
        {
            dir[0] = 1;
        }
    }

    public void checkTowards()
    {

    }

    public void checkRight()
    {

    }
}
