package com.learn.to_do_netomi.base.domain

interface SuspendableBaseUseCase<DATA,RETURN> {
    suspend operator fun invoke(data: DATA) : RETURN
}