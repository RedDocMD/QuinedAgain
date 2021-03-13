package org.deep.quined.algo

import java.lang.RuntimeException

enum class Term {
    Zero, One, DontCare;

    override fun toString() =
        when (this) {
            Zero -> "0"
            One -> "1"
            DontCare -> "-"
        }
}

class Cube(val terms: List<Term>, val minTerms: List<Int>) {
    constructor(minTerm: Int, termCount: Int) : this(minTermToTerm(minTerm, termCount), listOf(minTerm))

    val oneCount: Int
        get() = this.terms.count { it == Term.One }

    val dcCount: Int
        get() = this.terms.count { it == Term.DontCare }

    fun canJoin(other: Cube): Boolean {
        if (terms.size != other.terms.size)
            return false
        var diffFoundYet = false
        for ((term1, term2) in terms.zip(other.terms)) {
            if (term1 != term2) {
                if (!diffFoundYet) diffFoundYet = true
                else return false
            }
        }
        return diffFoundYet
    }

    fun joinCube(other: Cube): Cube {
        if (!canJoin(other)) throw CannotJoinCubeException()
        val joinedTerms =
            terms.zip(other.terms).map { (term1, term2) -> if (term1 == term2) term2 else Term.DontCare }
        val joinedMinTerms = minTerms.toMutableList()
        joinedMinTerms.addAll(other.minTerms)
        return Cube(joinedTerms, joinedMinTerms)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cube

        if (terms != other.terms) return false

        return true
    }

    override fun hashCode(): Int {
        return terms.hashCode()
    }
}

class CannotJoinCubeException : RuntimeException()

private fun minTermToTerm(minTerm: Int, termCount: Int): List<Term> {
    val terms = mutableListOf<Term>()
    var remaining = minTerm
    while (remaining > 0) {
        val rem = remaining % 2
        terms.add(if (rem == 0) Term.Zero else Term.One)
        remaining /= 2
    }
    val padding = List(termCount - terms.size) { Term.Zero }
    terms.addAll(padding)
    terms.reverse()
    return terms
}