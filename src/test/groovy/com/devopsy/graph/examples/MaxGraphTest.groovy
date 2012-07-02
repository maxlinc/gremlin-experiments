package com.devopsy.graph.examples

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph
import com.tinkerpop.gremlin.groovy.Gremlin
import org.junit.Before

class MaxGraphTest extends GroovyTestCase {
    static {
        Gremlin.load()
    }

    Graph g
    InferenceGraph ig

    @Before
    void setUp() {
        g = new TinkerGraph()
        g.loadGraphML('max.graphml')
        ig = new InferenceGraph(g)
    }

    def dumbSolve(Object id) {
        def results = g.v(id).outE.filter{it.duration > 365}.inV.outE.inV.paths {it.name} {it.description}
        results.toList()
    }

    void testKnownTraversalLessThanYear() {
        g.e('Max_TW').duration = 364
        def results = dumbSolve('Max')
        assertEquals(0, results.size)
    }

    void testKnownTraversalMoreThanYear() {
        def results = dumbSolve('Max')
        assertEquals(1, results.size)
        assertEquals('Max works at ThoughtWorks which qualifies for TW Credit Union', results[0].join(' '))
    }

    void testMaxLessThanYear() {
        g.e('Max_TW').duration = 364
        def results = ig.solve('Max')
        assertEquals(0, results.size)
    }

    void testMaxMoreThanYear() {
        def results = ig.solve('Max')
        assertEquals(1, results.size)
        def paths = ig.getDisplayPaths()
        def creditUnions = ig.getEligibleCUs()
        assertTrue(paths.contains('Max works at ThoughtWorks which qualifies for TW Credit Union'))
        assertEquals(creditUnions, ['TW Credit Union'])
    }
}
