package org.polytechtours.performance.tp.fourmispeintre.utils;

import org.polytechtours.performance.tp.fourmispeintre.Directions;
import org.polytechtours.performance.tp.fourmispeintre.PaintingAnts;

import java.util.StringTokenizer;

public class HTMLReader
{
    // Lecture du seuil de luminance
    // <PARAM NAME="SeuilLuminance" VALUE="N">
    // N : seuil de luminance : -1 = random(2..60), x..y = random(x..y)
    public static float readLuminance(PaintingAnts applet)
    {
        String lChaine = applet.getParameter("SeuilLuminance");

        if (lChaine != null)
            return StringUtils.readFloatParameter(lChaine);

        return 40f;
    }

    // Lecture du nombre de fourmis :
    // <PARAM NAME="NbFourmis" VALUE="N">
    // N : nombre de fourmis : -1 = random(2..6), x..y = random(x..y)
    public static int readNombreFourmis(PaintingAnts applet)
    {
        String lChaine = applet.getParameter("NbFourmis");

        if (lChaine != null)
            return StringUtils.readIntParameter(lChaine);

        return (int) (Math.random() * 5) + 2;
    }

    public static int readCouleur(StringTokenizer lSTParam)
    {
        // lecture de la couleur déposée
        StringTokenizer lSTCouleur = new StringTokenizer(lSTParam.nextToken(), ",");

        int red = StringUtils.readIntParameter(lSTCouleur.nextToken());
        int green = StringUtils.readIntParameter(lSTCouleur.nextToken());
        int blue = StringUtils.readIntParameter(lSTCouleur.nextToken());

        if (red == -1)
            red = (int) (Math.random() * 256);

        if (green == -1)
            green = (int) (Math.random() * 256);

        if (blue == -1)
            blue = (int) (Math.random() * 256);

        return ColorUtils.getColor(red, green, blue);
    }

    public static float[] readCoordonnees(StringTokenizer chaine)
    {
        float[] coordonnees = new float[2];

        coordonnees[0] = StringUtils.readFloatParameter(chaine.nextToken());
        coordonnees[1] = StringUtils.readFloatParameter(chaine.nextToken());

        if (coordonnees[0] < 0.0 || coordonnees[0] > 1.0)
            coordonnees[0] = (float) Math.random();

        if (coordonnees[1] < 0.0 || coordonnees[1] > 1.0)
            coordonnees[1] = (float) Math.random();

        return coordonnees;
    }

    public static int readDirection(StringTokenizer chaine)
    {
        int direction = StringUtils.readIntParameter(chaine.nextToken());

        if (direction < 0 || direction > 7)
            return (int) (Math.random() * 8);

        return direction;
    }

    public static int readTaille(StringTokenizer chaine)
    {
        int taille = StringUtils.readIntParameter(chaine.nextToken());

        if (taille < 0 || taille > 3)
            return (int) (Math.random() * 4);

        return taille;
    }

    public static char readTypeDeplacement(StringTokenizer lSTProbas)
    {
        char typeDeplacement = lSTProbas.nextToken().charAt(0);

        if (typeDeplacement != 'o' && typeDeplacement != 'd')
        {
            if (Math.random() < 0.5)
                return 'o';
        }

        return 'd';
    }

    public static float[] readProbas(StringTokenizer lSTProbas)
    {
        float[] probas = new float[4];

        probas[Directions.TOWARDS] = StringUtils.readFloatParameter(lSTProbas.nextToken());
        probas[Directions.LEFT] = StringUtils.readFloatParameter(lSTProbas.nextToken());
        probas[Directions.RIGHT] = StringUtils.readFloatParameter(lSTProbas.nextToken());
        probas[Directions.FOLLOW] = StringUtils.readFloatParameter(lSTProbas.nextToken());

        // On normalise au cas où.

        float lSomme = probas[Directions.LEFT] + probas[Directions.TOWARDS] + probas[Directions.RIGHT];

        probas[Directions.LEFT] /= lSomme;
        probas[Directions.TOWARDS] /= lSomme;
        probas[Directions.RIGHT] /= lSomme;

        return probas;
    }
}
