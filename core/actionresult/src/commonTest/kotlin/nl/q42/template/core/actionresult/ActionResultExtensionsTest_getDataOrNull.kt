package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ActionResultExtensionsTest_getDataOrNull {

    @Test
    fun `getDataOrNull returns data when Success`() {
        val result: ActionResult<String, String> = ActionResult.Success("data")
        assertEquals("data", result.getDataOrNull())
    }

    @Test
    fun `getDataOrNull returns null when Error`() {
        val result: ActionResult<String, String> = ActionResult.Error("error")
        assertNull(result.getDataOrNull())
    }
}

