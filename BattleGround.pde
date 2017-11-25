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

//BattleGroundの新規作成
void initializingBattleGround(BattleGround[][] battleGround) {
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
  int house = 10; //家の数
  int houseDistance = 10;
  int hut = 20;   //小屋の数
  int hutDistance = 6;
  int x, y; //家,小屋を置くための仮座標
  int count = 0;
  int bullet = 0;
  int battleSound = 0;
  boolean exist = false;
  while (count < house ) {
    x = int(random(battleGround.length)) ;
    y = int(random(battleGround.length)) ;

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
    x = int(random(battleGround.length)) ;
    y = int(random(battleGround.length)) ;

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

void spownningItem(BattleGround[][] battleGround) {
  for (int i = 0; i<battleGround.length-1; i++) {
    for (int j = 0; j<battleGround.length-1; j++) {
      int x = int(random(100));
      switch(battleGround[i][j].structure) {
      case 0:
        battleGround[i][j].item = 0;
        battleGround[i][j].weapon = 0;
        break;
      case 2:
        if (x > 70) {
          battleGround[i][j].item = int(random(3))+1;
        } else {
          battleGround[i][j].weapon = int(random(3))+1;
        }
        break;
      case 1:
        if (x > 70) {
          battleGround[i][j].weapon = int(random(3))+1;
        } else {
          battleGround[i][j].item = int(random(3))+1;
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