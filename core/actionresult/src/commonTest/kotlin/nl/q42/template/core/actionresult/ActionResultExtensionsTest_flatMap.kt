package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionResultExtensionsTest_flatMap {

    @Test
    fun `flatMap chains successful transformations`() {
        val result: ActionResult<Int, String> = ActionResult.Success(5)
        val mapped = result.flatMap { ActionResult.Success(it * 2) }

        assertTrue(mapped is ActionResult.Success)
        assertEquals(10, mapped.data)
    }

    @Test
    fun `flatMap returns Error from transformation`() {
        val result: ActionResult<Int, String> = ActionResult.Success(5)
        val mapped = result.flatMap { ActionResult.Error("error") }

        assertTrue(mapped is ActionResult.Error)
        assertEquals("error", mapped.error)
    }

    @Test
    fun `flatMap preserves original Error`() {
        val result: ActionResult<Int, String> = ActionResult.Error("original error")
        val mapped = result.flatMap { ActionResult.Success(it * 2) }

        assertTrue(mapped is ActionResult.Error)
        assertEquals("original error", mapped.error)
    }
}

