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

class Cube(private val terms: List<Term>, private val minTerms: List<Int>) {
    constructor(minTerm: Int, termCount: Int) : this(minTermToTerm(minTerm, termCount), listOf(minTerm))
    constructor(terms: List<Term>) : this(terms, termsToMinTerms(terms))

    val oneCount: Int
        get() = this.terms.count() { it == Term.One }

    val termCount: Int
        get() = this.terms.size

    fun canJoin(other: Cube): Boolean {
        if (terms.size != other.terms.size)
            return false;
        var diffFoundYet = false
        for ((term1, term2) in terms.zip(other.terms)) {
            if (term1 != term2) {
                if (!diffFoundYet) diffFoundYet = false
                else return false;
            }
        }
        return diffFoundYet
    }

    fun joinCube(other: Cube): Cube {
        if (!canJoin(other)) throw CannotJoinCubeException()
        val joinedTerms =
            terms.zip(other.terms).map { (term1, term2) -> if (term1 == term2) term2 else Term.DontCare }
        return Cube(joinedTerms)
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


private fun termsToMinTerms(terms: List<Term>): MutableList<Int> {
    if (Term.DontCare !in terms) {
        return mutableListOf(termsToInt(terms))
    }
    val dontCareIdx = terms.indexOf(Term.DontCare)
    val withOne = terms.toMutableList()
    val withZero = terms.toMutableList()
    withOne[dontCareIdx] = Term.One
    withZero[dontCareIdx] = Term.Zero
    val withOneMinTerms = termsToMinTerms(withOne)
    val withZeroMinTerms = termsToMinTerms(withZero)
    withZeroMinTerms.addAll(withOneMinTerms)
    return withZeroMinTerms
}

private fun termsToInt(terms: List<Term>): Int {
    var minTerm = 0
    for (term in terms) {
        minTerm *= 2
        minTerm += when (term) {
            Term.Zero -> 0
            Term.One -> 1
            Term.DontCare -> throw NonMinTermCubeException()
        }
    }
    return minTerm
}

class NonMinTermCubeException : RuntimeException()