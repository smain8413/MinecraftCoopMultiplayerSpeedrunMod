package com.github.smain8413.untitled;

import com.github.smain8413.untitled.utils.QueuedAction;

import java.util.ArrayList;
import java.util.List;


public interface ServerWorldExtras {
     List<QueuedAction> tickQueueActions = new ArrayList<>();

     void untitled$runNextTick(QueuedAction action);
}
