package org.deep.quined.algo

fun partitionCubesByOneCount(cubes: List<Cube>): Map<Int, List<Cube>> {
    val partitions = mutableMapOf<Int, MutableList<Cube>>()
    for (cube in cubes) {
        partitions.getOrPut(cube.oneCount, { mutableListOf() }).add(cube)
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
    for (i in 0 until termCount) {
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

class Cover(private val cubes: List<Cube>) : Comparable<Cover> {
    val termsCovered: Set<Int>
        get(): Set<Int> {
            val minTerms = mutableSetOf<Int>()
            for (cube in cubes) minTerms.addAll(cube.minTerms)
            return minTerms
        }

    val size: Int
        get() = cubes.size

    override fun compareTo(other: Cover): Int {
        return cubes.size.compareTo(other.cubes.size)
    }

    override fun toString() =
        cubes.joinToString(", ", "{", "}") { cube ->
            cube.terms.joinToString(
                " ",
                "(",
                ")"
            ) { term -> term.toString() }
        }
}

fun findMinCovers(implicants: List<Cube>, minTerms: List<Int>, dcTerms: List<Int>): List<Cover> {
    val minTermsSet = minTerms.toSet()
    val dcTermSet = dcTerms.toSet()
    val allPossibleCovers = 1 shl implicants.size
    val validCovers = mutableListOf<Cover>()
    for (i in 0 until allPossibleCovers) {
        val implicantsSelected = mutableListOf<Cube>()
        for ((idx, cube) in implicants.withIndex())
            if ((1 shl idx).and(i) != 0) implicantsSelected.add(cube)
        val cover = Cover(implicantsSelected)
        if (cover.termsCovered.subtract(dcTermSet) == minTermsSet)
            validCovers.add(cover)
    }
    validCovers.sort()
    val minSize = validCovers[0].size
    return validCovers.takeWhile { it.size == minSize }
}
