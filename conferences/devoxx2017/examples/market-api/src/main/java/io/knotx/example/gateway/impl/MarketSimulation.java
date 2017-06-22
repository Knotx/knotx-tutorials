package io.knotx.example.gateway.impl;

import java.util.Random;

class MarketSimulation {

  private double currentPrice;
  private double randomShockVolatility;
  private Random random;

  MarketSimulation(double randomShockVolatility, double startPrice) {
    this.currentPrice = startPrice;
    this.randomShockVolatility = randomShockVolatility;
    this.random = new Random();
  }


  double simulate() {
    double shock;
    double volatilityParameter = this.randomShockVolatility * this.currentPrice;
    // The greater the current price, the greater the volatility
    shock = randomNormal(0, volatilityParameter, this.random);
    this.currentPrice += shock;
    return currentPrice;
  }

  /**
   * @return returns a double chosen from a normal distribution with mean and standard deviation
   * specified in the parameters
   */
  private static double randomNormal(double mean, double stdDev, Random random) {
    double number = random.nextGaussian();
    return number * stdDev + mean;
  }
}
