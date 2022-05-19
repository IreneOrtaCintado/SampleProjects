package com.ioc.catchgame.gameEngine

var difficulty: Int = 2
var frameRate: Int = 60

var scoreFactor: Int = 0
var startingStep: Int = 0
var stepIncrease: Int = 0
var maxEnemies: Int = 0
var intervalAddingEnemies: Int = 0
var delayEnemyAppearance: Long = 0
var maxFruit: Int = 0
var intervalAddingFruit: Int = 0

fun setDifficultyEasy() {
    scoreFactor = 1
    startingStep = 5
    stepIncrease = 1
    maxEnemies = 2
    intervalAddingEnemies = 60
    delayEnemyAppearance = 30
    maxFruit = 4
    intervalAddingFruit = 30
}

fun setDifficultyNormal() {
    scoreFactor = 2
    startingStep = 8
    stepIncrease = 2
    maxEnemies = 3
    intervalAddingEnemies = 45
    delayEnemyAppearance = 30
    maxFruit = 6
    intervalAddingFruit = 25
}

fun setDifficultyHard() {
    scoreFactor = 4
    startingStep = 12
    stepIncrease = 3
    maxEnemies = 5
    intervalAddingEnemies = 40
    delayEnemyAppearance = 30
    maxFruit = 10
    intervalAddingFruit = 20
}