import GaFr.GFGame;
import GaFr.GFStamp;
import GaFr.GFFont;
import GaFr.Gfx;
import GaFr.GFPixels;
import GaFr.GFKeyboard;
import GaFr.GFKey;
import java.util.ArrayList;

public class Game extends GFGame
{
  {
    Gfx.clearColor(Gfx.Color.BLACK);
  }

  GFStamp[] slices;  // Slices of the background image
  int offset = 0;

  GFFont font = new GFFont("gafr/fonts/spleen/spleen-16x32.ffont.json");
  boolean isEnd = false;
  boolean isLost = false;

  GFStamp restartButton = new GFStamp("assets/restart-button.png").resize(200, 50);
  float restartButtonX = (800 - restartButton.width) / 2;
  float restartButtonY = 300;
  GFStamp copyright = new GFStamp("assets/copyright.png");
  float copyrightX = 800 - 155;
  float copyrightY = 500 - 20;

  GFStamp voyager = new GFStamp("assets/voyager.png").resize(100,100);
  Voyager myVoyager = new Voyager(50,200);

  GFStamp soldier1 = new GFStamp("assets/soldier1.png").resize(80,80);
  GFStamp soldier2 = new GFStamp("assets/soldier2.png").resize(80,80);
  Soldier soldierOne = new Soldier(myVoyager.x, myVoyager.y - soldier1.height, soldier1);
  Soldier soldierTwo = new Soldier(myVoyager.x, myVoyager.y + myVoyager.picture.height, soldier2);

  int maxBullets = 4;
  GFStamp bullet = new GFStamp("assets/bullet.png").resize(25,10);
  public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

  ArrayList<Enemy> enemies = new ArrayList<>();
  GFStamp madScientistImage = new GFStamp("assets/mad-scientist.png").resize(80, 80);
  GFStamp asteroidImage = new GFStamp("assets/asteroid.png").resize(60, 60);

  int maxLives = 3;
  int lives = 3;
  GFStamp heart = new GFStamp("assets/heart.png").resize(40, 40);
  GFStamp heartEmpty = new GFStamp("assets/heart-empty.png").resize(40, 40);

  {
    // Load the background image and split it into tiles (bg: 8000*500 pixels)
    GFPixels[] slicepix = new GFPixels("assets/background.png").splitIntoTilesBySize(800, 500);
    slices = new GFStamp[slicepix.length]; // 8000 / 800 = 10
    for (int i = 0; i < slicepix.length; ++i) { slices[i] = new GFStamp(slicepix[i]); }
  }

  class Voyager {
    double x, y;
    GFStamp picture;

    public Voyager(double x, double y) {
      this.x = x;
      this.y = y;
      this.picture = voyager;
    }

    boolean canMove (double dy) {
      double newY = (this.y + dy);
      if (newY < 50.0 || newY > 350.0) {
        return false;
      }
      return true;
    }
  }

  class Soldier {
    double x, y;
    GFStamp picture;
  
    public Soldier(double x, double y, GFStamp picture) {
      this.x = x;
      this.y = y;
      this.picture = picture;
    }
  }

  class Bullet {
    double x, y;
    GFStamp picture;

    public Bullet(double x, double y, GFStamp picture) {
      this.x = x;
      this.y = y;
      this.picture = picture;
    }

    public void fire() {
      this.x += 5;
    }
  }

  class Enemy {
    String type;
    GFStamp picture;
    double x, y;
    int health;

    double targetY;
  
    public Enemy(String type, double x, double y, GFStamp picture, int health) {
      this.type = type;
      this.x = x;
      this.y = y;
      this.picture = picture;
      this.health = health;

      this.targetY = y;
    }

    public void move() {
      this.x -= 1; // Match the scroll speed
  
      // Occasionally update the target y-coordinate with a random value
      if (Math.random() < 0.05) {
        double newTargetY = 100 + Math.random() * 300;
        if (newTargetY >= 100 && newTargetY <= 400) {
          this.targetY = newTargetY;
        }
      }
      this.y += (this.targetY - this.y) * 0.02;
    }
  }
  
  void drawSprites(double x, double y, GFStamp picture) {
    picture.moveTo((float) x, (float) y).stamp();
  }

  public void resetGame() {
    offset = 0;
    lives = maxLives;
    bullets.clear();
    enemies.clear();
    myVoyager.x = 50;
    myVoyager.y = 200;
    isEnd = false;
    isLost = false;
  }

  @Override
  public void onMouseUp(int x, int y, int buttons, int flags, int button)
  {
    if (isEnd && x >= restartButtonX && x <= restartButtonX + restartButton.width && y >= restartButtonY && y <= restartButtonY + restartButton.height)
    {
      resetGame();
    }
  }

  @Override
  public void onKeyDown(String key, int code, int flags)
  { 
    switch (code)
    {
      case GFKey.Space :
      {
        if (bullets.size() < maxBullets) {
          // Soldier 1's bullet
          Bullet shot1 = new Bullet(soldierOne.x+50 , soldierOne.y+30, bullet);
          bullets.add(shot1);

          // Soldier 2's bullet
          Bullet shot2 = new Bullet(soldierTwo.x+50, soldierTwo.y+30, bullet);
          bullets.add(shot2);
          break;
        }
        break;
      }
    }
  }

  public void updateVoyager() {
    if (GFKeyboard.isDown(GFKey.ArrowUp)) {
      if (myVoyager.canMove(-1.0))  --myVoyager.y;
    }
    if (GFKeyboard.isDown(GFKey.ArrowDown)) {
      if (myVoyager.canMove(1.0))  ++myVoyager.y;
    }
  } 

  public void spawnEnemy() {
    int randType = (int) (Math.random() * 2);
    double randX = 600 + Math.random() * 100; // 600 - 700
    double randY = 100 + Math.random() * 300; // 100 - 400
  
    Enemy enemy;
    if (randType == 0) {
      enemy = new Enemy("mad-scientist", randX, randY, madScientistImage, 2);
    } else {
      enemy = new Enemy("asteroid", randX, randY, asteroidImage, 1);
    }
  
    enemies.add(enemy);
  }

  boolean checkCollision(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
    return (x1 < x2 + w2) && (x1 + w1 > x2) && (y1 < y2 + h2) && (y1 + h1 > y2);
  }
  
  @Override
  public void onDraw(int frameCount)
  {
    if (!isEnd) {
      if (lives <= 0) {
        isEnd = true;
        isLost = true;
      } else if (offset >= slices.length * 800 - 800) {
        isEnd = true;
        isLost = false;
      }
    }
  
    if (!isEnd) {
      offset += 2;
    }
  
    int sliceNum = offset / 800;
    int remainder = offset % 800;
  
    for (int i = 0; i < 2; ++i)
    {
        int currentSlice = sliceNum + i;
        if (currentSlice < slices.length) {
            slices[currentSlice].moveTo(-remainder + 800 * i, 0).stamp();
        }
    }
  
    if (isEnd) {
      String message = isLost ? "Try again!\nYou've got this" : "Bravo!\nSolar system\nexploration complete!";
      font.draw(300, 175, message);
      restartButton.moveTo(restartButtonX, restartButtonY).stamp();
      return;
    }

    if (frameCount % 120 == 0) {
      if (enemies.size() < 3) {spawnEnemy();}
    }

    drawSprites(myVoyager.x, myVoyager.y, myVoyager.picture);

    double orbitRadius = 80;
    double orbitSpeed = 0.01;
    double angle = frameCount * orbitSpeed;

    // Update soldiers' positions relative to the voyager and make them orbit
    soldierOne.x = myVoyager.x + orbitRadius * Math.cos(angle);
    soldierOne.y = myVoyager.y + orbitRadius * Math.sin(angle);
    soldierTwo.x = myVoyager.x + orbitRadius * Math.cos(angle + Math.PI);
    soldierTwo.y = myVoyager.y + orbitRadius * Math.sin(angle + Math.PI);

    // Draw soldiers
    drawSprites(soldierOne.x, soldierOne.y, soldierOne.picture);
    drawSprites(soldierTwo.x, soldierTwo.y, soldierTwo.picture);

    if (bullets != null) {
      for (int i = 0; i < bullets.size(); i++) {
        if (bullets.get(i).x > 800) {
          bullets.remove(i);
        } else {
          drawSprites(bullets.get(i).x, bullets.get(i).y, bullets.get(i).picture);
          bullets.get(i).fire();
        }
      }
    }

    // Check for collisions
    for (int i = 0; i < enemies.size(); i++) {
      Enemy enemy = enemies.get(i);
      double enemyX = enemy.x;
      double enemyY = enemy.y;
      double enemyW = enemy.picture.width;
      double enemyH = enemy.picture.height;

      if (enemyX < 0) {
        enemies.remove(i);
      }

      // Check collision with bullets
      for (int j = 0; j < bullets.size(); j++) {
        Bullet bullet = bullets.get(j);
        double bulletX = bullet.x;
        double bulletY = bullet.y;
        double bulletW = bullet.picture.width;
        double bulletH = bullet.picture.height;

        if (checkCollision(enemyX, enemyY, enemyW, enemyH, bulletX, bulletY, bulletW, bulletH)) {
          enemy.health -= 1;
          bullets.remove(j);

          if (enemy.health <= 0) {
            enemies.remove(i);
          }
        }
      }

      // Check collision with voyager
      if (checkCollision(enemyX, enemyY, enemyW, enemyH, myVoyager.x, myVoyager.y, myVoyager.picture.width, myVoyager.picture.height)) {
        lives -= 1;
        enemies.remove(i);
      }
    }

    // Draw enemies and update their positions
    for (int i = 0; i < enemies.size(); i++) {
      drawSprites(enemies.get(i).x, enemies.get(i).y, enemies.get(i).picture);
      enemies.get(i).move();
    }
  
    // Display lives
    for (int i = 0; i < maxLives; i++) {
      if (i < lives) {
        drawSprites(10 + i * heart.width, 10, heart);
      } else {
        drawSprites(10 + i * heartEmpty.width, 10, heartEmpty);
      }
    }

    // Display copyright
    copyright.moveTo(copyrightX, copyrightY).stamp();

    updateVoyager();
  }
}
