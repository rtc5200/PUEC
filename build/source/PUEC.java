import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PUEC extends PApplet {

BattleGround[][] battleGround = new BattleGround[30][30];
int battleTime;
int turn ;
int rank = 0;
int scene;
int pointor;
int howManySelectedPlayer;
static int member = 8;
Player[] playerList;
Player[] selectedPlayer;
PImage[] playerIcon;
PFont font;
PImage titleLogo;
static int[][] weaponStatus= {{0, 0, 0}, {20, 2, 2}, {30, 2, 3}, {25, 3, 3}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {15, 0, 0}};
static int[] itemStatus  ={0, 10, 25, 15, 0, 0, 0, 0, 0, 5};

public void setup() {
  
  background(128);
  frameRate(60);
  font = loadFont("IPAPGothic-32.vlw");
  titleLogo = loadImage("PUEC_Logo.png");
  textFont(font, 32);
  battleTime = 0;
  turn = 0;
  scene = 0;
  pointor = 0;
  howManySelectedPlayer = 0;
  for (int i = 0; i < battleGround.length; i++) {
    for (int j = 0; j < battleGround.length; j++) {
      battleGround[i][j] = new BattleGround();
    }
  }
  initializingBattleGround(battleGround);
  spownningItem(battleGround);
  playerList = new Player[20];
  selectedPlayer = new Player[20];
  playerIcon = new PImage[20];
  for (int i= 0; i < 20; i++) {
    playerList[i] = null;
    selectedPlayer[i] = null;
    playerIcon[i] = null;
  }
  /*write here */
  playerList[0] = new randomAI();
  playerList[1] = new randomAI();
  playerList[2] = new randomAI();
  playerList[5] = new myAI();
  playerList[10] = new randomAI();
  //playerList[i] = new sampleAI();

  /*write here*/

  for (int i = 0; i < 20; i++) {
    if (playerList[i] != null) {
      playerList[i].img.resize(width/10, height/10);
    }
  }
}

public void draw() {
  if (scene == 0 ) {
    background(128);
    textSize(50);
    fill(0xffFFFF00);
    textAlign(CENTER, CENTER);
    imageMode(CENTER);
    image(titleLogo, width/2, height/3);
    text("PRESS ANY KEY", width/2, height*5/8);
    textAlign(LEFT);
    textSize(16);
    text("version 1.2", 0, 16);
    battleTime = 0;
    turn = 0;
    howManySelectedPlayer = 0;
  } else if (scene == 1) {
    textSize(50);
    stroke(0);
    background(128);
    //draw playerList
    imageMode(CORNER);
    for (int i = 0; i < 20; i++) {
      // \u4e0a\u6bb5
      if (i < 10) {
        fill(255);
        rect(i*width/10, height*2/3 - height/10, width/10, height/10);
        if (playerList[i] != null) {
          image(playerList[i].img, i * width/10, height*2/3 - height/10);
        }
        //\u4e0b\u6bb5
      } else {
        fill(255);
        rect((i-10)*width/10, height*2/3, width/10, height/10);
        if (playerList[i] != null) {
          image(playerList[i].img, (i-10)*width/10, height*2/3);
        }
      }
    }
    stroke(0xffFF0000);
    strokeWeight(5);
    fill(0, 0);
    if (pointor < 10) {
      rect(pointor*width/10, height*2/3 - height/10, width/10, height/10, 5);
    } else {
      rect((pointor-10)*width/10, height*2/3, width/10, height/10, 5);
    }
    //draw selected player
    fill(255);
    stroke(0);
    for (int i = 0; i < 8; i++) {
      rect((i+1)*width/10, height/3, width/10, height/10);
      if (selectedPlayer[i] != null) {
        image(selectedPlayer[i].img, (i+1)*width/10, height/3);
      }
    }
    text(howManySelectedPlayer, width/2, height/2);
  } else if (scene == 2) {
    background(255);
    stroke(0);
    strokeWeight(1);
    // drrawing map
    for (int i = 0; i < battleGround.length; i++) {
      for (int j = 0; j < battleGround.length; j++) {
        switch(battleGround[i][j].structure) {
        case 0:
          fill(0xffA0FFA0);
          break;
        case 1:
          fill(0xffFF0000);
          break;
        case 2:
          fill(0xff0000FF);
          break;
        default:
          fill(0xffFFFFFF);
          break;
        }
        rect(i*width/40, j*height/40, width/40, height/40);
        if (battleGround[i][j].bullet[0] != 0) {
          fill(0xffFF0000);
          ellipse(i*width/40+width/80, j*height/40+height/80, width/80, height/80);
        }
        // \u6bd2\u306e\u63cf\u5199
        if (battleGround[i][j].poison == 1) {
          fill(0xffA901DB, 100);
          rect(i*width/40, j*height/40, width/40, height/40);
        } else if (battleGround[i][j].poisonCheck == 1) {
          fill(0xffF000F0, 100);
          rect(i*width/40, j*height/40, width/40, height/40);
        }
      }
    }

    for (int i = 0; i <  howManySelectedPlayer; i++) {
      //\u53f3\u5074\u306e\u30e1\u30f3\u30d0\u30fc\u30ea\u30b9\u30c8\u306e\u63cf\u5199
      fill(255);
      rect(width*3/4, i*height/(member+1), width/4, height/(member+1));
      image(selectedPlayer[i].img, width*3/4, i*height/(member+1));
      fill(0);
      textSize(height*3/160);
      text(selectedPlayer[i].AIname, width*3/4+width/10, i*height/(member+1)+height/50);
      fill(255, 0, 0);
      rect(width*17/20, i*height/(member+1)+height/45, width/7, height/50);
      if (selectedPlayer[i].hp > 0) {
        fill(60, 250, 60);
        rect(width*3/4+width/10, i*height/(member+1)+height/45, (width/7)*(PApplet.parseFloat(selectedPlayer[i].hp)/100), height/50);
      }
      textSize(height*3/160);
      fill(0);
      text("x:" + selectedPlayer[i].x + "y:" + selectedPlayer[i].y, width*17/20, i*height/(member+1)+height/15);
      switch(selectedPlayer[i].getWeapon()) {
      case 0:
        text("\u6b66\u5668:\u306a\u3057", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 1:
        text("\u6b66\u5668:\u30cf\u30f3\u30c9\u30ac\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 2:
        text("\u6b66\u5668:\u30b7\u30e7\u30c3\u30c8\u30ac\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 3:
        text("\u6b66\u5668:\u30e9\u30a4\u30d5\u30eb", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 9:
        text("\u6b66\u5668:\u30d5\u30e9\u30a4\u30d1\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      default:
        text("\u6b66\u5668:?", width*17/20, i*height/(member+1)+height/15+height*3/160);
      }
      switch(selectedPlayer[i].getItem()) {
      case 0:
        text("\u30a2\u30a4\u30c6\u30e0:\u306a\u3057", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 1:
        text("\u30a2\u30a4\u30c6\u30e0:\u5305\u5e2f", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 2:
        text("\u30a2\u30a4\u30c6\u30e0:\u6551\u6025\u30ad\u30c3\u30c8", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 3:
        text("\u30a2\u30a4\u30c6\u30e0:\u93ae\u75db\u5264", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 9:
        text("\u30a2\u30a4\u30c6\u30e0:\u3061\u304f\u308f", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      default:
        text("\u30a2\u30a4\u30c6\u30e0:?", width*17/20, i*height/(member+1)+height/15+height*3/80);
      }
      if (!selectedPlayer[i].live) {
        fill(0, 70);
        rect(width*3/4, i*height/(member+1), width/4, height/(member+1));
        fill(0xffFFFF00);
        textSize(height/(member+1)-height/15);
        text(selectedPlayer[i].getRank(), width*3/4+width/7, i*height/(member+1)+height/17);
      }
      //\u30de\u30c3\u30d7\u4e0a\u306e\u30e1\u30f3\u30d0\u30fc\u306e\u4f4d\u7f6e\u306e\u63cf\u5199
      if (turn > 0) {
        image(playerIcon[i], selectedPlayer[i].x*width/40, selectedPlayer[i].y*height/40);
      }
    }
    if (battleTime > 60 && battleTime % 600 == 0) {
      startingTurn();
      for (int i =0; i < howManySelectedPlayer; i++) {
        if (turn == 0) {
          playerIcon[i] = loadImage(selectedPlayer[i].AIname+ ".png");
          playerIcon[i].resize(width/40, height/40);
          selectedPlayer[i].fallingAt(PApplet.parseInt(random(battleGround.length-1)), PApplet.parseInt(random(battleGround.length-1)));
          selectedPlayer[i].setHp(100);
          selectedPlayer[i].live = true;
          selectedPlayer[i].weapon = 9;
          selectedPlayer[i].item = 9;
          selectedPlayer[i].rank = 0;
        } else {
          // turn > 0 \u4ee5\u964d\u306e\u30d7\u30ec\u30a4\u30e4\u30fc\u306e\u884c\u52d5
          if (selectedPlayer[i].live) {
            switch(selectedPlayer[i].ai()) {
            case 1:
              selectedPlayer[i].moveN();
              break;
            case 2:
              selectedPlayer[i].moveS();
              break;
            case 3:
              selectedPlayer[i].moveW();
              break;
            case 4:
              selectedPlayer[i].moveE();
              break;
            case 5:
              selectedPlayer[i].pickUpWeapon();
              break;
            case 6:
              selectedPlayer[i].pickUpItem();
              break;
            case 7:
              selectedPlayer[i].useItem();
              break;
            case 8:
              selectedPlayer[i].shootWeaponN(i);
              break;
            case 9:
              selectedPlayer[i].shootWeaponS(i);
              break;
            case 10:
              selectedPlayer[i].shootWeaponW(i);
              break;
            case 11:
              selectedPlayer[i].shootWeaponE(i);
              break;
            default:
            }
          }
        }
      }
      endingTurn();
      turn++;
    }
    battleTime += 10;
    fill(0xff000000);
    textSize(width/40);
    text("turn:" + turn, 0, height*3/4+width/40);
  } else if (scene == 3) {
    stroke(0);
    strokeWeight(1);
    // drrawing map
    for (int i = 0; i < battleGround.length; i++) {
      for (int j = 0; j < battleGround.length; j++) {
        switch( battleGround[i][j].structure) {
        case 0:
          fill(0xffA0FFA0);
          break;
        case 1:
          fill(0xffFF0000);
          break;
        case 2:
          fill(0xff0000FF);
          break;
        default:
          fill(0xffFFFFFF);
          break;
        }
        rect(i*width/40, j*height/40, width/40, height/40);
        // \u6bd2\u306e\u63cf\u5199
        if (battleGround[i][j].poison == 1) {
          fill(0xffA901DB, 100);
          rect(i*width/40, j*height/40, width/40, height/40);
        } else if (battleGround[i][j].poisonCheck == 1) {
          fill(0xffF000F0, 100);
          rect(i*width/40, j*height/40, width/40, height/40);
        }
      }
    }
    for (int i = 0; i < howManySelectedPlayer; i++) {
      //\u53f3\u5074\u306e\u30e1\u30f3\u30d0\u30fc\u30ea\u30b9\u30c8\u306e\u63cf\u5199
      fill(255);
      rect(width*3/4, i*height/(member+1), width/4, height/(member+1));
      image(selectedPlayer[i].img, width*3/4, i*height/(member+1));
      fill(0);
      textSize(height*3/160);
      text(selectedPlayer[i].AIname, width*3/4+width/10, i*height/(member+1)+height/50);
      fill(255, 0, 0);
      rect(width*17/20, i*height/(member+1)+height/45, width/7, height/50);
      if (selectedPlayer[i].hp > 0) {
        fill(60, 250, 60);
        rect(width*3/4+width/10, i*height/(member+1)+height/45, (width/7)*(PApplet.parseFloat(selectedPlayer[i].hp)/100), height/50);
      }
      textSize(height*3/160);
      fill(0);
      text("x:" + selectedPlayer[i].hp + "y:" + selectedPlayer[i].y, width*17/20, i*height/(member+1)+height/15);
      switch(selectedPlayer[i].getWeapon()) {
      case 0:
        text("\u6b66\u5668:\u306a\u3057", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 1:
        text("\u6b66\u5668:\u30cf\u30f3\u30c9\u30ac\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 2:
        text("\u6b66\u5668:\u30b7\u30e7\u30c3\u30c8\u30ac\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 3:
        text("\u6b66\u5668:\u30e9\u30a4\u30d5\u30eb", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      case 9:
        text("\u6b66\u5668:\u30d5\u30e9\u30a4\u30d1\u30f3", width*17/20, i*height/(member+1)+height/15+height*3/160);
        break;
      default:
        text("\u6b66\u5668:?", width*17/20, i*height/(member+1)+height/15+height*3/160);
      }
      switch(selectedPlayer[i].getItem()) {
      case 0:
        text("\u30a2\u30a4\u30c6\u30e0:\u306a\u3057", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 1:
        text("\u30a2\u30a4\u30c6\u30e0:\u5305\u5e2f", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 2:
        text("\u30a2\u30a4\u30c6\u30e0:\u6551\u6025\u30ad\u30c3\u30c8", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 3:
        text("\u30a2\u30a4\u30c6\u30e0:\u93ae\u75db\u5264", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      case 9:
        text("\u30a2\u30a4\u30c6\u30e0:\u3061\u304f\u308f", width*17/20, i*height/(member+1)+height/15+height*3/80);
        break;
      default:
        text("\u30a2\u30a4\u30c6\u30e0:?", width*17/20, i*height/(member+1)+height/15+height*3/80);
      }
      if (!selectedPlayer[i].live) {
        fill(0, 70);
        rect(width*3/4, i*height/(member+1), width/4, height/(member+1));
        fill(0xffFFFF00);
        textSize(height/(member+1)-height/15);
        text(selectedPlayer[i].getRank(), width*3/4+width/7, i*height/(member+1)+height/17);
      }
      //\u30de\u30c3\u30d7\u4e0a\u306e\u30e1\u30f3\u30d0\u30fc\u306e\u4f4d\u7f6e\u306e\u63cf\u5199
      if (turn > 0) {
        image(playerIcon[i], selectedPlayer[i].x*width/40, selectedPlayer[i].y*height/40);
      }
    }
    for (int i = 0; i < howManySelectedPlayer; i++) {
      if (selectedPlayer[i].live) {
        textSize(48);
        fill(0xffFFFF00);
        text("Winner:" + selectedPlayer[i].AIname, width/4-width/8, height*3/8);
        image(selectedPlayer[i].img, width/4+width/20, height*3/8+16);
      }
    }
  }
}

public void startingTurn() {
  for (int i = 0; i < battleGround.length; i++) {
    for (int j = 0; j < battleGround.length; j++) {
      if (turn >= 15 && turn%10 >= 5) {
        spreadingPoison(battleGround, i, j);
      }
      battleGround[i][j].bullet[0] = 0;
      battleGround[i][j].bullet[1] = 0;
      battleGround[i][j].battleSound = 0;
    }
  }

  for (int i = 0; i < howManySelectedPlayer; i++) {
    if (selectedPlayer[i].hp <= 0 && selectedPlayer[i].live ) {
      selectedPlayer[i].setLive(false);
      selectedPlayer[i].rank = howManySelectedPlayer - rank;
      rank++;
    }
  }
  if (rank == howManySelectedPlayer - 1) {
    scene = 3;
  }
}


public void endingTurn() {
  for (int i = 0; i < battleGround.length; i++) {
    for (int j = 0; j < battleGround.length; j++) {
      if (turn >= 14 && turn%10 >=4) {
        checkPoison(battleGround, i, j);
      }
    }
  }
  for (int i = 0; i < howManySelectedPlayer; i++) {
    if (battleGround[selectedPlayer[i].getX()][selectedPlayer[i].getY()].poison == 1 && selectedPlayer[i].live) {
      selectedPlayer[i].decreasingHp(10);
    }
    if (battleGround[selectedPlayer[i].getX()][selectedPlayer[i].getY()].bullet[1] != PApplet.parseInt(i)+1 && selectedPlayer[i].live) {
      selectedPlayer[i].decreasingHp(weaponStatus[battleGround[selectedPlayer[i].getX()][selectedPlayer[i].getY()].bullet[0]][0]);
    }
    if (selectedPlayer[i].hp <= 0 && selectedPlayer[i].live) {
      selectedPlayer[i].setLive(false);
      selectedPlayer[i].rank = howManySelectedPlayer - rank;
      rank++;
    }
    if (selectedPlayer[i].reloadTime > 0) {
      selectedPlayer[i].reloadTime--;
    }
  }
}

public void keyPressed() {
  switch(scene) {
  case 0:
    scene = 1;
    break;
  case 1:
    if (keyCode == SHIFT) {
      scene = 2;
    } else if (keyCode == UP) {
      if (pointor < 10) {
        pointor += 10;
      } else {
        pointor -= 10;
      }
    } else if (keyCode == DOWN) {
      if (pointor < 10) {
        pointor += 10;
      } else {
        pointor -= 10;
      }
    } else if (keyCode == LEFT) {
      if (pointor == 0 || pointor == 10) {
        pointor += 9;
      } else {
        pointor -= 1;
      }
    } else if (keyCode == RIGHT) {
      if (pointor == 9 || pointor == 19) {
        pointor -= 9;
      } else {
        pointor += 1;
      }
    } else if (key == 'z') {
      if (howManySelectedPlayer < 8 && playerList[pointor] != null) {
        selectedPlayer[howManySelectedPlayer] = playerList[pointor].clone();
        howManySelectedPlayer ++;
      } else if (howManySelectedPlayer == 8) {
        scene = 2;
      }
    } else if (key == 'x') {
      if (howManySelectedPlayer > 0) {
        selectedPlayer[howManySelectedPlayer-1] = null;
        howManySelectedPlayer --;
      } else {
        scene = 0;
      }
    }
    break;
  case 3:
    initializingBattleGround(battleGround);
    for (int i= 0; i < howManySelectedPlayer; i++) {
      selectedPlayer[i]= null;
    }
    howManySelectedPlayer = 0;
    rank = 0;
    scene = 1;
    break;
  }
}
public  class BattleGround {
  private int item = 0, weapon = 0, structure, poison, poisonCheck,battleSound;
  private int[] bullet = new int[2];
  BattleGround() {
  }

  BattleGround(int item, int structure, int poison, int poisonCheck) {
    this.item = item;
    this.structure = structure;
    this.poison = poison;
    this.poisonCheck = poisonCheck ;
  }
  public int getItem(){
    return this.item;
  }
  public int getWeapon(){
    return this.weapon;
  }
}

//BattleGround\u306e\u65b0\u898f\u4f5c\u6210
public void initializingBattleGround(BattleGround[][] battleGround) {
  for (int i = 0; i<battleGround.length; i++) {
    for (int j =0; j<battleGround.length; j++) {
      battleGround[i][j].structure = 0;
      battleGround[i][j].bullet[0] = 0;
      battleGround[i][j].bullet[1] = 0;
      battleGround[i][j].item = 0;
      battleGround[i][j].weapon = 0;
      battleGround[i][j].poison = 0;
      battleGround[i][j].poisonCheck = 0;
    }
  }
  int house = 10; //\u5bb6\u306e\u6570
  int houseDistance = 10;
  int hut = 20;   //\u5c0f\u5c4b\u306e\u6570
  int hutDistance = 6;
  int x, y; //\u5bb6,\u5c0f\u5c4b\u3092\u7f6e\u304f\u305f\u3081\u306e\u4eee\u5ea7\u6a19
  int count = 0;
  int bullet = 0;
  int battleSound = 0;
  boolean exist = false;
  while (count < house ) {
    x = PApplet.parseInt(random(battleGround.length)) ;
    y = PApplet.parseInt(random(battleGround.length)) ;

    for (int i=-houseDistance/2; i <houseDistance/2 && x+i>-1 && x+i < battleGround.length; i++) {
      for (int j= 0; j > -1*(houseDistance/2 -abs(i)) && y+j>-1; j--) {
        if (battleGround[x+i][y+j].structure == 1) {
          exist = true;
        }
      }
      for (int j = 1; j < houseDistance/2 -abs(i) && y+j <battleGround.length-1; j++ ) {
        if (battleGround[x+i][y+j].structure ==1) {
          exist = true;
        }
      }
    }
    if (!exist) {
      battleGround[x][y].structure = 1;
      count ++;
    }
    exist =false ;
  }
  count =0;
  while (count<hut) {
    x = PApplet.parseInt(random(battleGround.length)) ;
    y = PApplet.parseInt(random(battleGround.length)) ;

    for (int i=-hutDistance/2; i <hutDistance/2 && x+i>-1 && x+i < battleGround.length; i++) {
      for (int j= 0; j > -1*(hutDistance/2 -abs(i)+1) && y+j>-1; j--) {
        if (battleGround[x+i][y+j].structure == 2) {
          exist = true;
        }
      }
      for (int j = 0; j < hutDistance/2 -abs(i)+1 && y+j <battleGround.length; j++ ) {
        if (battleGround[x+i][y+j].structure ==2) {
          exist = true;
        }
      }
    }
    if (!exist) {
      battleGround[x][y].structure = 2;
      count ++;
    }
    exist =false;
  }
}

public void spownningItem(BattleGround[][] battleGround) {
  for (int i = 0; i<battleGround.length-1; i++) {
    for (int j = 0; j<battleGround.length-1; j++) {
      int x = PApplet.parseInt(random(100));
      switch(battleGround[i][j].structure) {
      case 0:
        battleGround[i][j].item = 0;
        battleGround[i][j].weapon = 0;
        break;
      case 2:
        if (x > 70) {
          battleGround[i][j].item = PApplet.parseInt(random(4))+1;
        } else {
          battleGround[i][j].weapon = PApplet.parseInt(random(4))+1;
        }
        break;
      case 1:
        if (x > 70) {
          battleGround[i][j].weapon = PApplet.parseInt(random(4))+1;
        } else {
          battleGround[i][j].item = PApplet.parseInt(random(4))+1;
        }
        break;
      }
      battleGround[i][j].poison = 0;
    }
  }
}

private void checkPoison(BattleGround[][] battleGround, int i, int j) {
  if (i != 0 && j !=0 && i !=battleGround.length-1 && j!=battleGround.length-1)
  {
    if (battleGround[i-1][j].poison == 1 || battleGround[i +1][j].poison == 1) {
      if (battleGround[i][j-1].poison == 1 || battleGround[i][j+1].poison == 1) {
        battleGround[i][j].poisonCheck =1;
      }
    }
  }
  if ((i != 0 && i != battleGround.length-1) && (j == 0 || j == battleGround.length-1)) {
    if (battleGround[i-1][j].poison == 1 || battleGround[i+1][j].poison == 1) {
      battleGround[i][j].poisonCheck = 1;
    }
  } else if ((i == 0 || i == battleGround.length-1) &&(j != 0 && j != battleGround.length-1)) {
    if (battleGround[i][j-1].poison == 1 || battleGround[i][j+1].poison == 1) {
      battleGround[i][j].poisonCheck = 1;
    }
  }

  if ( i == 0 && j == 0)battleGround[i][j].poisonCheck = 1;
  if ( i == 0 && j == battleGround.length-1)battleGround[i][j].poisonCheck = 1;
  if ( i == battleGround.length-1 && j == 0)battleGround[i][j].poisonCheck = 1;
  if ( i == battleGround.length-1 && j == battleGround.length-1)battleGround[i][j].poisonCheck = 1;
}
private void spreadingPoison(BattleGround[][] battleGround, int i, int j) {
  if (battleGround[i][j].poisonCheck ==1) {
    battleGround[i][j].poison = 1;
  }
}

public int checkingStructure(int x, int y) {
  if (x > -1 && y > -1 && x < battleGround.length && y <battleGround.length) {
    return battleGround[x][y].structure;
  } else {
    return 0;
  }
}
public int getBattleGroundItem(int x,int y){
  return battleGround[x][y].item;
}
public int getBattleGroundWeapon(int x,int y){
  return battleGround[x][y].weapon;
}
public int checkingPoison(int x, int y) {
  if (x > -1 && y > -1 && x <battleGround.length && y < battleGround.length) {
    return battleGround[x][y].poison ;
  } else {
    return 0;
  }
}
public int checkingCheckPoison(int x, int y) {
  if (x > -1 && y > -1 && x <battleGround.length && y < battleGround.length) {
    return battleGround[x][y].poisonCheck;
  } else {
    return 0;
  }
}
public class Player {
  String AIname;
  private int x, y;
  private int weapon;
  private int item ;
  private int hp;
  PImage img;
  private boolean live;
  private int rank;
  private int reloadTime;
  Player (){};

  public String getAIname() {
    return this.AIname;
  }
  public int getX() {
    return this.x;
  }
  public int getY() {
    return this.y;
  }
  public int getWeapon() {
    return this.weapon;
  }
  public int getItem() {
    return this.item;
  }
  public int getHp() {
    return this.hp;
  }
  public PImage getImg() {
    return this.img;
  }
  public boolean getLive() {
    return this.live;
  }
  public int getRank() {
    return this.rank;
  }
  public int getReloadTime(){
    return this.reloadTime;
  }

  public int getDroppedItem(){
    return battleGround[this.x][this.y].getItem();
  }
  public int getDroppedWeapon(){
    return battleGround[this.x][this.y].getWeapon();
  }


  public void setAIname(String AIname) {
    this.AIname = AIname;
  }
  public void setImage(PImage image) {
    this.img = image;
  }
  private void setWeapon(int weapon) {
    this.weapon = weapon;
  }
  private void setItem(int item) {
    this.item = item;
  }
  private void setHp(int hp) {
    this.hp = hp;
  }
  private void setLive(boolean live) {
    this.live = live;
  }
  private void fallingAt(int x, int y) {
    if (turn == 0) {
      this.x = x;
      this.y = y;
    }
  }

  // \u30ea\u30b9\u30c8\u304b\u3089\u30d7\u30ec\u30a4\u30e4\u30fc\u3078\u306e\u79fb\u884c
  public Player clone() {
    Player foo;
    foo = new Player();
    foo.AIname = this.AIname;
    foo.img = this.img;
    return foo;
  }

  public int ai() {
    return 0;
  }

  // \u79fb\u52d5\u306e\u95a2\u6570
  public void moveN() {
    if (this.y > 0) {
      this.y -= 1;
    }
  }
  public void moveS() {
    if (this.y < battleGround.length -1)
    {
      this.y += 1;
    }
  }
  public void moveW() {
    if (this.x > 0) {
      this.x -=1;
    }
  }
  public void moveE() {
    if (this.x <battleGround.length -1) {
      this.x += 1;
    }
  }
  public void pickUpWeapon() {
    int stack;
    stack = this.getWeapon();
    this.weapon = battleGround[this.x][this.y].weapon;
    battleGround[this.x][this.y].weapon  = stack;
  }

  public void pickUpItem() {
    int stack;
    stack = this.item;
    this.setItem(battleGround[this.x][this.y].item);
    battleGround[this.item][this.y].item = stack;
  }
  public void useItem() {
    switch(this.item) {
    case 1:
      if (this.hp + itemStatus[1] >100) {
        this.hp = 100;
      } else {
        this.hp += itemStatus[1];
      }
      this.item = 0;
      break;
    case 2:
      if (this.hp + itemStatus[2] > 100) {
        this.hp = 100;
      } else {
        this.hp += itemStatus[2];
      }
      this.item = 0;
      break;
    case 3:
      if (this.hp + itemStatus[3] > 100) {
        this.hp = 100;
      } else {
        this.hp += itemStatus[3];
      }
      this.item =0;
    case 9:
      if (this.hp + itemStatus[9] >100) {
        this.hp =100;
      } else {
        this.hp += itemStatus[9];
      }
      this.item = 0;
      break;
    default:
      this.item = 0;
      break;
    }
  }
  public void decreasingHp(int x) {
    this.hp -= x;
  }
  public void shootWeaponN(int playerNumber) {
    battleGround[this.x][this.y].battleSound = 1;
    if(this.reloadTime <= 0 &&battleGround[this.x][this.y].structure != 2){
    for (int i = 0; i<weaponStatus[this.weapon][1]+1&& this.y -i > -1; i++) {
      if (battleGround[this.x][this.y -i].structure == 0) {
        battleGround[this.x][this.y -i].bullet[0] = this.weapon;
        battleGround[this.x][this.y -i].bullet[1] = playerNumber+1;
        if(battleGround[this.x][this.y].structure == 2){
          return ;
        }
      } else if (battleGround[this.x ][this.y -i].structure == 1) {
        return ;
      }
    }
    this.reloadTime = weaponStatus[this.weapon][2];
  }
  }
  public void shootWeaponS(int playerNumber) {
    battleGround[this.x][this.y].battleSound = 1;
    if(this.reloadTime <= 0 && battleGround[this.x][this.y].structure != 2){
      for (int i = 0; i<weaponStatus[this.weapon][1]+1 && this.y+i < battleGround.length && y-i > -1; i++) {
        if (battleGround[this.x][this.y +i].structure == 0) {
          battleGround[this.x][this.y +i].bullet[0] = this.weapon;
          battleGround[this.x][this.y +i].bullet[1] = playerNumber +1;
          if(battleGround[this.x][this.y].structure == 2){
            return ;
          }
          } else if (battleGround[this.x ][this.y +i].structure == 1) {
              return ;
            }
    }
    this.reloadTime = weaponStatus[this.weapon][2];
  }
  }
  public void shootWeaponW(int playerNumber) {
    battleGround[this.x][this.y].battleSound = 1;
    if(this.reloadTime <= 0 ){
      for (int i = 0; i<weaponStatus[this.weapon][1]+1 && this.x -i > -1; i++) {
        if (battleGround[this.x -i][this.y ].structure == 0) {
          battleGround[this.x-i][this.y ].bullet[0] = this.weapon;
          battleGround[this.x-i][this.y ].bullet[1] = playerNumber+1;
          if(battleGround[this.x][this.y].structure == 2){
            return ;
          }

        } else if (battleGround[this.x -i][this.y ].structure == 1) {
            return ;
        }
        }
        this.reloadTime = weaponStatus[this.weapon][2];
      }
  }
  public void shootWeaponE(int playerNumber) {
      battleGround[this.x][this.y].battleSound = 1;
      if(this.reloadTime <= 0 && battleGround[this.x][this.y].structure != 2){
        for (int i = 0; i<weaponStatus[this.weapon][1]+1 && this.x +i < battleGround.length; i++) {
          if (battleGround[this.x +i][this.y ].structure == 0) {
            battleGround[this.x + i][this.y ].bullet[0] = this.weapon;
            battleGround[this.x +i][this.y ].bullet[1] = playerNumber+1;
            if(battleGround[this.x][this.y].structure == 2){
              return ;
            }
          } else if (battleGround[this.x +i][this.y ].structure == 1) {
              return ;
          }
        }
        this.reloadTime = weaponStatus[this.weapon][2];
      }
  }

  public int[] hearingGunSound() {
    int[] sound = {0, 0, 0, 0};
    for (int i = -5; i<1 && this.x + i > -1; i++) {
      for (int j = 0; j< 5 +abs(i) && this.y + j < battleGround.length; j++ ) {
        if (battleGround[this.x+i][this.y+j].battleSound == 1) {
          sound[0] = 1;
        }
      }
      for (int j = 0; j> -1*(5+abs(i)) &&this.y + j > -1; i--) {
        if (battleGround[this.x+i][this.y+j].battleSound == 1) {
          sound[1] = 1;
        }
      }
    }
    for (int i = 0; i<6 && this.x + i <battleGround.length; i++) {
      for (int j = 0; j< 5 +abs(i) && this.y + j < battleGround.length; j++ ) {
        if (battleGround[this.x+i][this.y+j].battleSound == 1) {
          sound[2] = 1;
        }
      }
      for (int j = 0; j> -1*(5+abs(i)) &&this.y + j > -1; i--) {
        if (battleGround[this.x+i][this.y+j].battleSound == 1) {
          sound[3] = 1;
        }
      }
    }
    return sound;
  }
  public boolean searchingOtherPlayer(int x, int y) {
    boolean findout = false;
    if ( abs(this.x-x) >3 || abs(this.y -y) > 3|| battleGround[x][y].structure == 2) {
      findout = false;
    } else {
      for (int i = 0; i<member; i++) {
        if (selectedPlayer[i].x == x && selectedPlayer[i].y == y) {

          findout = true;
        }
      }
    }
    return findout;
  }
}
class myAI extends Player{
  myAI instance;
  int[] priority = new int[2];//attack,move,
  int[][] structures = new int[31][31];
  myAI(){
    setAIname("myAI");
    setImage(loadImage("myAI.png"));
    for(int x = 0;x <= 30; x++){
      for(int y = 0;y <= 30;y++){
        structures[x][y] = checkingStructure(x,y);
      }
    }
  }
  public Player clone(){
    myAI ai;
    ai = new myAI();
    ai.AIname = this.AIname;
    ai.img = this.img;
    instance = ai;
    return ai;
  }
  public int ai(){
    //\u30a8\u30ea\u30a2&\u4f53\u529b\u5224\u5b9a
    moveController mc = new moveController(this);
    if(this.getDroppedItem() != 0){
      if(this.getItem() == 0 || this.getItem() == 9){
        return 6;
      }
      if(this.getHp() < 100){
        return 7;
      }
    }
    if(this.getDroppedWeapon() != 0){
      if(this.getWeapon() == 9){
        return 5;
      }
      if(this.getDroppedWeapon() == 2){
        return 5;
      }
    }
    if(mc.hasTarget()){
      return mc.moveToNearestStructure();
    }
    if(!mc.needMove()){
      //\u6575\u3044\u305f\u3089\u6253\u3064
      if(this.getHp() < 50){
        return 7;
      }
      if(shoot() != 0){shoot();}
      return mc.moveToNearestStructure();
    }
    return mc.moveToCenter();
  }
  public int shoot(){
    if(this.searchingOtherPlayer(getX(),getY()) && getWeapon() == 9){
      return (int)random(1,4);
    }
    for(int ox = -2;ox < 3;ox++){
      for(int oy = -2;oy < 3;oy++){
        if(this.searchingOtherPlayer(getX()+ox,getY()+oy)){
          if(oy == 0){
            if(ox < 0){
              return 10;
            }
            return 11;
          }
          if(ox == 0){
            if(oy < 0){
              return 8;
            }
            return 9;
          }
        }
      }
    }
    return 0;
  }
}

enum direction{
  NORTH,NORTH_EAST,EAST,SOUTH_EAST,SOUTH,SOUTH_WEST,WEST,NORTH_WEST;
  public static direction split_1(direction dir){
    switch (dir){
      case NORTH_WEST : return direction.NORTH;
      case NORTH_EAST : return direction.NORTH;
      case SOUTH_EAST : return direction.SOUTH;
      case SOUTH_WEST : return direction.SOUTH;
      default: return dir;
    }
  }
  public static direction split_2(direction dir){
    switch (dir){
      case NORTH_WEST : return direction.WEST;
      case SOUTH_WEST : return direction.WEST;
      case NORTH_EAST : return direction.EAST;
      case SOUTH_EAST : return direction.EAST;
      default: return dir;
    }
  }
}
class moveController{
  myAI main;
  int[] targetStructure;
  moveController(myAI main){
    this.main = main;
  }
  public boolean needMove()
  {
    for(int ox = -1;ox < 2;ox++){
      for(int oy = -1;oy < 2;oy++){
        if(checkingPoison(main.getX()+ox,main.getY()+oy) == 1){
          return true;
        }
      }
    }
    return false;
  }
  public boolean hasTarget(){
    if(targetStructure == null){
      return false;
    }
    return true;
  }
  public int moveToCenter(){
    if(main.getX() < 15){
      if(main.getY() < 15){
        return _movetocenter(direction.SOUTH_EAST);
      }
      return _movetocenter(direction.NORTH_EAST);
    }
    if(main.getX() > 15){
      if(main.getY() < 15){
        return _movetocenter(direction.SOUTH_WEST);
      }
      return _movetocenter(direction.NORTH_WEST);
    }
    if(main.getX() == 15){
      if(main.getY() > 15){
        return _movetocenter(direction.NORTH);
      }
      if(main.getY() < 15){
        return _movetocenter(direction.SOUTH);
      }
      return 0;
    }
    if(main.getY() == 15){
      if(main.getX() > 15){
        return _movetocenter(direction.EAST);
      }
      if(main.getX() < 15){
        return _movetocenter(direction.WEST);
      }
    }
    return 0;
  }
  private int _movetocenter(direction dir){
    int nowX = main.getX();
    int nowY = main.getY();
    direction dir1 = direction.split_1(dir);
    direction dir2 = direction.split_2(dir);
    if (distance(15,15,calX(nowX,dir1),calY(nowY,dir1)) > distance(15,15,calX(nowX,dir2),calY(nowY,dir2))){
      return move(dir1);
    }
    return move(dir2);
  }
  private float distance(int x1,int y1,int x2,int y2)
  {
    return sqrt((x1-x2)^2+(y1-y2)^2);
  }
  private int calX(int x,direction dir){
    if(dir == direction.EAST || dir == direction.NORTH_EAST || dir == direction.SOUTH_EAST){
      return x + 1;
    }else if(dir == direction.WEST || dir == direction.NORTH_WEST || dir == direction.SOUTH_WEST){
      return x - 1;
    }
    return x;
  }
  private int calY(int y,direction dir){
    if(dir == direction.NORTH || dir == direction.NORTH_EAST || dir == direction.NORTH_WEST){
      return y - 1;
    }else if(dir == direction.SOUTH || dir == direction.SOUTH_EAST || dir == direction.SOUTH_WEST){
      return y + 1;
    }
    return y;
  }
  public int moveToNearestStructure(){
    if(!hasTarget() || checkingStructure(main.getX(),main.getY()) != 0 || checkingPoison(targetStructure[0],targetStructure[1]) == 1){
      float mindis = -1;
      int sx = 0;
      int sy = 0;
      for(int i = 0;i <= 30;i++){
        for(int j = 0;j <= 30;j++){
          if(main.structures[i][j] != 0){
            float d = distance(main.getX(),main.getY(),i,j);
            if(mindis == -1){
              mindis = d;
              sx=i;
              sy=j;
              continue;
            }
            if(d <= mindis){
              if(checkingPoison(i,j) == 0 || checkingCheckPoison(i,j) == 0 && d != 0){
                mindis = d;
                sx=i;
                sy=j;
                main.structures[i][j] = 0;
              }
            }
          }
        }
      }
      if(mindis == -1){
        return 0;
      }
      targetStructure = new int[2];
      targetStructure[0] = sx;targetStructure[1] = sy;
    }
    return moveTo(targetStructure[0],targetStructure[1]);
  }
  public int move(direction dir)
  {
    switch (dir){
      case NORTH: return 1;
      case SOUTH: return 2;
      case WEST: return 3;
      case EAST: return 4;
      case NORTH_EAST:
      case NORTH_WEST: return 1;
      case SOUTH_EAST:
      case SOUTH_WEST: return 2;
      default: return 0;
    }
  }
  public int moveTo(int x,int y){
    direction dir1 = null;
    direction dir2 = null;
    if(main.getX() - x > 0){
      dir1 = direction.EAST;
    }else if(main.getX() - x < 0){
      dir1 = direction.WEST;
    }
    if(main.getY() - y > 0){
      dir2 = direction.NORTH;
    }else if(main.getY() - y < 0){
      dir2 = direction.SOUTH;
    }
    if(dir1 == null){
      if(dir2 == null){
        return 0;
      }
      return move(dir2);
    }
    if(dir2 == null){
      return move(dir1);
    }
    int nowX = main.getX();int nowY = main.getY();
    if (abs(distance(nowX,nowY,calX(nowX,dir1),calY(nowY,dir1)) - distance(nowX,nowY,calX(nowX,dir2),calY(nowY,dir2))) > 1){
      if(distance(nowX,nowY,calX(nowX,dir1),calY(nowY,dir1)) > distance(nowX,nowY,calX(nowX,dir2),calY(nowY,dir2))){
        return move(dir2);
      }
      return move(dir1);
    }
    if(random(2) == 1){
      return move(dir1);
    }
    return move(dir2);
  }
}
class randomAI extends Player {
  randomAI() {
    setAIname("randomAI");
    setImage(loadImage("randomAI.png"));
  }

  public Player clone() {
    Player ai;
    ai = new randomAI();
    ai.AIname = this.AIname;
    ai.img = this.img;
    return ai;
  }

  public int ai() {
    return PApplet.parseInt(random(12));
  }
}
class sampleAI extends Player { //write this line
  sampleAI() {
    setAIname("sampleAI"); //write this line
    setImage(loadImage("sampleAI.png")); //write this line
  }

  public Player clone() {
    Player ai;
    ai = new sampleAI(); //write this line
    ai.AIname = this.AIname;
    ai.img = this.img;
    return ai;
  }

  public int ai() {
    /* write here */
    return 0;
  }
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PUEC" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
