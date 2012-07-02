package com.devopsy.graph.examples

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph
import com.tinkerpop.gremlin.groovy.Gremlin
import org.junit.Before

class ComplexGraphTest extends GroovyTestCase {
    static {
        Gremlin.load()
    }

    Graph g
    InferenceGraph ig

    @Before
    void setUp() {
        g = new TinkerGraph()
        g.loadGraphML('moremax.graphml')
        ig = new InferenceGraph(g)
    }

    void testMax() {
        g.e('Max_TW').duration = 366
        def results = ig.solve('Max')
        def paths = ig.getDisplayPaths()
        def creditUnions = ig.getEligibleCUs()
        assertTrue(paths.contains('Max works at ThoughtWorks which qualifies for TW Credit Union'))
        assertTrue(paths.contains('Max lives at NYC which qualifies for Big Apple Credit Union'))
        assertTrue(paths.contains('Max studied at Drexel which qualifies for ACME Credit Union'))
        assertTrue(paths.contains('Max lives in Manhattan which qualifies for Big Apple Credit Union'))
        assertEquals(4, results.size)
        assertEquals(3, creditUnions.size)
    }

    void testMaxWrongDegree() {
        g.e('Max_Drexel').degree = 'BSCS'
        def results = ig.solve('Max')
        def paths = ig.getDisplayPaths()
        def creditUnions = ig.getEligibleCUs()
        assertTrue(paths.contains('Max works at ThoughtWorks which qualifies for TW Credit Union'))
        assertTrue(paths.contains('Max lives at NYC which qualifies for Big Apple Credit Union'))
        assertTrue(paths.contains('Max lives in Manhattan which qualifies for Big Apple Credit Union'))
        assertEquals(3, results.size)
        assertEquals(2, creditUnions.size)
    }

    void testMaxAllWrong(){
        g.e('Max_Drexel').degree = 'BSCS'
        g.e('Max_TW').duration = 1
        def results = ig.solve('Max')
        def paths = ig.getDisplayPaths()
        def creditUnions = ig.getEligibleCUs()
        assertTrue(paths.contains('Max lives at NYC which qualifies for Big Apple Credit Union'))
        assertTrue(paths.contains('Max lives in Manhattan which qualifies for Big Apple Credit Union'))
        assertEquals(2, results.size)
        assertEquals(1, creditUnions.size)
    }

    void testEvan() {
        def results = ig.solve('Evan')
        def paths = ig.getDisplayPaths()
        def creditUnions = ig.getEligibleCUs()
        assertTrue(paths.contains('Evan plays in Flabberghaster which qualifies for Rocking Credit Union'))
        assertEquals(1, results.size)
        assertEquals(creditUnions, ['Rocking Credit Union'])
    }
}
