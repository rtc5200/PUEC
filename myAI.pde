class myAI extends Player{
  myAI instance;
  int[] priority = new int[2];//attack,move,
  myAI(){
    setAIname("myAI");
    setImage(loadImage("myAI.png"));
  }
  Player clone(){
    Player ai;
    ai = new myAI();
    ai.AIname = this.AIname;
    ai.img = this.img;
    instance = ai;
    return ai;
  }
  int ai(){
    //エリア&体力判定
    moveController mc = new moveController(this);
    if(!mc.needMove){
      //敵いたら打つ
    }
    return int(random(12));
  }
}

enum direction{
  NORTH,NORTH_EAST,EAST,SOUTH_EAST,SOUTH,SOUTH_WEST,WEST,NORTH_WEST
}
class moveController{
  myAI main;
  moveController(myAI main){
    this.main = main;
  }
  boolean needMove()
  {
    return true;
  }
  int moveToCenter(){
    return 0;
  }
  int moveToNearestStructure(){
    return 0;
  }
}
