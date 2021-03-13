package org.deep.quined

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
    constructor(terms: List<Term>) : this(terms, termsToMinTerms(terms))
}

fun canJoin(cube1: Cube, cube2: Cube): Boolean {
    if (cube1.terms.size != cube2.terms.size)
        return false;
    var diffFoundYet = false
    for ((term1, term2) in cube1.terms.zip(cube2.terms)) {
        if (term1 != term2) {
            if (!diffFoundYet) diffFoundYet = false
            else return false;
        }
    }
    return diffFoundYet
}

// Precondition: canJoin cube1 and cube2
fun joinCube(cube1: Cube, cube2: Cube): Cube {
    val joinedTerms =
        cube1.terms.zip(cube2.terms).map { (term1, term2) -> if (term1 == term2) term2 else Term.DontCare }
    return Cube(joinedTerms)
}

fun minTermToTerm(minTerm: Int, termCount: Int): List<Term> {
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


fun termsToMinTerms(terms: List<Term>): MutableList<Int> {
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

fun termsToInt(terms: List<Term>): Int {
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