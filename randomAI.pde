class randomAI extends Player {
  randomAI() {
    setAIname("randomAI");
    setImage(loadImage("randomAI.png"));
  }

  Player clone() {
    Player ai;
    ai = new randomAI();
    ai.AIname = this.AIname;
    ai.img = this.img;
    return ai;
  }

  int ai() {
    return int(random(12));
  }
}