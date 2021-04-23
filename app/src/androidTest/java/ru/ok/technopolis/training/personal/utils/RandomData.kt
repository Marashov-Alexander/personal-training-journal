package ru.ok.technopolis.training.personal.utils

fun randomNumber(): Int {
    return (Math.random() * 1000).toInt() % 1000
}

fun randomWorkoutName(): String {
    return "Workout" + randomNumber()
}

fun randomExerciseName(): String {
    return "Exercise" + randomNumber()
}

fun randomParameterName(): String {
    return "Parameter" + randomNumber()
}

fun randomMinutes(): Int {
    return randomNumber() % 60
}

fun randomHours(): Int {
    return randomNumber() % 24
}
