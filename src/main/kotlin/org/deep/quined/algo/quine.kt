package org.deep.quined.algo

fun partitionCubesByOneCount(cubes: List<Cube>): Map<Int, List<Cube>> {
    val partitions = mutableMapOf<Int, MutableList<Cube>>()
    for (cube in cubes) {
        partitions.getOrPut(cube.oneCount, { mutableListOf(cube) }).add(cube)
    }
    return partitions
}

data class ReductionStepResult(val newCubes: List<Cube>, val oldPrimes: List<Cube>)
