package odoo.miem.android.core.jsonrpc.engine.client

import odoo.miem.android.core.jsonrpc.base.engine.JsonRpcCaller
import odoo.miem.android.core.jsonrpc.base.engine.exception.NetworkRequestException
import odoo.miem.android.core.jsonrpc.base.engine.exception.TransportException
import odoo.miem.android.core.jsonrpc.base.engine.protocol.JsonRpcRequest
import odoo.miem.android.core.jsonrpc.base.engine.protocol.JsonRpcResponse
import odoo.miem.android.core.jsonrpc.base.parser.RequestConverter
import odoo.miem.android.core.jsonrpc.base.parser.ResponseParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

// TODO Description
class BaseJsonRpcCaller(
    private val baseUrl: String,
    private val okHttpClient: OkHttpClient,
    private val requestConverter: RequestConverter,
    private val responseParser: ResponseParser
) : JsonRpcCaller {

    override fun call(
        jsonRpcRequest: JsonRpcRequest,
        headers: Map<String, String>,
        paths: List<String>
    ): JsonRpcResponse {
        val requestBody = requestConverter.convert(jsonRpcRequest).toByteArray().toRequestBody()
        val request = Request.Builder()
            .post(requestBody)
            .setHeaders(headers)
            .setUrlWithPath(
                baseUrl = baseUrl,
                paths = paths
            )
            .build()

        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (e: Exception) {
            throw NetworkRequestException(
                message = "Network error: ${e.message}",
                cause = e
            )
        }
        return if (response.isSuccessful) {
            response.body?.let { responseParser.parse(it.bytes()) }
                ?: throw IllegalStateException("Response body is null")
        } else {
            throw TransportException(
                httpCode = response.code,
                message = "HTTP ${response.code}. ${response.message}",
                response = response,
            )
        }
    }

    private fun Request.Builder.setHeaders(headers: Map<String, String>): Request.Builder = apply {
        for ((key, value) in headers) {
            addHeader(key, value)
        }
    }

    private fun Request.Builder.setUrlWithPath(
        baseUrl: String,
        paths: List<String>
    ): Request.Builder = apply {
        url(
            baseUrl + if (paths.isNotEmpty()) paths.joinToString(
                separator = "/",
                postfix = "/"
            ) else ""
        )

    }
}