package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionResultExtensionsTest_mapError {

    @Test
    fun `mapError transforms Error`() {
        val result: ActionResult<String, Int> = ActionResult.Error(5)
        val mapped = result.mapError { it * 2 }

        assertTrue(mapped is ActionResult.Error)
        assertEquals(10, mapped.error)
    }

    @Test
    fun `mapError preserves Success`() {
        val result: ActionResult<String, Int> = ActionResult.Success("data")
        val mapped = result.mapError { it * 2 }

        assertTrue(mapped is ActionResult.Success)
        assertEquals("data", mapped.data)
    }
}

