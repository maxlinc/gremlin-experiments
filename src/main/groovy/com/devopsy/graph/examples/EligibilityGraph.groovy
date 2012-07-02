package com.devopsy.graph.examples

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.gremlin.groovy.Gremlin

class EligibilityGraph extends GroovyShell {
    Graph g
    List<List<String>> solutions

    static {
        Gremlin.load()
    }

    EligibilityGraph(Graph graph) {
        g = graph
    }

    def dumbSolve(Object id) {
        def results = g.v(id).outE.filter{it.duration > 365}.inV.outE.inV.paths {it.name} {it.description}
        solutions = results.toList()
    }

    def solve(Object id) {
        def results =
//            Find the User node and save it as u
            g.v(id).as('u').sideEffect{u = it}
//            Save the edge before the condition as l
                .outE.sideEffect{l = it}
//            Save the edge to be filtered as x
                .inV.outE.sideEffect{x = it}
//            Filter based on the "condition" closure
                .filter {it.condition == null || this.evaluate("${it.condition}")}
//            Keep going until we find a Credit Union
                .inV.loop('u') {it.object.type != 'CU' && it.loops < 50}
//            Format our results
                .paths {it.name} {it.description}
        solutions = results.toList()
    }

    List<String> getDisplayPaths() {
        if(solutions == null) throw new IllegalStateException("Solve first")
        solutions.collect{it.join(' ')}
    }

    List<String> getEligibleCUs() {
        if(solutions == null) throw new IllegalStateException("Solve first")
        solutions.collect{it[-1]}.unique()
    }

    def run() {
        throw new Exception ("Just needed the shell")
    }
}
