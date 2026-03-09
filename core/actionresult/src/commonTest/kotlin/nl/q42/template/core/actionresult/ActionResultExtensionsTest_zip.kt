package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionResultExtensionsTest_zip {

    @Test
    fun `zip combines two Success results`() {
        val result1: ActionResult<String, String> = ActionResult.Success("first")
        val result2: ActionResult<Int, String> = ActionResult.Success(42)

        val zipped = result1.zip(result2)

        assertTrue(zipped is ActionResult.Success)
        assertEquals(Pair("first", 42), zipped.data)
    }

    @Test
    fun `zip returns first Error when first fails`() {
        val result1: ActionResult<String, String> = ActionResult.Error("error1")
        val result2: ActionResult<Int, String> = ActionResult.Success(42)

        val zipped = result1.zip(result2)

        assertTrue(zipped is ActionResult.Error)
        assertEquals("error1", zipped.error)
    }

    @Test
    fun `zip returns second Error when second fails`() {
        val result1: ActionResult<String, String> = ActionResult.Success("first")
        val result2: ActionResult<Int, String> = ActionResult.Error("error2")

        val zipped = result1.zip(result2)

        assertTrue(zipped is ActionResult.Error)
        assertEquals("error2", zipped.error)
    }
}

