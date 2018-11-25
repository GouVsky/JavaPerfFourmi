package org.polytechtours.performance.tp.fourmispeintre.utils;

public class MathsUtils
{
    /*************************************************************************************************
     * Titre : modulo Description : Fcontion de modulo permettant au fourmi de
     * reapparaitre de l autre coté du Canvas lorsque qu'elle sorte de ce dernier
     *
     * @param x
     *          valeur
     *
     * @return int
     */
    public static int modulo(int x, int m) {
        return (x + m) % m;
    }
}
