package puissancequatre;

import processing.core.PApplet;

/**
 * Class Jeton pour projet PuissanceQuatre
 * @author Xavier
 */
public class Jeton {
  /** PApplet parent */
  private final PApplet app;
  /** Position x */
  private final float x;
  /** Position y */
  private float y;
  /** Velocite (animation) */
  private float yVel;
  /** Acceleration (animation) */
  private float yAcc;
  /** Position y max (animation) */
  private final float yMax;
  /** Rayon du jeton */
  private final int radius;
  /** Couleur du jeton */
  private final int color;
  /** Repere si le jeton appartient aux verts ou aux roses */
  private final boolean player1;

  /**
   * Constructeur du jeton
   * @param app PApplet parent
   * @param size Taille d'une case
   * @param placed Tableau des jetons places
   * @param player1 Tour du joueur jaune
   */
  public Jeton(PApplet app, int size, Jeton[][] placed, boolean player1) {
    this.app = app;
    radius = size / 2;
    x = (app.mouseX / size) * size + radius;
    y = -radius;
    yVel = 0;
    yAcc = 0.8f;
    yMax = computeYMax(placed);
    this.player1 = player1;
    if (player1) {
      color = app.color(191, 231, 0);
    } else {
      color = app.color(231, 0, 191);
    }
  }

  /** Affiche le jeton pendant l'animation */
  public void animate() {
    app.fill(color);
    app.ellipse(x, y, radius * 2, radius * 2);
    y += yVel;
    yVel += yAcc;
    if (y >= yMax) {
      y = yMax;
      yVel = 0;
      yAcc = 0;
    }
  }

  /** Affiche le jeton quand il est place */
  public void show() {
    app.fill(color);
    app.ellipse(x, y, radius * 2, radius * 2);
  }

  /**
   * Calcul la position verticale max que le jeton peut atteindre en cas de
   * presence d'autres jetons dans la colonne
   * @param placed Tableau des jetons places
   * @return Position verticale maximale
   */
  private float computeYMax(Jeton[][] placed) {
    int xIndex = app.mouseX / (radius * 2);
    if (xIndex >= placed.length) {
      xIndex = placed.length - 1;
    }
    if (xIndex < 0) {
      xIndex = 0;
    }
    int yIndex = 5;
    while (placed[xIndex][yIndex] != null) {
      yIndex--;
      if (yIndex == -1) {
        break;
      }
    }
    if (yIndex != -1) {
      return yIndex * radius * 2 + radius;
    }
    return 0;
  }
  
  // Getters
  public float getX() { return x; }
  public float getY() { return y; }
  public float getYMax() { return yMax; }
  public float getVel() { return yVel; }
  public boolean isPlayer1() { return player1; }
}
