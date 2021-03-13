package org.deep.quined

import org.deep.quined.algo.Cube
import org.deep.quined.algo.partitionCubesByOneCount
import org.deep.quined.algo.reduceOneStep
import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    println("Please enter the number of variables:")
    val termCount = Integer.parseInt(reader.readLine())
    println("Please enter the min-terms (space separated):")
    val minTerms = reader.readLine().split(" ").map { Cube(Integer.parseInt(it), termCount) }
    println("Please enter the don't cares (space separated, none for none):")
    val line = reader.readLine()
    val dontCares = if (line == "none") listOf() else line.split(" ").map { Cube(Integer.parseInt(it), termCount) }
    val allCubes = minTerms.toMutableList()
    allCubes.addAll(dontCares)
    doQuineMcClusky(allCubes, termCount)
}

fun doQuineMcClusky(initCubes: List<Cube>, termCount: Int) {
    var partitions = partitionCubesByOneCount(initCubes)
    val primeImplicants = mutableListOf<Cube>()
    while (true) {
        val (nextCubes, leftovers) = reduceOneStep(partitions, termCount)
        primeImplicants.addAll(leftovers)
        printPartitions(partitions, leftovers)
        println("")
        println("")
        if (nextCubes.isEmpty()) break
        partitions = partitionCubesByOneCount(nextCubes)
    }
}

fun printPartitions(parts: Map<Int, List<Cube>>, leftovers: List<Cube>) {
    for ((oneCount, cubes) in parts) {
        val dcCount = cubes.first().dcCount
        println("<$oneCount:1, $dcCount:D>")
        println("---------------------------")
        for (cube in cubes) {
            val variables = cube.terms.joinToString(" ") { it.toString() }
            val minTerms = cube.minTerms.joinToString { it.toString() }
            val tail = if (cube in leftovers) "" else "✓"
            println("$variables | ($minTerms) $tail")
        }
        println("---------------------------")
        println()
    }
}