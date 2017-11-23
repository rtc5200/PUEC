class sampleAI extends Player { //write this line
  sampleAI() {
    setAIname("sampleAI"); //write this line
    setImage(loadImage("sampleAI.png")); //write this line
  }

  Player clone() {
    Player ai;
    ai = new sampleAI(); //write this line
    ai.AIname = this.AIname;
    ai.img = this.img;
    return ai;
  }

  int ai() {
    /* write here */
    return 0;
  }
}
