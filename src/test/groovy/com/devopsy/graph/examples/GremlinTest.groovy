package com.devopsy.graph.examples

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph
import com.tinkerpop.gremlin.groovy.Gremlin

class GremlinTest extends GroovyTestCase {
    static {
        Gremlin.load()
    }
    void testNeo4Gremlin() {
        Graph g = new TinkerGraph()
        String graphML = 'test.graphml'
        g.loadGraphML(graphML)
        def ig = new InferenceGraph(g)
        println 'Marko is thinking'
        def results = ig.solve(1)
        println(results)
        assertEquals(2, results.size)
        assertEquals('marko knows josh created ripple', results[0].join(' '))
        assertEquals('marko knows josh created lop', results[1].join(' '))
        g.v(1).sleepy = true
        println 'Sleepy Marko is thinking'
        results = ig.solve(1)
        println(results)
        assertEquals(1, results.size)
        assertEquals('marko knows josh created lop', results[0].join(' '))
    }
}
