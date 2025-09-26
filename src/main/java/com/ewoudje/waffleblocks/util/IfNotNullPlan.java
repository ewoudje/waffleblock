package com.ewoudje.waffleblocks.util;

import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.task.TaskExecutor;
import dev.engine_room.flywheel.lib.task.SimplyComposedPlan;

import java.util.function.Function;
import java.util.function.Supplier;

public class IfNotNullPlan<C, V> implements SimplyComposedPlan<C> {
    private final Supplier<V> supplier;
    private final Function<V, Plan<C>> plan;

    public IfNotNullPlan(Supplier<V> supplier, Function<V, Plan<C>> plan) {
        this.supplier = supplier;
        this.plan = plan;
    }

    @Override
    public void execute(TaskExecutor taskExecutor, C context, Runnable onCompletion) {
        var value = supplier.get();
        if (value != null) {
            plan.apply(value).execute(taskExecutor, context, onCompletion);
        } else {
            onCompletion.run();
        }
    }
}
