# Voyager's Journey

<img width="400" alt="Screen Shot 2023-04-09 at 9 39 37 PM" src="https://user-images.githubusercontent.com/89917595/230820162-ffbec6ba-5e82-4c39-a862-3e50e229b7c7.png"> <img width="400" alt="Screen Shot 2023-04-09 at 9 39 06 PM" src="https://user-images.githubusercontent.com/89917595/230820301-e0f446a6-61fe-4adf-9d83-6cbbb6c1851d.png">
<img width="400" alt="Screen Shot 2023-04-09 at 9 39 42 PM" src="https://user-images.githubusercontent.com/89917595/230820155-4a8f82a8-249e-4eef-8612-07bd70e15dc0.png"> <img width="400" alt="Screen Shot 2023-04-09 at 9 38 47 PM" src="https://user-images.githubusercontent.com/89917595/230820264-fa5e086b-c730-45a3-b291-485b79c299df.png">


## Game Overview:

Voyager's Journey is a 2D space game where a player controls a spacecraft called Voyager accompanied by two soldiers orbiting around it. The goal is to safely navigate to the end of our solar system by avoiding enemies and obstacles while shooting them down with the soldiers. The game ends when either the player loses all lives or reaches the end of the map.

### 🏆 Demo - Win
https://user-images.githubusercontent.com/89917595/230820745-97f6e7e4-6716-423e-967e-735054e6b859.mov

### 😭 Demo - Lose
https://user-images.githubusercontent.com/89917595/230820749-f79376d1-ebe9-4049-bbbe-0e6446a6a477.mov


## Implementation
#### Parenting & Orbiting:
- Two soldiers are orbiting Voyager object
  ```
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
  ```

#### Enemy’s movement: easing
- move() function within the Enemy class controls the random movement of the enemy.
  ```
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
  ```

#### Assets:
- I created background and some text assets(restart button, copyright) with Figma
  <img width="911" alt="Screen Shot 2023-04-09 at 11 41 07 PM" src="https://user-images.githubusercontent.com/89917595/230821107-60e93569-22ec-4824-8f29-f4323dfcb32c.png">


## Game Design
#### Constants:
- Max lives (3)
- Initial positions of voyager and soldiers
- Bullet speed

#### Variables:
- Live status
- Voyager's position (x, y coordinates)
- Soldiers' positions (x, y coordinates)
- Bullets’ positions (x, y coordinates)
- Enemies’ (mad scientist, asteroid)
  - positions (x, y coordinates)
  - type
  - health (mad scientist: 2, asteroid: 1)
- Game state (isEnd, isLost)

#### Input:
- Arrow keys to move the Voyager up and down
- Spacebar to fire bullets from both soldiers

#### Behavior:
- onDraw()
  - Draw background, voyager, soldiers, bullets, enemies, and other UI elements
  - Check for collisions (bullets ⇔ enemies) (voyager ⇔ enemies)
  - Update game state based on collisions, lives remaining

- updateVoyager()
  - Update voyager's position based on arrow key inputs

- spawnEnemy()
  - Spawn enemies at random positions and with random types

- checkCollision()
  - Check for collisions between game objects (bullets, enemies, and voyager)

- resetGame()
  - Reset lives, position of voyager and soldier, remove bullets and enemies

