package ru.walking.backend.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> T.ifNull(block: (T) -> Unit): T {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }

    if (this == null) block(this)

    return this
}
