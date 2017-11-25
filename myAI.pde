class myAI extends Player{
  myAI instance;
  int[][] structures = new int[31][31];
  int turn;
  moveController mc;
  myAI(){
    setAIname("myAI");
    setImage(loadImage("myAI.png"));
    for(int x = 0;x <= 30; x++){
      for(int y = 0;y <= 30;y++){
        structures[x][y] = checkingStructure(x,y);
      }
    }
    turn = 0;
    mc = new moveController(this);
  }
  Player clone(){
    myAI ai;
    ai = new myAI();
    ai.AIname = this.AIname;
    ai.img = this.img;
    instance = ai;
    return ai;
  }
  int ai(){
    turn++;
    //エリア&体力判定
    if(this.getHp() < 75){
      return 7;
    }
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
    if(!mc.needMove()){
      //敵いたら打つ
      return shoot() != 0 ? shoot() : mc.moveToNearestStructure();
    }
    return mc.moveToCenter();
  }
  int shoot(){
    if(this.searchingOtherPlayer(getX(),getY()) && getWeapon() == 9 || this.getReloadTime() != 0){
      return 0;
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
  static direction split_1(direction dir){
    switch (dir){
      case NORTH_WEST : return direction.NORTH;
      case NORTH_EAST : return direction.NORTH;
      case SOUTH_EAST : return direction.SOUTH;
      case SOUTH_WEST : return direction.SOUTH;
      default: return dir;
    }
  }
  static direction split_2(direction dir){
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
  boolean needMove()
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
  boolean hasTarget(){
    if(targetStructure == null){
      return false;
    }
    return true;
  }
  int moveToCenter(){
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
      return moveToNearestStructure();
    }
    if(main.getY() == 15){
      if(main.getX() > 15){
        return _movetocenter(direction.EAST);
      }
      if(main.getX() < 15){
        return _movetocenter(direction.WEST);
      }
    }
    return moveToNearestStructure();
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
  int moveToNearestStructure(){
    if(!hasTarget() || checkingStructure(main.getX(),main.getY()) != 0 || checkingPoison(targetStructure[0],targetStructure[1]) == 1){
      float mindis = 32;
      int sx = 0;
      int sy = 0;
      for(int i = 0;i <= 30;i++){
        for(int j = 0;j <= 30;j++){
          if(main.structures[i][j] != 0){
            float d = distance(main.getX(),main.getY(),i,j);
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
      if(mindis == 32){
        return moveToCenter();
      }
      targetStructure = new int[2];
      targetStructure[0] = sx;targetStructure[1] = sy;
    }
    return moveTo(targetStructure[0],targetStructure[1]);
  }
  int move(direction dir)
  {
    switch (dir){
      case NORTH: return 1;
      case SOUTH: return 2;
      case WEST: return 3;
      case EAST: return 4;
      case NORTH_EAST: return 1;
      case NORTH_WEST: return 1;
      case SOUTH_EAST: return 2;
      case SOUTH_WEST: return 2;
      default: return (int)random(1,5);
    }
  }
  int moveTo(int x,int y){
    int nowX = main.getX();int nowY = main.getY();
    if(x != nowX){
      if(nowX > x){
        return move(direction.WEST);
      }
      return move(direction.EAST);
    }
    if(nowY > y){
      return move(direction.NORTH);
    }
    return move(direction.SOUTH);
  }
}
