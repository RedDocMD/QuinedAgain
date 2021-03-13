package org.deep.quined.algo

fun partitionCubesByOneCount(cubes: List<Cube>): Map<Int, List<Cube>> {
    val partitions = mutableMapOf<Int, MutableList<Cube>>()
    for (cube in cubes) {
        partitions.getOrPut(cube.oneCount, { mutableListOf(cube) }).add(cube)
    }
    return partitions
}

data class ReductionStepResult(val newCubes: List<Cube>, val oldPrimes: List<Cube>)

fun reduceOneStep(partitionedCubes: Map<Int, List<Cube>>, termCount: Int): ReductionStepResult {
    val newCubes = mutableListOf<Cube>()
    val cubesCovered = mutableMapOf<Cube, Boolean>()
    for ((_, cubes) in partitionedCubes) {
        for (cube in cubes) {
            cubesCovered[cube] = false
        }
    }
    for (i in 1 until termCount) {
        val lowerList = partitionedCubes[i]
        val higherList = partitionedCubes[i + 1]
        if (lowerList != null && higherList != null) {
            for (lowerCube in lowerList) {
                for (higherCube in higherList) {
                    if (lowerCube.canJoin(higherCube)) {
                        newCubes.add(lowerCube.joinCube(higherCube))
                        cubesCovered[lowerCube] = true
                        cubesCovered[higherCube] = true
                    }
                }
            }
        }
    }
    val cubesNotCovered = mutableListOf<Cube>()
    for ((cube, covered) in cubesCovered) {
        if (!covered) cubesNotCovered.add(cube)
    }
    return ReductionStepResult(newCubes, cubesNotCovered)
}