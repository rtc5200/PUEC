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

  String getAIname() {
    return this.AIname;
  }
  int getX() {
    return this.x;
  }
  int getY() {
    return this.y;
  }
  int getWeapon() {
    return this.weapon;
  }
  int getItem() {
    return this.item;
  }
  int getHp() {
    return this.hp;
  }
  PImage getImg() {
    return this.img;
  }
  boolean getLive() {
    return this.live;
  }
  int getRank() {
    return this.rank;
  }
  int getReloadTime(){
    return this.reloadTime;
  }

  int getDroppedItem(){
    return battleGround[this.x][this.y].getItem();
  }
  int getDroppedWeapon(){
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

  // リストからプレイヤーへの移行
  Player clone() {
    Player foo;
    foo = new Player();
    foo.AIname = this.AIname;
    foo.img = this.img;
    return foo;
  }

  int ai() {
    return 0;
  }

  // 移動の関数
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
    if(this.reloadTime <= 0 ){
    for (int i = 0; i<weaponStatus[this.weapon][1]+1&& this.y -i > -1; i++) {
      if (battleGround[this.x][this.y -i].structure == 0) {
        battleGround[this.x][this.y -i].bullet[0] = this.weapon;
        battleGround[this.x][this.y -i].bullet[1] = playerNumber+1;
        if(battleGround[this.x][this.y].structure == 2){
          if(this.weapon == 9){
            battleGround[this.x][this.y].bullet[0] = this.weapon;
            battleGround[this.x][this.y].bullet[1] = playerNumber +1;
          }
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
            if(this.weapon == 9){
              battleGround[this.x][this.y].bullet[0] = this.weapon;
              battleGround[this.x][this.y].bullet[1] = playerNumber +1;
            }
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
          if(battleGround[this.x][this.y].structure == 2 ){
            if(this.weapon == 9){
              battleGround[this.x][this.y].bullet[0] = this.weapon;
              battleGround[this.x][this.y].bullet[1] = playerNumber +1;
            }
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
              if(this.weapon == 9){
                battleGround[this.x][this.y].bullet[0] = this.weapon;
                battleGround[this.x][this.y].bullet[1] = playerNumber +1;
              }
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
  boolean searchingOtherPlayer(int x, int y) {
    boolean findout = false;
    if ( abs(this.x-x) >3 && abs(this.y -y) > 3 && battleGround[x][y].structure == 2) {
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
