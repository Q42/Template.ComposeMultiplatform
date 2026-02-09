package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionResultExtensionsTest_getOrElse {

    @Test
    fun `getOrElse returns data when Success`() {
        val result: ActionResult<String, String> = ActionResult.Success("data")
        assertEquals("data", result.getOrElse { "computed default" })
    }

    @Test
    fun `getOrElse computes default when Error`() {
        val result: ActionResult<String, String> = ActionResult.Error("error")
        assertEquals("computed default", result.getOrElse { "computed default" })
    }
}

