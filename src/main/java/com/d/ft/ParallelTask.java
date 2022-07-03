package com.d.ft;

public abstract class ParallelTask<Param, ReturnValue> {

    abstract ParallelResult<ReturnValue> call(Param param) throws Exception;

}
