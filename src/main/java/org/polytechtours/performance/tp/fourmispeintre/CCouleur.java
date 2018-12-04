package org.polytechtours.performance.tp.fourmispeintre;

import java.awt.*;

public class CCouleur extends Color
{
    public static float seuilLuminance;

    private float luminance;
    

    public CCouleur(int red, int green, int blue)
    {
        super(red, green, blue);

        luminance = 0.2426f * red + 0.7152f * green + 0.0722f * blue;
    }

    public CCouleur(int rgb)
    {
        super(rgb);

        luminance = 0.2426f * getRed() + 0.7152f * getGreen() + 0.0722f * getBlue();
    }

    /*************************************************************************************************
     * Titre : boolean testCouleur() Description : fonction testant l'égalité
     * d'une couleur avec la couleur suivie
     *
     */
    public boolean testCouleur(CCouleur pCouleur)
    {
        /* test */
        if (Math.abs(luminance - pCouleur.luminance) < seuilLuminance)
            return true;

        return false;
    }

    public float getLuminance()
    {
        return luminance;
    }
}
