package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionResultExtensionsTest_mapList {

    @Test
    fun `mapList transforms each item in Success list`() {
        val result: ActionResult<List<Int>, String> = ActionResult.Success(listOf(1, 2, 3))
        val mapped = result.mapList { it * 2 }

        assertTrue(mapped is ActionResult.Success)
        assertEquals(listOf(2, 4, 6), mapped.data)
    }

    @Test
    fun `mapList preserves Error`() {
        val result: ActionResult<List<Int>, String> = ActionResult.Error("error")
        val mapped = result.mapList { it * 2 }

        assertTrue(mapped is ActionResult.Error)
        assertEquals("error", mapped.error)
    }
}

