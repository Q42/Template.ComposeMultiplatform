package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ActionResultExtensionsTest_getErrorOrNull {

    @Test
    fun `getErrorOrNull returns error when Error`() {
        val result: ActionResult<String, String> = ActionResult.Error("error")
        assertEquals("error", result.getErrorOrNull())
    }

    @Test
    fun `getErrorOrNull returns null when Success`() {
        val result: ActionResult<String, String> = ActionResult.Success("data")
        assertNull(result.getErrorOrNull())
    }
}

