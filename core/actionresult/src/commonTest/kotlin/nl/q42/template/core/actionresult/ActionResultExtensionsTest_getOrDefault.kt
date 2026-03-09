package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionResultExtensionsTest_getOrDefault {

    @Test
    fun `getOrDefault returns data when Success`() {
        val result: ActionResult<String, String> = ActionResult.Success("data")
        assertEquals("data", result.getOrDefault("default"))
    }

    @Test
    fun `getOrDefault returns default when Error`() {
        val result: ActionResult<String, String> = ActionResult.Error("error")
        assertEquals("default", result.getOrDefault("default"))
    }
}

