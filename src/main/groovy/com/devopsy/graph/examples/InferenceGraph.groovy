package com.devopsy.graph.examples

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.gremlin.groovy.Gremlin

class InferenceGraph extends GroovyShell {
    def g

    static {
        Gremlin.load()
    }

    InferenceGraph(Graph graph) {
        g = graph
    }

    def solve(int id) {
        def results = g.v(id).as('u').sideEffect{u = it}
                .outE.sideEffect{l = it}
                .inV.outE.filter {it.condition == null || this.evaluate("${it.condition}")}
                .inV.loop('u') {it.object.lang != 'java' && it.loops < 50}
                .paths {it.name} {it.label}
        results.toList()
    }

    def run() {
        throw new Exception ("Just needed the shell")
    }
}
