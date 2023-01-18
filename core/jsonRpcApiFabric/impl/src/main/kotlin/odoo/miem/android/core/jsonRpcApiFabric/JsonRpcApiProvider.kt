package odoo.miem.android.core.retrofitApiFabric

import odoo.miem.android.core.retrofitApiFabric.api.JsonRpcApi

/**
 * [JsonRpcApiProvider] - SAM interface for wrapping instance of [JsonRpcApi] and providing in
 * general **Map** of [JsonRpcApiResolver]
 *
 * Usage:
 * JsonRpcApiResolver { SomeRetrofitApiProvider.create() }
 *
 * @see JsonRpcApiResolver
 *
 * @author Vorozhtsov Mikhail
 */
fun interface JsonRpcApiProvider {
    fun get(): JsonRpcApi
}
