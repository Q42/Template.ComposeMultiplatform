package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionResultExtensionsTest_onSuccess {

    @Test
    fun `onSuccess executes action on Success`() {
        var executed = false
        val result: ActionResult<String, String> = ActionResult.Success("data")

        val returned = result.onSuccess { executed = true }

        assertEquals(true, executed)
        assertEquals(result, returned)
    }

    @Test
    fun `onSuccess does not execute action on Error`() {
        var executed = false
        val result: ActionResult<String, String> = ActionResult.Error("error")

        val returned = result.onSuccess { executed = true }

        assertEquals(false, executed)
        assertEquals(result, returned)
    }
}

