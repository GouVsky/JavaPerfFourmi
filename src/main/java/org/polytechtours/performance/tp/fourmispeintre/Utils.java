package org.polytechtours.performance.tp.fourmispeintre;

import java.util.StringTokenizer;

public class Utils
{
    // =========================================================================
    // cette fonction analyse une chaine :
    // si pStr est un nombre : sa valeur est retournée
    // si pStr est un interval x..y : une valeur au hasard dans [x,y] est
    // retournée
    public static float readFloatParameter(String pStr)
    {
        float lMin, lMax, lResult;
        // System.out.println(" chaine pStr: "+pStr);
        StringTokenizer lStrTok = new StringTokenizer(pStr, ":");
        // on lit une premiere valeur
        lMin = Float.valueOf(lStrTok.nextToken()).floatValue();
        // System.out.println(" lMin: "+lMin);
        lResult = lMin;
        // on essaye d'en lire une deuxieme
        try {
            lMax = Float.valueOf(lStrTok.nextToken()).floatValue();
            // System.out.println(" lMax: "+lMax);
            if (lMax > lMin) {
                // on choisit un nombre entre lMin et lMax
                lResult = (float) (Math.random() * (lMax - lMin)) + lMin;
            }
        } catch (java.util.NoSuchElementException e) {
            // il n'y pas de deuxieme nombre et donc le nombre retourné correspond au
            // premier nombre
        }
        return lResult;
    }

    // =========================================================================
    // cette fonction analyse une chaine :
    // si pStr est un nombre : sa valeur est retournée
    // si pStr est un interval x..y : une valeur au hasard dans [x,y] est
    // retournée
    public static int readIntParameter(String pStr)
    {
        int lMin, lMax, lResult;
        StringTokenizer lStrTok = new StringTokenizer(pStr, ":");
        // on lit une premiere valeur
        lMin = Integer.valueOf(lStrTok.nextToken()).intValue();
        lResult = lMin;
        // on essaye d'en lire une deuxieme
        try {
            lMax = Integer.valueOf(lStrTok.nextToken()).intValue();
            if (lMax > lMin) {
                // on choisit un nombre entre lMin et lMax
                lResult = (int) (Math.random() * (lMax - lMin + 1)) + lMin;
            }
        } catch (java.util.NoSuchElementException e) {
            // il n'y pas de deuxieme nombre et donc le nombre retourné correspond au
            // premier nombre
        }
        return lResult;
    }

}
