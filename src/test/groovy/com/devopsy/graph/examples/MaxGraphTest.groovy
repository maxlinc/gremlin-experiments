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
    EligibilityGraph eg

    @Before
    void setUp() {
        g = new TinkerGraph()
        g.loadGraphML('max.graphml')
        eg = new EligibilityGraph(g)
    }

    void testKnownTraversalLessThanYear() {
        g.e('Max_TW').duration = 364
        def results = eg.dumbSolve('Max')
        assertEquals(0, results.size)
    }

    void testKnownTraversalMoreThanYear() {
        def results = eg.dumbSolve('Max')
        assertEquals(1, results.size)
        assertEquals(eg.getDisplayPaths(), ['Max works at ThoughtWorks which qualifies for TW Credit Union'])
    }

    void testMaxLessThanYear() {
        g.e('Max_TW').duration = 364
        def results = eg.solve('Max')
        assertEquals(0, results.size)
    }

    void testMaxMoreThanYear() {
        def results = eg.solve('Max')
        assertEquals(1, results.size)
        def paths = eg.getDisplayPaths()
        def creditUnions = eg.getEligibleCUs()
        assertTrue(paths.contains('Max works at ThoughtWorks which qualifies for TW Credit Union'))
        assertEquals(creditUnions, ['TW Credit Union'])
    }
}
