package unittest.anura.common.json

import anura.common.fp.Result.Success
import anura.common.fp.expectFailure
import anura.common.fp.expectSuccess
import anura.common.fp.flatMap
import anura.common.json.InvalidJson
import anura.common.json.Json.obj
import anura.common.json.LocationAwareJsonNode
import anura.common.json.extractAttributes
import anura.common.json.of
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import org.junit.Test

class LocationAwareJsonNodeExtractAttributesTest {

    private val testJsonAsResult = Success(obj(
        "title" of "Mr",
        "name" of "Bob Smith",
        "address" of obj(
            "houseNumber" of 24,
            "isUkAddress" of true
        )
    ))

    @Test
    fun `success parsing single mandatory attribute`() {
        val result = testJsonAsResult
            .flatMap { it.extractAttributes("/title" to LocationAwareJsonNode::getMandatoryText) }
            .expectSuccess()

        assertThat(result, equalTo("Mr"))
    }

    @Test
    fun `failure parsing single attribute`() {
        testJsonAsResult
            .flatMap { it.extractAttributes("/notPresentAttribute" to LocationAwareJsonNode::getMandatoryText) }
            .expectFailure(isA<InvalidJson>())
    }

    @Test
    fun `success parsing multiple attributes`() {
        val (houseNum, isUkAddress) = testJsonAsResult
            .flatMap {
                it.extractAttributes(
                    "/address/houseNumber" to LocationAwareJsonNode::getMandatoryNumber,
                    "/address/isUkAddress" to LocationAwareJsonNode::getMandatoryBoolean
                )
            }.expectSuccess()

        assertThat(houseNum.toInt(), equalTo(24))
        assertThat(isUkAddress, equalTo(true))
    }

    @Test
    fun `fails if any attributes have parsing errors`() {
        testJsonAsResult
            .flatMap {
                it.extractAttributes(
                    "/address/houseNumber" to LocationAwareJsonNode::getMandatoryNumber,
                    "/address/isUkAddress" to LocationAwareJsonNode::getMandatoryBoolean,
                    "/title" to LocationAwareJsonNode::getMandatoryText,
                    "/notPresent" to LocationAwareJsonNode::getMandatoryText,
                    "/name" to LocationAwareJsonNode::getMandatoryText
                )
            }.expectFailure(isA<InvalidJson>())
    }
}