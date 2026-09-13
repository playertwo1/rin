package com.playertwo1.rin

import com.playertwo1.rin.ui.navigation.RinDestination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class NavigationDestinationTest {

    @Test
    fun verifyAllDestinations_haveDistinctRoutes_andValidResources() {
        val destinations = RinDestination.entries
        assertEquals(4, destinations.size)

        val routes = destinations.map { it.route }
        assertEquals(routes.distinct().size, routes.size)

        assertEquals("home", RinDestination.startDestination.route)

        destinations.forEach { dest ->
            assertNotNull(dest.icon)
            assert(dest.labelResId != 0)
        }
    }
}
