package com.learn.to_do_netomi.base.domain

interface BaseUseCase<DATA,RETURN> {
    operator fun invoke(data: DATA) : RETURN
}