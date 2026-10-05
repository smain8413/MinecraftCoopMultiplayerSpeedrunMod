package com.github.smain8413.untitled.mixin;

import com.github.smain8413.untitled.utils.QueuedAction;
import org.apache.http.annotation.Immutable;

import java.util.ArrayList;
import java.util.List;


public interface ServerWorldExtras {
     List<QueuedAction> tickQueueActions = new ArrayList<>();
     default void runNextTick(QueuedAction action) {
        tickQueueActions.add(action);
    }
}
