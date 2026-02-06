package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionResultExtensionsTest_map {

    @Test
    fun `map transforms Success data`() {
        val result: ActionResult<Int, String> = ActionResult.Success(5)
        val mapped = result.map { it * 2 }

        assertTrue(mapped is ActionResult.Success)
        assertEquals(10, mapped.data)
    }

    @Test
    fun `map preserves Error`() {
        val result: ActionResult<Int, String> = ActionResult.Error("error")
        val mapped = result.map { it * 2 }

        assertTrue(mapped is ActionResult.Error)
        assertEquals("error", mapped.error)
    }
}

